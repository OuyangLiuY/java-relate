package com.ouyangliuy.string;

public class Test {

    public static void main(String[] args) {

        System.out.println(Integer.toBinaryString(-8192));
        System.out.println(Integer.toBinaryString(~(8192-1)));

        System.out.println(Integer.toBinaryString(- 8));

        int anInt = Integer.parseInt("-1111111111111111110000000000000", 2);
        System.out.println(anInt);
        System.out.println(Integer.toBinaryString(anInt));
        System.out.println(512 / 16);


        System.out.println(Long.parseLong("10000000000000000000000000000000000000000000000000000000000000000000",2));
    }
}
