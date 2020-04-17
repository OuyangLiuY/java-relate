package lock;


import com.luban.layout.A;
import org.openjdk.jol.info.ClassLayout;

import static java.lang.System.out;

/**
 * 注意：
 *      1.JVM默认是在启动5秒之后才可以设置偏向锁，可使用该命令直接开始设置而不需要延迟：
 *             -XX:+UseBiasedLocking(开启偏向锁) -XX:BiasedLockingStartupDelay=0(偏向锁直接加载)
 *      2.当一个对象调用hashcode()方法之后，该对象不能被设置为偏向锁对象。
 *
 * 锁：（64位hotSpot）
 *  header状态：
 *      1.无锁
 *      25:unused + 31:hash +1:unused + 4:age +  1:biased_lock + 2:lock = 64bit
 *      eg:56 + 0 0000 0 01 ->无锁
 *      2.偏向锁
 *      54:thread& + 2:epoch + 1:unused + 4:age + 1:biased_lock + 2:lock =64bit
 *      eg:56 + 0 0000 1 01 ->偏向锁
 *      3.轻量级锁
 *                 62:prt_to_lock_record                        + 2:lock = 64bit
 *      eg:56 + 001010 00 ->轻量级锁
 *      4.重量级锁
 *               62:prt_to_heavyweight_monitor                  + 2:lock  = 64bit
 *      eg: 56 + 111010 10 ->重量级锁
 */
public class LockSpeedTest {

    static AA a;
    static int aa = 0;
    static class NoLockSpeed{

        public static void main(String[] args) {
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                method();
            }
            long end = System.currentTimeMillis();
            //10ms
            System.out.println((end - begin) + "ms");
            System.out.println(aa);
        }
        static void method(){
            aa ++;
        }
    }
    static class BiasedLockSpeed{

        public static void main(String[] args) throws InterruptedException {
            Thread.sleep(5000);
            a = new AA();
            System.out.println("before:");
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                if(aa == 100000){
                    System.out.println("ing:");
                    System.out.println(ClassLayout.parseInstance(a).toPrintable());
                }
                method();
            }
            long end = System.currentTimeMillis();
            // 180ms
            System.out.println((end - begin) + "ms");
            System.out.println(a.getAa());
        }
        static void method(){
            synchronized (a){
                aa ++;
            }
        }
    }
    static class LightLockSpeed{

        public static void main(String[] args) throws InterruptedException {
            //Thread.sleep(5000); //-XX:BiasedLockingStartupDelay=0
            a = new AA();
            System.out.println("before:");
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                method();
                if(aa==100000){
                    System.out.println("++++++:" + aa);
                    System.out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
            long end = System.currentTimeMillis();
            // 2000ms
            System.out.println((end - begin) + "ms");
            System.out.println(aa);
        }

       static void method(){
            synchronized (a){
                aa ++;
                if(aa==100000){
                    System.out.println("++++++:" + aa);
                    System.out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
        }

    }
    static class HeavyLockSpeed{

        public static void main(String[] args) throws InterruptedException {
            //Thread.sleep(5000); //-XX:BiasedLockingStartupDelay=0
            a = new AA();
            System.out.println("before:");
            System.out.println(ClassLayout.parseInstance(a).toPrintable());

            long begin = System.currentTimeMillis();
            Thread t1 = new Thread(()->{
                for (int i = 0; i < 100000000/2; i++) {
                    method();
                    if(i == 1000000){
                        System.out.println("t1:" + i);
                        System.out.println(ClassLayout.parseInstance(a).toPrintable());
                    }
                }
            },"t1");
            Thread t2 = new Thread(()->{
                for (int i = 0; i < 100000000/2; i++) {
                    method();
                    if(i == 1000000){
                        System.out.println("t1:" + i);
                        System.out.println(ClassLayout.parseInstance(a).toPrintable());
                    }
                }
                System.out.println(a.getAa());
                System.out.println("t2ing:");
                System.out.println(ClassLayout.parseInstance(a).toPrintable());
            },"t2");
            t1.start();
            t2.start();
            //t1 t2 执行完之后在执行主线程
            t1.join();
            t2.join();
            // 5537ms
            long end = System.currentTimeMillis();
            System.out.println((end - begin) + "ms");
            System.out.println(a.getAa());

        }
        static void method(){
            synchronized (a){
                aa ++;
            }
        }
    }

}

