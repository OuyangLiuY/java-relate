package com.hander.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {

    SelectorThread[] selectorThreads;
    ServerSocketChannel server=null;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    SelectorThreadGroup bossGroup = this;
    public SelectorThreadGroup(int num) {
        selectorThreads = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            selectorThreads[i] = new SelectorThread(this);
            new Thread(selectorThreads[i]).start();
        }
    }

    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            //将server注册到哪个selector呢
            registerSelector(server);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 既需要注册server端的channel也需要注册客户端的channel
    public void registerSelector(Channel channel) {
        SelectorThread selector = nextSelector();
        selector.getDeque().add(channel);
        selector.getSelector().wakeup();
    }

    private SelectorThread nextSelector() {
        int index = atomicInteger.getAndIncrement() % selectorThreads.length;
        return selectorThreads[index];
    }

    private SelectorThread nextWorkerSelector() {
        int index = atomicInteger.getAndIncrement() % bossGroup.selectorThreads.length;
        return bossGroup.selectorThreads[index];
    }

    public void setWorker(SelectorThreadGroup boss) {
        this.bossGroup = boss;
    }
}
