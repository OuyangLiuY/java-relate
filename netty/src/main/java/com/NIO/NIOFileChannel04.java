package com.NIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/home/workspace/Spring_framework/images/netty/NIO-selector-buffer-channel.png");
        FileOutputStream outputStream = new FileOutputStream("copy.png");

        FileChannel in = fileInputStream.getChannel();
        FileChannel dest =   outputStream.getChannel();

        dest.transferFrom(in,0,in.size());

        fileInputStream.close();
        outputStream.close();
    }
}
