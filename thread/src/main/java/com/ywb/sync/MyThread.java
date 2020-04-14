package com.ywb.sync;

import org.openjdk.jol.info.ClassLayout;

public class MyThread {

    Object o= new Object();

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("pppppppppppppppppppppp");
        //SyncDemo syncDemo = new SyncDemo();
       // syncDemo.start();
        MyThread thread = new MyThread();
        thread.start();

        System.out.println(ClassLayout.parseInstance(MyThread.class).toPrintable());
        System.out.println(HashUtil.countHash(thread));
    }
    public void start() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        System.out.println("aaa");
                        sync();
                    } catch (InterruptedException e) {

                    }
                }
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                while (true) {
                    try {
                         Thread.sleep(500);
                        sync();

                        System.out.println("bbb");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.setName("t1");
        thread2.setName("t2");
        thread.start();
        thread2.start();
    }
   // public native void  gettid();
    public   void sync() throws InterruptedException {
       // gettid();//打印了当前线程的id
        synchronized (o){
            System.out.println("111");
        }
    }
}
