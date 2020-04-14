package sync.api.demo14;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现10个消费者，消费，2个生产者，生产的阻塞调用;保证数据不能大于10条
 */
public class Conditionall<T> {
    private Lock lock = new ReentrantLock();
    private Condition product = lock.newCondition();
    private Condition consumer = lock.newCondition();
    LinkedList<T> list = new LinkedList<>();
    static int MAX = 10;
    int count = 0;

    public void put(T t) {
        lock.lock();
        try {
            while (MAX == list.size()) {
                product.await();
            }
            count++;
            list.add(t);
            consumer.signalAll();
            System.out.println("put:" + t);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public T get() {
        lock.lock();
        T t = null;
        try {
            while (list.size() <= 0) {
                consumer.await();
            }
            t = list.removeFirst();
            count --;
            product.signalAll();
            System.out.println("get:" + t);
            System.out.println("size:" + count +"?" + list.size());
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        Conditionall<String> c = new Conditionall<>();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    c.get();
                }
            }, "c").start();
        }

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            new Thread(() -> {
                int j = 1;
                while (true){
                    j++;
                    c.put(Thread.currentThread().getName() + "-" + finalI+"---"+j);
                }
            }, "p").start();
        }
    }
}
