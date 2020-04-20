package concur.api;


public class SynchronizedTest {

    class TestOne {
        // 但写法一与写法二是等价的，都是锁定了整个方法时的内容。
        public synchronized void method1() {
            // todo
        }

        public void method2() {
            synchronized (this) {
                // todo
            }
        }

        /**
         * synchronized关键字不能继承。
         * 虽然可以使用synchronized来定义方法，但synchronized并不属于方法定义的一部分，
         * 因此，synchronized关键字不能被继承。如果在父类中的某个方法使用了synchronized关键字，
         * 而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，
         * 而必须显式地在子类的这个方法中加上synchronized关键字才可以。
         * 当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，
         * 但子类调用了父类的同步方法，因此，子类的方法也就相当于同步了。
         */

        class Parent {
            public synchronized void method() {
            }
        }

        class Child extends Parent {
            public void method() {
                super.method();
            }
        }
        /**
         * 1.在定义接口方法时不能使用synchronized关键字。
         * 2.构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。
         */
    }

    /**
     * 静态方法是属于类的而不属于对象的。
     * 同样的，synchronized修饰的静态方法锁定的是这个类的所有对象。
     */

    static class SyncStatic implements Runnable {
        private static int count;

        public SyncStatic() {
            count = 0;
        }

        @Override
        public void run() {
            method();
        }

        public synchronized static void method() {
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + (count++));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) {
            SyncStatic syncThread1 = new SyncStatic();
            SyncStatic syncThread2 = new SyncStatic();
            Thread thread1 = new Thread(syncThread1, "SyncThread1");
            Thread thread2 = new Thread(syncThread2, "SyncThread2");
            thread1.start();
            thread2.start();
        }


    }
    static class Test{
        public static void main(String[] args) {
           /* private static final int RUNNING    = -1 << COUNT_BITS;
            private static final int SHUTDOWN   =  0 << COUNT_BITS;
            private static final int STOP       =  1 << COUNT_BITS;
            private static final int TIDYING    =  2 << COUNT_BITS;
            private static final int TERMINATED =  3 << COUNT_BITS;*/
            int COUNT_BITS = Integer.SIZE - 3;
            System.out.println((1<<COUNT_BITS) -1);
            System.out.println(0<<COUNT_BITS);
            System.out.println(-1<<COUNT_BITS);
            System.out.println(1<<COUNT_BITS);
            System.out.println(2<<COUNT_BITS);
            System.out.println(3<<COUNT_BITS);
        }
    }
}
