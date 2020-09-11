package com.NIO.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

public class OldIOClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1",7001);
        String fileName = "/home/workspace/Spring_framework/algorithm/src/main/java/leetcode/editor/cn/all.json";
        InputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount ;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while((readCount = inputStream.read(buffer)) >= 0){
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送字节总数:" + total + ",耗时=" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        inputStream.close();
        socket.close();
    }
}
