package com;

import concur.sync.HashUtil;
import org.openjdk.jol.info.ClassLayout;

public class Test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        A a = new A();
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        System.out.println(a.hashCode());
        String hash = HashUtil.countHash(a);
        System.out.println(hash);
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
