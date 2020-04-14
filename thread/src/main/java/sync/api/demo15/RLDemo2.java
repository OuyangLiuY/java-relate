package sync.api.demo15;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RLDemo2 {

    Lock lock = new ReentrantLock();

    public void test1(){
        try {
            lock.lock();
            for (int i = 0; i < 2; i++) {
                System.out.println(i);
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 使用reentrantlock可以进行“尝试锁定”tryLock，这样无法锁定，或者在指定时间内无法锁定，
     * 线程可以决定是否继续等待
     * 可以使用tryLock进行尝试锁定，不管锁定与否，方法都将继续执行
     * 可以根据tryLock的返回值来判定是否锁定
     * 也可以指定tryLock的时间，由于tryLock(time)抛出异常，所以要注意unclock的处理，必须放到finally中
     */
    public void test2(){
        boolean locked = false;
        try {
            locked = lock.tryLock(3, TimeUnit.SECONDS);
            System.out.println("test2...." + locked);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (locked) {
                System.out.println("test2 end");
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        RLDemo2 rlDemo2 = new RLDemo2();
        new Thread(rlDemo2::test1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rlDemo2::test2).start();
    }

}
