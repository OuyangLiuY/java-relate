package com.ywb.sync;

public class SyncDemo {
    Object o= new Object();

    public static void main(String[] args) {
        System.out.println("pppppppppppppppppppppp");
        SyncDemo syncDemo = new SyncDemo();
        syncDemo.start();
    }



    public void start() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        //Thread.sleep(500);
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
                       // Thread.sleep(500);
                        sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.setName("t1");
//        thread.wait();
//        thread.notify();

        thread2.setName("t2");

        thread.start();
         thread2.start();
    }

    /**
     * synchronized   实现一把锁
     * 偏向锁-  我们的方法一定要保证线程安全---但是实际情况不一定有互斥
     * 所以偏向锁是synchronized锁的对象如果没有资源竞争的情况下存在的
     * 偏向锁不会调用os函数实现---第一次会调用
     *
     * os函数   pthread_mutex_lock()//上锁
     * pthread_mutex_lock(){
     *
     *     fprintf(stderr,"msg"+pthred_self());
     * }
     *
     *
     * 只有调用pthread_mutex_lock才回到打印
     * 只有 同步方法 重量级锁才会打印
     * 偏向锁
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * 轻量级锁
     * 重量级锁
     *
     *
     *
     */
   // public native void  gettid();
    public   void sync() throws InterruptedException {
       // gettid();//打印了当前线程的id
    }

        //  System.out.println(Thread.currentThread().getName() + "----xxxxttttxxx");
   // }
}
