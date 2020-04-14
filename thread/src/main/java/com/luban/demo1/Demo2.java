package com.luban.demo1;

public class Demo2 {

    private int count = 10;

    public void test(){
        //synchronized(this)锁定的是当前类的实例,这里锁定的是Demo2类的实例
        synchronized (this){
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }

}
