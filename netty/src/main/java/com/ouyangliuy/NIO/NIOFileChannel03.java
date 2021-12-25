package com.ouyangliuy.NIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/home/workspace/Spring_framework/netty/a.txt");
        FileOutputStream outputStream = new FileOutputStream("/home/workspace/Spring_framework/netty/out.txt");

        FileChannel in = fileInputStream.getChannel();
        FileChannel out =   outputStream.getChannel();

        ByteBuffer allocate = ByteBuffer.allocate(1024);
        while (true){
            //这里有一个重要的操作,重置
            allocate.clear();//清空buffer
            int read = in.read(allocate);
            //表示读完
            if(read == -1){
                break;
            }
            //将文件写入 out.txt
            allocate.flip();
            out.write(allocate);
        }
        fileInputStream.close();
        outputStream.close();
    }
}
