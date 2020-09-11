package com.NIO.zerocopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * java IO 的服务器
 */
public class OldIOServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        byte[] bytes = new byte[4096];
        while(true){
            int readCount = dataInputStream.read(bytes,0,bytes.length);
            if(-1 == readCount){
                break;
            }
        }
    }
}
