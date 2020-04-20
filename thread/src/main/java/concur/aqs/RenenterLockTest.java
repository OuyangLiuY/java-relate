package concur.aqs;

import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RenenterLockTest {

    static ReentrantLock lock = new ReentrantLock(true);

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> method(), "t1");
        Thread t2 = new Thread(() -> {
            method();
        }, "t2");

        t1.start();

        t2.start();
    }

    static void method() {
        System.out.println("try lock");
        lock.lock();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": this is a locked block");
        lock.unlock();
        System.out.println("release");
    }

    static class ReadWriter {
        static int aa = 0;
        static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        public static void main(String[] args) {
            int SHARED_SHIFT = 16;
            System.out.println(Integer.toBinaryString(SHARED_SHIFT));
            int r = 1 << SHARED_SHIFT;
            System.out.println(r);
            System.out.println(Integer.toBinaryString(r));
            System.out.println(Integer.toBinaryString((r)-1));


            /*for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    get();
                }, "t1-" + i).start();
            }
            for (int i = 0; i < 20; i++) {
                int finalI = i;
                new Thread(() -> {
                    put(finalI);
                }, "t2-" + i).start();
            }*/
        }

        static void get() {
            readWriteLock.readLock().lock();
            try {
                Thread.sleep(1000);
                System.out.println("read:" + Thread.currentThread().getName() + "--" + aa);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.readLock().unlock();
            }

        }

        static void put(int a) {
            readWriteLock.writeLock().lock();
            try {
                aa = a;
                Thread.sleep(1000);
                System.out.println("write:" + Thread.currentThread().getName() + "--" + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.writeLock().unlock();
            }

        }
    }

}
