package com;

import sun.misc.Contended;

public class T02_Contended {
    @Contended
    long a = 0L;
    @Contended
    long b = 0L;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("???");
        T02_Contended t = new T02_Contended();
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 1_000_000_000L; i++) {
                t.a= i;
            }
        });
        Thread t2 = new Thread(()->{
            for (int i = 0; i < 1_000_000_000L; i++) {
                t.b= i;
            }
        });
        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start)/100_0000);
    }
}
