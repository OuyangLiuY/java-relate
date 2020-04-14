package sync.api.demo3;

/**
 * 问题就在于线程重入的问题，
 * 第一个线程减了个1变成9了，还没打印，第二个线程又减了个1，第三个线程又减了个1，
 * 这时候虽然第一个线程只减了一个1但是却打印出来一个7(这里情况是不一定的)
 * 可以给方法加上synchronized
 */
public class Demo1 implements Runnable{

    private int count = 10;

    @Override
    public /*synchronized*/ void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }


    public static void main(String[] args) {
        Demo1 demo = new Demo1();
        for (int i = 0; i < 5; i++) {
            new Thread(demo,"THREAD" + i).start();
        }
    }

}
