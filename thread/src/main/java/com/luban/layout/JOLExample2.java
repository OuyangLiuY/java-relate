package com.luban.layout;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import static java.lang.System.out;

public class JOLExample2 {
    public static void main(String[] args) throws Exception {
        A a= new A();
        out.println("befor hash");
        //没有计算HASHCODE之前的对象头
        out.println(ClassLayout.parseInstance(a).toPrintable());
        //JVM 计算的hashcode
        out.println("jvm------------0x"+Integer.toHexString(a.hashCode()));
        HashUtil.countHash(a);
        //当计算完hashcode之后，我们可以查看对象头的信息变化
        out.println("after hash");
        out.println(ClassLayout.parseInstance(a).toPrintable());

    }
}
