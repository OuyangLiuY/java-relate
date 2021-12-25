package com.ouyangliuy.hander.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {

    SelectorThread[] selectorThreads;
    ServerSocketChannel server=null;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    SelectorThreadGroup worker = this;
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
//            registerSelector(server);
            registerSelectorV3(server);

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

    public void registerSelectorV3(Channel channel) {
        if(channel instanceof ServerSocketChannel){
            // listen选择boss组中一个线程后，要更新这个线程的worker组
            SelectorThread selector = nextSelector();
            selector.getDeque().add(channel);
            //selector.setWorker(this.worker);
            selector.getSelector().wakeup();
        }else {
        // 取堆中的selectorThread对象
        SelectorThread selector = nextSelectorV3();
        //1.通过队列传递消息数据
        selector.getDeque().add(channel);
        //2.通过打断阻塞，让对应的线程去自己在打断后完成注册selector
        selector.getSelector().wakeup();
        }
    }



    private SelectorThread nextSelector() {
        System.out.println(Thread.currentThread().getName() + " nextSelector length = " + selectorThreads.length);
        int index = atomicInteger.getAndIncrement() % selectorThreads.length;
        return selectorThreads[index];
    }

    private SelectorThread nextSelectorV3() {
        int index = atomicInteger.getAndIncrement() % worker.selectorThreads.length;
        return worker.selectorThreads[index];
    }

    public void setWorker(SelectorThreadGroup worker) {
        this.worker = worker;
    }
}
