package com.ouyangliuy.NIO;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        File file = new File("/home/workspace/Spring_framework/netty/a.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        //获取输入流通道
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据读到buffer
        channel.read(buffer);
        //将byteBuffer的数据输出
        System.out.println(new String(buffer.array()));
        fileInputStream.close();
    }
}
