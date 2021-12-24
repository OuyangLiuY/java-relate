package com.ouyangliuy.hander.reactor;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

public class SelectorThread  implements  Runnable{

    SelectorThreadGroup group;
    public SelectorThread(SelectorThreadGroup group) {
        try {
            this.selector = Selector.open();
            this.group = group;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Selector selector = null;
    private final LinkedBlockingDeque<Channel> deque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true){
            try {
                //1 阻塞获取数据，如果需要跳过阻塞，需要调用 selector 的 wakeUp() 方法
                int num = selector.select();
                //2
                if(num > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()){
                        SelectionKey key = it.next();
                        it.remove();
                        // 接收
                        if(key.isAcceptable()){
                            acceptHandler(key);
                        }else if(key.isReadable()){ // 可读
                            readHandler(key);
                        }else if(key.isWritable()){ // 可写
                            writeHandle(key);
                        }
                    }
                }
                //3 处理一些 task： listen client
                if(!deque.isEmpty()){
                    Channel channel = deque.take();
                    if(channel instanceof ServerSocketChannel){ // 服务端channel
                        ServerSocketChannel serverChannel = (ServerSocketChannel)channel;
                        serverChannel.register(selector,SelectionKey.OP_ACCEPT);
                        System.out.println(Thread.currentThread().getName() + "" + " register listen");
                    }else if(channel instanceof SocketChannel){ // 客户端channel
                        SocketChannel clientChannel = (SocketChannel)channel;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        clientChannel.register(selector,SelectionKey.OP_READ,buffer);
                        System.out.println(Thread.currentThread().getName() + "" + " register client : " + clientChannel.getRemoteAddress());
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeHandle(SelectionKey key) {

    }

    private void readHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " readHandler ...");
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true){
            try {
                int read = client.read(buffer);
                if(read > 0){
                    buffer.flip();
                    // 读取类荣然后直接写出
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    buffer.clear();
                }else if(read ==0){
                    // 没有数据读到
                    break;
                }else {
                    // 客户端断开
                    System.out.println(Thread.currentThread().getName() + " 客户端断开了..." + client.getRemoteAddress());
                    key.cancel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " acceptHandler ...");
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            // choose a selector and register
            group.registerSelectorV3(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedBlockingDeque<Channel> getDeque() {
        return deque;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setWorker(SelectorThreadGroup workerGroup) {
        this.group = workerGroup;
    }
}
