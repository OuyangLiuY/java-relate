package concur.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * atomicXXX
 * 一道面试题：多个atomic类连续调用能否构成原子性?
 */
public class AtomicTest {

    AtomicInteger count = new AtomicInteger(0);

    //比如count加到999了，这时候一个线程拿到count判断，虽然.get方法保证原子性，但是他阻止
    //不了其它线程也来判断，所以第一个线程还没加完，第二个线程也进来了，这时候两个线程都给count加了
    public void test(){
        for (int i = 0; i < 10000; i++) {
            if(count.get() < 1000){
                count.incrementAndGet();
            }
        }
    }
    public static void main(String[] args) {
        AtomicTest atomicTest = new AtomicTest();

        List<Thread> threads = new ArrayList();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(atomicTest::test, "thread-" + i));
        }

        threads.forEach(Thread::start);

        threads.forEach((o)->{
            try {
                o.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println(atomicTest.count);
    }

}
