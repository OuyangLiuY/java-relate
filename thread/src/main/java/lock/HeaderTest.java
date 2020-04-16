package lock;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

public class HeaderTest {
    volatile static boolean flag = true;
   static A a;
    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        a = new A();
        System.out.println("before:" );
        System.out.println(ClassLayout.parseInstance(a).toPrintable());

        final HeaderTest header = new HeaderTest();

        Thread t1 = new Thread(header::method, "t1");
        Thread t2 = new Thread(header::method, "t2");
        t1.start();
        //t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = false;

        System.out.println("after:" );
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        System.gc();
        System.out.println("gc:" );
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

    public void method() {
        synchronized (a) {
            System.out.println("locking:" + Thread.currentThread().getName());
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
        }
    }
    //09 9d e1 40 || 00001001 10011101 11100001 01000000
    //15 00 00 00 || 00010101 00000000 00000000 00000000


}
