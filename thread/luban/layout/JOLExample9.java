package com.luban.layout;
import org.openjdk.jol.info.ClassLayout;

import static java.lang.System.out;

public class JOLExample9 {
   static A a;
    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        a= new A();
        out.println("befor concur.lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());

        Thread thread = new Thread(){
            @Override
            public void run() {

                synchronized (a){
                    out.println("t concur.lock ing");
                    out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
        };
        thread.start();
        Thread.sleep(10);
        synchronized (a){
            out.println("main concur.lock ing");
            out.println(ClassLayout.parseInstance(a).toPrintable());
        }
        Thread thread2 = new Thread(){
            @Override
            public void run() {

                synchronized (a){
                    out.println("t2 concur.lock ing");
                    out.println(ClassLayout.parseInstance(a).toPrintable());
                }
            }
        };
        thread2.start();
        Thread.sleep(10);
        synchronized (a){
            out.println("main concur.lock ing");
            out.println(ClassLayout.parseInstance(a).toPrintable());
        }
        out.println("after concur.lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
