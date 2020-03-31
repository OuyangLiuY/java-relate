package com.ywb.synchronize;

public class TestSynchronized {
    // 特殊的instance变量
    private final byte[] lock = new byte[0];
    private final String aa = new String("11");

    public void method1()
    {
        synchronized(lock) {
        }

        synchronized(aa) {

        }
        /**
         * synchronized(this)
         * 因为this是当前对象本身，所以锁定只对你自己new了并调用那个对象有用，所以另外一个人如果要new并调用，
         * 则和这个不是同一个锁，因为this变了。
         */
        synchronized(this) {
        }
        /**
         * synchronized(类的名.class)
         * 每个类在jvm里面只有一个唯一的字节码，所以.class是唯一的，无论多少对象，共用此同一把锁
         */
        synchronized(TestSynchronized.class) {
        }
    }
    private final char bb = Character.MAX_VALUE  ;
    private final byte dd = Byte.MAX_VALUE ;

    public void method2(Object obj)
    {
        synchronized(this) {
        }
        synchronized(obj) {


        }
    }
    public void method3()
    {

    }
}
