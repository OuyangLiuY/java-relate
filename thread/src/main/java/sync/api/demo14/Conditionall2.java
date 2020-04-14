package sync.api.demo14;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 实现10个消费者，消费，2个生产者，生产的阻塞调用;保证数据不能大于10条
 */
public class Conditionall2<T> {
    LinkedList<T> list = new LinkedList<>();
    static int MAX = 10;
    int count = 0;

    public synchronized void put(T t) {
        try {
            while (MAX == list.size()) {
                this.wait();
            }
            count++;
            list.add(t);

            System.out.println("put:" + t);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.notifyAll();
    }

    public synchronized T get() {
        T t = null;
            while (list.size() <= 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t = list.removeFirst();
            count --;

            System.out.println("get:" + t);
            System.out.println("size:" + count +"?" + list.size());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.notifyAll();
        return t;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        Conditionall2<String> c = new Conditionall2<>();
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
