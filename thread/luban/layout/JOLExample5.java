package com.luban.layout;
import org.openjdk.jol.info.ClassLayout;

import static java.lang.System.out;

public class JOLExample5 {
    static A a;
    public static void main(String[] args) throws Exception {
        a = new A();
        out.println("befre concur.lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());
        sync();
        out.println("after concur.lock");
        out.println(ClassLayout.parseInstance(a).toPrintable());
    }

    public  static  void sync() throws InterruptedException {
        synchronized (a){
            out.println("concur.lock ing");
            out.println(ClassLayout.parseInstance(a).toPrintable());
        }
    }
}
