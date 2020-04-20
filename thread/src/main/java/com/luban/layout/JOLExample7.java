package com.luban.layout;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.CountDownLatch;
import static java.lang.System.out;
public class JOLExample7 {
    static A a;
    public static void main(String[] args) throws Exception {
        //Thread.sleep(5000);
        a = new A();
        out.println("befre concur.lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());//无锁

        Thread t1= new Thread(){
            public void run() {
                synchronized (a){
                    try {
                        Thread.sleep(1500);
                        System.out.println("t1 release");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t1.start();
        Thread.sleep(1000);
        out.println("t1 concur.lock ing");
        out.println(ClassLayout.parseInstance(a).toPrintable());//轻量锁
        sync();

        out.println("after concur.lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());//无锁

        System.gc();
        out.println("after gc()");
        out.println(ClassLayout.parseInstance(a).toPrintable());//无锁---gc
    }

    public  static  void sync() throws InterruptedException {
        synchronized (a){
            System.out.println("t1 main concur.lock");
            out.println(ClassLayout.parseInstance(a).toPrintable());//重量锁
        }
        //Thread.sleep(1000);
    }
}
