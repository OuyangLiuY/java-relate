package com.ywb.synchronize;

class SynchronizedTwo implements Runnable{

    /**
     * 一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞
     */
    private static int count;

    public SynchronizedTwo() {
        count = 0;
    }

    public  void run() {
        synchronized(this) {
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + (count++));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCount() {
        return count;
    }


}
 class Exec {
     /**
      * 当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，
      * 在同一时刻只能有一个线程得到执行，另一个线程受阻塞，必须等待当前线程执行完这个代码块以后才能执行该代码块。
      * Thread1和thread2是互斥的，因为在执行synchronized代码块时会锁定当前的对象，
      * 只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象
      */
    public static void main(String args[]){
        SynchronizedTwo s = new SynchronizedTwo();
        Thread t1 = new Thread(s);
        Thread t2 = new Thread(s);
        t1.start();
        t2.start();
    }
}

class Counter implements  Runnable{
    private volatile int count;

    public Counter() {
        count = 0;
    }
    public void run() {
        String threadName = Thread.currentThread().getName();
        if (threadName.equals("A")) {
            countAdd();
        } else if (threadName.equals("B")) {
            printCount();
        }
    }


    public void countAdd() {
        synchronized(this) {
            for (int i = 0; i < 5; i ++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + (++count));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //非synchronized代码块，未对count进行读写操作，所以可以不用synchronized
    public void printCount() {
        for (int i = 0; i < 5; i ++) {
            try {
                System.out.println(Thread.currentThread().getName() + " count:" + count);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Execute{
    /**
     * 可以看见B线程的调用是非synchronized,并不影响A线程对synchronized部分的调用。
     * 从上面的结果中可以看出一个线程访问一个对象的synchronized代码块时，
     * 别的线程可以访问该对象的非synchronized代码块而不受阻塞。
     * @param args
     */
    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread thread1 = new Thread(counter, "A");
        Thread thread2 = new Thread(counter, "B");
        thread1.start();
        thread2.start();
    }
}