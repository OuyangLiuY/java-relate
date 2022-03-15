package concur.aqs;

import sun.misc.Unsafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyReentrantLock {

    private static final Unsafe unsafe = Unsafe.getUnsafe();
    /*private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;*/
    static class Node{
        static final Node EXCLUSIVE = null;

    }


    public static void main(String[] args) {
        MyReentrantLock lock = new MyReentrantLock();
        System.out.println(lock.compareState(1,2));

    }

    boolean compareState(int expect, int update){
       return unsafe.compareAndSwapInt(this,1L,expect,update);
    }
}
