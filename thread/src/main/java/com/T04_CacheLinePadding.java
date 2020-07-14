package com;

public class T04_CacheLinePadding {

   /* public static volatile long[] arr = new long[16];
    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(()->{
            for (long i = 0; i < 1_000_000_000L; i++) {
                arr[0] = i;
            }
        });

        Thread t2 = new Thread(()->{
            for (long i = 0; i < 1_000_000_000L; i++) {
                arr[8] = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start)/100_0000);
    }*/
   public static void main(String[] args) {

       //声明一个1000000行8列的long型的数组，则一行为一个cache line
       int row = 1000000;
       long[][] longArr = new long[row][8];
       for (int i = 0; i < row; i++) {
           for (int j = 0; j < 8; j++) {
               longArr[i][j] = 0L;
           }
       }

       //使用缓存行: 一行一行的读，会读一个cache line
       long marked = System.currentTimeMillis();
       for (int i = 0; i < row; i++) {
           for (int j = 0; j < 8; j++) {
               long sum = +longArr[i][j];
           }
       }
       System.out.println("loop times:" + (System.currentTimeMillis() - marked) + "ms");


       //不使用cache line：跳行读取
       marked = System.currentTimeMillis();
       for (int i = 0; i < 8; i++) {
           for (int j = 0; j < row; j++) {
               long sum = +longArr[j][i];
           }
       }
       System.out.println("loop times:" + (System.currentTimeMillis() - marked) + "ms");
   }

}
