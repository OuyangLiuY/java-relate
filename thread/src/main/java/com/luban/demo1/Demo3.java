package com.luban.demo1;

public class Demo3 {

    private int count = 10;

    //直接加在方法声明上，相当于是synchronized(this)
    public synchronized void test(){
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

}
