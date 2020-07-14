package com;

import concur.sync.HashUtil;
import org.openjdk.jol.info.ClassLayout;

public class Test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        A a = new A();
       /* System.out.println(ClassLayout.parseInstance(a).toPrintable());
        System.out.println(a.hashCode());
        String hash = HashUtil.countHash(a);
        System.out.println(hash);
        System.out.println(ClassLayout.parseInstance(a).toPrintable());*/
        String aa = "casda";
        Integer cc = 9;

        byte[] bytes = aa.getBytes();
        for (byte aByte : bytes) {
            System.out.println(Integer.toBinaryString(aByte));
            System.out.println(aByte);
        }
        System.out.println(bytes.length);

        System.out.println(Integer.bitCount(128));
        System.out.println(Integer.bitCount(cc));
        System.out.println(Integer.toBinaryString(cc));
        System.out.println(Integer.toBinaryString(129));
        //
        System.out.println(cc.byteValue());

        //二进制
        System.out.println( 0b1111111111000000000000000000010);
        //八进制，以0开始
        System.out.println( 077651);
        //十机制
        System.out.println( 11);
        //十六进制
        System.out.println( 0x11c);
        System.out.println(Long.toBinaryString(Long.MAX_VALUE));
        System.out.println(Long.toBinaryString(Long.MAX_VALUE).length());
       // System.out.println(ClassLayout.parseInstance(cc).toPrintable());
    }
}
