package com.luban.demo8;

import java.util.concurrent.TimeUnit;

/**
 * T2线程能否执行？
 */
public class Demo {

    int count = 0;

    synchronized void test(){
        System.out.println(Thread.currentThread().getName() + " start......");
        while (true) {
            count ++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count == 5) {
                //碰到异常的情况，如果没有处理，会自动释放锁，所以T2可以执行。
                int i = 1/0;
            }
        }
    }

    public static void main(String[] args) {
        Demo demo11 = new Demo();

        Runnable r = () -> demo11.test();

        new Thread(r, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(r, "t2").start();
    }

}
