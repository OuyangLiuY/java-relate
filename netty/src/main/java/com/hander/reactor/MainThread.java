package com.hander.reactor;

public class MainThread {

    public static void main(String[] args) {
        //
        SelectorThreadGroup boss = new SelectorThreadGroup(3);
        SelectorThreadGroup group = new SelectorThreadGroup(3);
        group.setWorker(boss);
        group.bind(9999);
    }
}
