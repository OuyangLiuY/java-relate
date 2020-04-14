package com.luban.demo4;
//同步方法和非同步方法是否可以同时调用？可以
public class Demo{

    public synchronized void test1(){
        System.out.println(Thread.currentThread().getName() + " test1 start...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " test1 end...");
    }

    public void test2(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " test2");
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        new Thread(demo :: test1,"test1").start();
        new Thread(demo :: test2,"test2").start();
    }

}