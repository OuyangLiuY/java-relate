package com.NIO;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明
 * 1.MappedByteBuffer  可让文件直接在内存(堆外内存)中修改,操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile accessFile = new RandomAccessFile("/home/workspace/Spring_framework/netty/a.txt","rw");
        //获取通道
        FileChannel channel = accessFile.getChannel();
        /**
         * 参数1: FileChannel.MapMode.READ_WRITE 使用读写模式
         * 参数2: 0, 可以直接修改的起始位置
         * 参数3: 5, 是映射到内存的大小,即将a.txt的多少个字节映射到内存
         * 可以直接修改的范围是0 - 5
         * 实际类型是 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        //上面参数5 是大小位置,而不是索引位置5,IndexOutOfBoundsException
        mappedByteBuffer.put(5, (byte) 'X');

        accessFile.close();
    }
}
