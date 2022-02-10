package com.ouyangliuy.test;

import java.util.ArrayList;
import java.util.List;

public class MyTest {


    public static void main(String[] args) {
        String[] zsl = {"1","2","3","4"};
        int[] rank = new int[32];
        int i;
        for ( i = zsl.length-1; i >= 0; i--) {
            /* store rank that is crossed to reach the insert position */
            rank[i] = i == zsl.length-1 ? 0 : rank[i+1];
            rank[i] += 1;
            System.out.println(i);
            System.out.println(rank[i]);
        }
    }

    public static void test(){

      /*  for (int i = 0; i < 32; i++) {
            System.out.print(i +" "+  (0 | (i << 3)));
            System.out.print("  ");
            System.out.print(Integer.toBinaryString(0 | (i << 3)));
            System.out.println();
        }*/

        char aa = 'a';
        byte[] a = {127,100};
        System.out.println(a[0]);


    }
}
