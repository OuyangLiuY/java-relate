package com.ouyangliuy.NIO;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str = "hello file Channel";
        //获取输出流
        FileOutputStream fileOutputStream = new FileOutputStream("/home/workspace/Spring_framework/netty/a.txt");
        //获取文件管道
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个byteBuffer,并将数据写入
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        //读写切换
        buffer.flip();
        channel.write(buffer);
    }
}
