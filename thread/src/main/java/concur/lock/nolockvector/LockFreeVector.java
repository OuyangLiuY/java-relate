package concur.lock.nolockvector;


import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class LockFreeVector<E> {
    static boolean debug = false;
    static int index ;
    private static final int FIRST_BUCKET_SIZE = 8;
    private final int zeroNumFirst = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE);
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
    private final AtomicReference<Descriptor<E>> descriptor;
    private static final int N_BUCKET = 30;

    public LockFreeVector() {
        this.buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(N_BUCKET);
        buckets.set(0, new AtomicReferenceArray<E>(FIRST_BUCKET_SIZE));
        this.descriptor = new AtomicReference<Descriptor<E>>(new Descriptor<E>(0, null));
    }

    static class Descriptor<E> {
        private int size;
        volatile WriteDescriptor<E> writeOp;

        public Descriptor(int size, WriteDescriptor<E> writeOp) {
            this.size = size;
            this.writeOp = writeOp;
        }

        public void completeWrite() {
            WriteDescriptor<E> tmpOp = writeOp;
            if (tmpOp != null) {
                tmpOp.doIt();
                writeOp = null; //this is safe since all write to writeOp use
            }
        }
    }

    static class WriteDescriptor<E> {
        public E oldV;
        public E newV;
        private AtomicReferenceArray<E> addr;
        private int addr_ind;

        public WriteDescriptor(AtomicReferenceArray<E> addr, int addr_ind, E oldV, E newV) {
            this.oldV = oldV;
            this.newV = newV;
            this.addr = addr;
            this.addr_ind = addr_ind;
        }

        public void doIt() {
            addr.compareAndSet(addr_ind, oldV, newV);
        }
    }

    public void push_back(E e) {
        Descriptor<E> desc;
        Descriptor<E> newD;
        do {
            desc = descriptor.get();
            desc.completeWrite();
            int pos = desc.size + FIRST_BUCKET_SIZE;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            int bucketInd = zeroNumFirst - zeroNumPos;
            if (buckets.get(bucketInd) == null) {
                int newLen = 2 * buckets.get(bucketInd - 1).length();
                if (debug)
                    System.out.println(newLen);
                buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<E>(newLen));
            }
            int idx = (0x80000000 >>> zeroNumPos) ^ pos;
            newD = new Descriptor<E>(desc.size + 1, new WriteDescriptor<E>(buckets.get(bucketInd), idx, null, e));

        } while (!descriptor.compareAndSet(desc, newD));
        descriptor.get().completeWrite();
    }
    public int size() {
        return descriptor.get().size;
    }
    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketInd = zeroNumFirst - zeroNumPos;
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        return buckets.get(bucketInd).get(idx);
    }

    public static void main(String[] args) {
        List<String> arg = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String str : arg ) {
            if (str.startsWith("-Xrunjdwp") || str.startsWith("-agentlib:jdwp")) {
                debug = true;
                break;
            }
        }
        long begin = System.currentTimeMillis();

        LockFreeVector<String> vector = new LockFreeVector<String>();
        for (int i = 0; i < 10000; i++) {
            vector.push_back("1->" + i);
        }
        System.out.println(vector.size());
        for (int i = 0; i < vector.size(); i++) {
            System.out.print(vector.get(i) + "\t");
        }

        long end =  System.currentTimeMillis();
        System.out.println();
        System.out.println((end - begin));

        long listbegin = System.currentTimeMillis();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10000; i++) {
            list.add("1->" + i);
        }
        for (int i = 0; i <list.size() ; i++) {
            System.out.print(list.get(i) + "\t");
        }
        long listend =  System.currentTimeMillis();
        System.out.println();
        System.out.println(listend - listbegin);


        long vectorbegin = System.currentTimeMillis();
        Vector<String> stringVector = new Vector<String>();
        for (int i = 0; i < 10000; i++) {
            stringVector.add("1->" + i);
        }
        for (int i = 0; i <stringVector.size() ; i++) {
            System.out.print(stringVector.get(i) + "\t");
        }
        long vectorend =  System.currentTimeMillis();
        System.out.println();
        System.out.println(vectorend - vectorbegin);

           /*System.out.println(Integer.toBinaryString(65536));*/
    }
}
