package com.ouyangliuy.NIO.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final  int PORT = 6667;
    //初始化
    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen(){
        try {
            while(true){
                int count = selector.select();
                if(count > 0){ //有事件处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + " 上线了 ");
                        }
                        if(key.isReadable()){//通道发生read事件,即通道中的数据可读状态
                            //处理读
                            readData(key);
                        }
                        //remove防止重复处理
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待....");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //读取客户端消息
    private void readData(SelectionKey key){
        //定义一个socketChannel
        SocketChannel channel = null;
        try{
            //得到channel
            channel = (SocketChannel) key.channel();
            //创建一个buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count的值处理
            if(count > 0){
                String msg = new String(buffer.array());
                System.out.println("from 客户端 : " + msg.trim());
                //向其他的客户端转发消息
                sendInfoToOtherClients(msg,channel);
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                assert channel != null;
                System.out.println(channel.getRemoteAddress() + "  离线了");
                //取消注册
                key.cancel();;
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    //转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws Exception{
        System.out.println("服务器转发消息中....");
        //遍历 所有注册到selector上的SocketChannel,并排除自己
        Iterator<SelectionKey> iterator = selector.keys().iterator();
        while(iterator.hasNext()) {
            SelectionKey key = iterator.next();
            //通过key取出socketChannel
            Channel channel = key.channel();
            if(channel instanceof SocketChannel && channel != self){
                SocketChannel dest = (SocketChannel) key.channel();
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }
    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
