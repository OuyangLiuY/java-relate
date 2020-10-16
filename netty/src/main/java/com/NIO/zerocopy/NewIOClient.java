package com.NIO.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args)throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",7001));
        String fileName = "/home/workspace/Spring_framework/algorithm/src/main/java/leetcode/editor/cn/all.json";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();
        //在linux下一个transferTo 方法就可以完成传输
        //在windows 下一次调用transferTo 只能发送8M数据,就需要分段传输文件,
       //transferTo 底层使用的是零拷贝
        long transfer = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数 = " + transfer + ",耗时 : " + (System.currentTimeMillis() - startTime));
    }
}
