package sync.api.demo15;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RLDemo1 {

    /**
     * reentrantlock用于替代synchronized
     * 使用reentrantlock可以完成同样的功能
     * reentrantlock必须要手动释放锁
     * 使用syn锁定的话如果遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，
     * 因此经常在finally中进行锁的释放
     */
    Lock lock = new ReentrantLock();

    public void test1(){
        try {
            lock.lock();//this
            for (int i = 0; i < 3; i++) {
                System.out.println(i);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void test2(){
        lock.lock();
        System.out.println("test 2...");
        lock.unlock();
    }

    public static void main(String[] args) {
        RLDemo1 rlDemo1 = new RLDemo1();
        new Thread(rlDemo1::test1).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rlDemo1::test2).start();
    }

}
