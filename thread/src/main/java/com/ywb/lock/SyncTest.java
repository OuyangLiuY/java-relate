package com.ywb.lock;

import org.openjdk.jol.info.ClassLayout;

public class SyncTest {
    static int[] a = new int[10];
    /**
     * sync 上锁就是改变对象的对象头
     * 64 byte JVM
     * 对象内存中分三块区域：
     * 1.对象头    ---大小固定 ->96bit
     *         1.Mark Word ->64bit  --> unused:25,hashcode:31,unused:1,age:4,biased_lock:1,lock:2.
     *         2.Class MeteData Address ->32bit
     * 2.实例数据   ---大小不固定
     * 3.填充数据
     * 使用sync时对象的状态：
     *  1.无状态
     *  2.偏向锁
     *  3.轻量锁
     *  4.重量锁
     *  5.gc标记
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(L.class).toPrintable());
        //System.out.println(ClassLayout.parseInstance(a.class).toPrintable());
    }
}
