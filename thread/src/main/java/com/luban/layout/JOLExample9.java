package com.luban.layout;
import org.openjdk.jol.info.ClassLayout;

import static java.lang.System.out;

public class JOLExample9 {
   static A a;
    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        a= new A();
        out.println("befor lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());

        Thread thread = new Thread(){
            @Override
            public void run() {

                synchronized (a){
                    out.println("t lock ing");
                    out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
        };
        thread.start();
        Thread.sleep(10);
        synchronized (a){
            out.println("main lock ing");
            out.println(ClassLayout.parseInstance(a).toPrintable());
        }
        Thread thread2 = new Thread(){
            @Override
            public void run() {

                synchronized (a){
                    out.println("t2 lock ing");
                    out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
        };
        thread2.start();
        Thread.sleep(10);
        synchronized (a){
            out.println("main lock ing");
            out.println(ClassLayout.parseInstance(a).toPrintable());
        }
        out.println("after lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
