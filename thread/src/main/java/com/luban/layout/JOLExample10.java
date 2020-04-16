package com.luban.layout;
import org.openjdk.jol.info.ClassLayout;

import static java.lang.System.out;

public class JOLExample10 {
   static A a;
    public static void main(String[] args) throws Exception {
        a= new A();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                synchronized (a){
                    out.println("t1 lock ing");
                    out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
        };
        t1.start();
        t1.join();
       //Thread.sleep(6000);
        synchronized (a){//a b c c+++
            out.println("main lock ing");
            out.println(ClassLayout.parseInstance(a).toPrintable());
        }

//        Thread thread2 = new Thread(){
//            @Override
//            public void run() {
//
//                synchronized (a){
//                    out.println("t2 lock ing");
//                    out.println(ClassLayout.parseInstance(a).toPrintable());
//                }
//            }
//        };
//        thread2.start();
//        Thread.sleep(10);
//        synchronized (a){
//            out.println("main lock ing");
//            out.println(ClassLayout.parseInstance(a).toPrintable());
//        }
        out.println("after lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
