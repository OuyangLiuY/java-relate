package com.ouyangliuy.NIO.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    //定义属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public GroupChatClient() {
        try {
            this.selector = Selector.open();
            //链接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到 userName
            this.userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) throws IOException {
        info = userName + " 说 " + info;
        socketChannel.write(ByteBuffer.wrap(info.getBytes()));
    }

    public void readInfo() throws IOException {
        int select = selector.select();
        if (select > 0) { //有可用的通道
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    //得到相关通道
                    SocketChannel sc = (SocketChannel) key.channel();
                   // sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //读取
                    sc.read(buffer);
                    System.out.println("发送消息--");
                    System.out.println(userName + " : " + new String(buffer.array()).trim());
                    iterator.remove();
                }
            }

        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient client = new GroupChatClient();
        //启动一个线程
        new Thread(() -> {
            while (true) {
                try {
                    client.readInfo();
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //发送数据哥服务器端
        Scanner scanner = new Scanner(System.in);
        String ss = scanner.nextLine();
        client.sendInfo(ss);

    }
}