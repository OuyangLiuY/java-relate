package com.ywb.sync;

import lock.A;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;

public class SyncTest {

    static A a;
    static ArrayList<A> as = new ArrayList<>();

    //-XX:BiasedLockingStartupDelay=0
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                a = new A();
                synchronized (a) {
                    as.add(a);
                }
                if (i == 20) {
                    System.out.println("aaa");
                    System.out.println(ClassLayout.parseInstance(a).toPrintable());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1");
        t1.start();
        t1.join();

        Thread t2 = new Thread(() -> {
            int k = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 50; i++) {
                a = as.get(i);
                synchronized (a) {
                    if (i == 20 || i ==16 || i==18 || i==39 ||i ==41 ||i ==40) {
                        System.out.println(ClassLayout.parseInstance(a).toPrintable()+" ---t2 ing \t\t" + i);
                    }
                }
            }
        }, "t2");
        t2.start();
        t2.join();

       /* Thread.sleep(1000);
        Thread tmp  =  new Thread(()->{
            for (int i = 0; i < 15; i++) {
                a = as.get(i);
                synchronized (a) {
                    System.out.println("tmp");
                    if (i > 0) {
                        System.out.println("tmp ing");
                        System.out.println(ClassLayout.parseInstance(a).toPrintable() + "\n --- ");
                    }
                }
            }

        },"tmp");
        tmp.start();
        tmp.join();*/
    }

}
