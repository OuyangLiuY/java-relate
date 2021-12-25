package com.ouyangliuy.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * 弱引用得一个简单得测试
 */
public class WeakSoftReferenceTest {
    private static final ReferenceQueue<VeryBig> queue = new ReferenceQueue<>();

    public static void check() {
        Reference<? extends VeryBig> re = null;
        while ((re = queue.poll()) != null) {
            System.out.println("In queue: " + ((VeryBigWeakReference) (re)).id);
        }
    }

    public static void main(String[] args) {

        LinkedList<WeakReference<VeryBig>> weakList = new LinkedList<>();
        int size = 5;
        for (int i = 0; i < size; i++) {
            weakList.add(new VeryBigWeakReference(new VeryBig("weak" + i), queue));
            System.out.println("Just created weak: " + weakList.getLast());
        }
        System.gc();
        try { // 下面休息几分钟，让上面的垃圾回收线程运行完成
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        check();
    }


}

class VeryBig {
    public String id;
    byte[] bytes = new byte[2 * 1024];

    public VeryBig(String id) {
        this.id = id;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Finalizing VeryBig " + id);
    }
}

class VeryBigWeakReference extends WeakReference<VeryBig> {

    public String id;

    public VeryBigWeakReference(VeryBig big, ReferenceQueue<? super VeryBig> q) {
        super(big, q);
        this.id = big.id;
    }

    protected void finalize() {
        System.out.println("Finalizing VeryBigWeakReference " + id);
    }
}


