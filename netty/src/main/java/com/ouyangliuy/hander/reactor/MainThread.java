package com.ouyangliuy.hander.reactor;

public class MainThread {

    public static void main(String[] args) {
        // boss 线程用来接受客户端链接，然后将连接分发给worker线程去处理
        SelectorThreadGroup boss = new SelectorThreadGroup(1);
        SelectorThreadGroup worker = new SelectorThreadGroup(3);
        boss.setWorker(worker);
        boss.bind(9999);
        boss.bind(8888);
        boss.bind(7777);
        boss.bind(6666);
    }
}
