package com;


//import sun.misc.Contended;

public class A {

   // @Contended
   // @Contended
    static long aa = 110L;

    public static void main(String[] args) {
        System.out.println(aa);
    }
}
