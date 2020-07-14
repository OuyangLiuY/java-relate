package com.luban.layout;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import static java.lang.System.out;

public class JOLExample1 {
    static  A a = new A();
    public static void main(String[] args) {
        //jvm的信息
        out.println(VM.current().details());
        out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
