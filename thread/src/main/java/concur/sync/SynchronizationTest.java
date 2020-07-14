package concur.sync;

import concur.lock.A;
import org.openjdk.jol.info.ClassLayout;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个线程创建了100个偏向对象。
 * 批量重偏向： 当另外一个对象t2 再对该对象加锁当累计20此的时候，就将后续的对象都重偏向于t2线程。
 * 批量撤销： 此时当另外一个线程t3使用后续对象加锁，此对象累计到40时，此时就将剩余的所以对象都撤销为轻量级锁。
 */
public class SynchronizationTest {

    static A a;
    static ArrayList<A> as = new ArrayList<>();

    //-XX:BiasedLockingStartupDelay=0
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(6000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                a = new A();
                synchronized (a) {
                    as.add(a);
                    if (i == 10) {
                        System.out.println("获取了第10个对象：");
                        System.out.println("\t期望值是偏量锁："+ClassLayout.parseInstance(a).toPrintable());

                    }
                }
            }
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        //t1.join();
        Thread.sleep(5000);
        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 100; i++) {
                a = as.get(i);
                if(i==20){
                    a = as.get(9);
                }
                synchronized (a) {
                    if(i ==10){
                        System.out.println("获取了第2个对象，期望值是无锁，当转化成轻量级锁之后就会释放这个锁变为无锁：" + ClassLayout.parseInstance(as.get(1)).toPrintable());
                        System.out.println("获取了第10个对象，期望值是轻量级锁："+ClassLayout.parseInstance(a).toPrintable());
                    }
                    if(i==19){
                        System.out.println("获取了第10个对象，期望值是无锁，当转化成轻量级锁之后就会释放这个锁变为无锁：" + ClassLayout.parseInstance(as.get(9)).toPrintable());
                        System.out.println("获取了第20个对象，期望值是偏向锁，因为发生了重偏向："+ClassLayout.parseInstance(a).toPrintable());
                        System.out.println("获取了第40个对象，期望值是偏向锁，因为这里的后续对象仍然偏向于线程t1："+ClassLayout.parseInstance(as.get(40)).toPrintable());
                    }
                   /* if (i == 20 || i ==16 || i==18 || i==39 ||i ==41 ||i ==40  ||i ==60) {
                        System.out.println(ClassLayout.parseInstance(a).toPrintable()+" ---t2 ing \t\t" + i);
                    }*/
                    if(i==20){
                        System.out.println("获取了第9个对象，期望值是轻量级锁，因为这里获取第9个对象之后使用sync关键字加锁，此时是轻量级锁，因为已经偏向后的对象不能重新偏向新的线程"+ClassLayout.parseInstance(a).toPrintable());
                    }
                   /* if(i==39){
                        System.out.println("获取了第40个对象，期望值是轻量级锁，因为这里的后续对象都将是轻量级锁："+ClassLayout.parseInstance(a).toPrintable());
                    }
                    if(i==80){
                        System.out.println("获取了第40个对象，期望值是轻量级锁，："+ClassLayout.parseInstance(a).toPrintable());
                    }*/
                }
            }
        }, "t2");
        t2.start();
        //t2.join();

       /* Thread.sleep(1000);
        Thread tmp  =  new Thread(()->{
            for (int i = 0; i < 15; i++) {
                a = as.get(i);
                synchronized (a) {
                    System.out.println("tmp");
                    if (i > 0) {
                        System.out.println("tmp ing");
                        System.out.println(ClassLayout.parseInstance(a).toPrintable() + "\n --- ");
                    }
                }
            }

        },"tmp");
        tmp.start();
        tmp.join();*/
    }
     static class LockTest{
         public static void main(String[] args) throws Exception {
             //睡眠超过偏向锁延迟，以使用偏向锁
             Thread.sleep(6000);

             List<A> list = new ArrayList<>();

             //初始化数据
             for (int i = 0; i < 100; i++) {
                 list.add(new A());

             }

             Thread t1 = new Thread() {
                 String name = "1";

                 public void run() {
                     out.printf(name);
                     for (A a : list) {
                         synchronized (a) {
                             if (a == list.get(10))
                                 out.println("t1 预期是偏向锁"+10 + ClassLayout.parseInstance(a).toPrintable());
                         }
                     }
                     try {
                         Thread.sleep(100000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
             };
             t1.start();
             Thread.sleep(5000);
             out.println("main 预期是偏向锁"+10 + ClassLayout.parseInstance(list.get(10)).toPrintable());

             Thread t2 = new Thread() {
                 String name = "2";

                 public void run() {
                     out.printf(name);

                     for(int i = 0;i<100;i++){
                         A a = list.get(i);
                         // hack 为了在批量重偏向发生后再次加锁前面使用了轻量级锁的对象
                         if(i==20){
                             a= list.get(9);
                         }

                         synchronized (a) {
                             if ( i==10) {
                                 //已经经过偏向锁撤销，并使用轻量级锁的对象，释放后  状态依为001 无锁状态
                                 out.println("t2 i=10 get(1)预期是无锁" +  ClassLayout.parseInstance(list.get(1)).toPrintable());
                                 //因为和t1交替使用对象a 没有发生竞争，但偏向锁已偏向，另外不满足重偏向条件，所以使用轻量级锁
                                 out.println("t2 i=10 get(i) 预期轻量级锁 " + i + ClassLayout.parseInstance(a).toPrintable());
                             }
                             if ( i== 19) {
                                 //已经经过偏向锁撤销，并使用轻量级锁的对象，在批量重偏向发生后。不会影响现有的状态  状态依然为001
                                 out.println("t2  i=19  get(10)预期是无锁" + 10 + ClassLayout.parseInstance(list.get(10)).toPrintable());
                                 //满足重偏向条件后，已偏向的对象可以重新使用偏向锁 将线程id指向当前线程，101
                                 out.println("t2  i=19  get(i) 满足重偏向条件20 预期偏向锁 " + i + ClassLayout.parseInstance(a).toPrintable());
                                 //满足重偏向条件后，已偏向还为需要加锁的对象依然偏向线程1 因为偏向锁的撤销是发生在下次加锁的时候。这里没有执行到同步此对象，所以依然偏向t1
                                 out.println("t2  i=19  get(i) 满足重偏向条件20 但后面的对象没有被加锁，所以依旧偏向t1 " + i + ClassLayout.parseInstance(list.get(40)).toPrintable());
                             }
                             if (i == 20) {
                                 //满足重偏向条件后，再次加锁之前使用了轻量级锁的对象，依然轻量级锁，证明重偏向这个状态只针对偏向锁。已经发生锁升级的，不会退回到偏向锁
                                 out.println("t2  i=20 满足偏向条件之后，之前被设置为无锁状态的对象，不可偏向，这里使用的是轻量级锁  get(9)预期是轻量级锁 "  + ClassLayout.parseInstance(a).toPrintable());
                             }
                         }
                     }
                     try {
                         Thread.sleep(100000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
             };
             t2.start();
             Thread.sleep(5000);

         }
    }

    static class Test2{
        static A a;

        public static void main(String[] args) throws Exception {
            Thread.sleep(6000);
//        a = new A();
            List<A> list = new ArrayList<>();
            List<A> list2 = new ArrayList<>();
            List<A> list3 = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                list.add(new A());
                list2.add(new A());
                list3.add(new A());
            }
            out.println("初始状态" + 10 + ClassLayout.parseClass(A.class).toPrintable());//偏向锁

            Thread t1 = new Thread() {
                String name = "1";

                public void run() {
                    out.printf(name);
                    for (A a : list) {
                        synchronized (a) {
                            if (a == list.get(10))
                                out.println("t1 预期是偏向锁" + 10 + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                        }
                    }
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            t1.start();
            Thread.sleep(5000);
            out.println("main 预期是偏向锁" + 10 + ClassLayout.parseInstance(list.get(10)).toPrintable());//偏向锁

            Thread t2 = new Thread() {
                String name = "2";

                public void run() {
                    out.printf(name);

                    for (int i = 0; i < 100; i++) {
                        A a = list.get(i);
                        synchronized (a) {
                            if (a == list.get(10)) {
                                out.println("t2 i=10 get(1)预期是无锁" + ClassLayout.parseInstance(list.get(1)).toPrintable());//偏向锁
                                out.println("t2 i=10 get(10) 预期轻量级锁 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                            }
                            if (a == list.get(19)) {
                                out.println("t2  i=19  get(10)预期是无锁" + 10 + ClassLayout.parseInstance(list.get(10)).toPrintable());//偏向锁
                                out.println("t2  i=19  get(19) 满足重偏向条件20 预期偏向锁 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                                out.println("A类的对象累计撤销达到20");
                            }

                        }
                    }

                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            };
            t2.start();

            Thread.sleep(5000);


            Thread t3 = new Thread() {
                String name = "3";

                public void run() {

                    out.printf(name);
                    for (A a : list2) {
                        synchronized (a) {
                            if (a == list2.get(10))
                                out.println("t3 预期是偏向锁" + 10 + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                        }
                    }
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t3.start();
            Thread.sleep(5000);


            Thread t4 = new Thread() {
                String name = "4";

                public void run() {

                    out.printf(name);

                    for (int i = 0; i < 100; i++) {
                        A a = list2.get(i);
                        synchronized (a) {
                            if (a == list2.get(10)) {
                                out.println("t4 i=10 get(1)预期是无锁" + ClassLayout.parseInstance(list2.get(1)).toPrintable());//偏向锁
                                out.println("t4 i=10 get(10) 当前不满足重偏向条件 20 预期轻量级锁 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                            }
                            if (a == list2.get(19)) {
                                out.println("t4  i=19  get(10)预期是无锁" + 10 + ClassLayout.parseInstance(list2.get(10)).toPrintable());//偏向锁
                                out.println("t4 i=19 get(19) 当前满足重偏向条件 20 但A类的对象累计撤销达到40 预期轻量级锁 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                                out.println("A类的对象累计撤销达到40");
                            }
                            if (a == list2.get(20)) {
                                out.println("t4 i=20 get(20) 当前满足重偏向条件 20 预期轻量级锁 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁

                            }
                        }
                    }

                }
            };
            t4.start();
            Thread.sleep(5000);


            out.println("main 预期是偏向锁" + 10 + ClassLayout.parseInstance(list3.get(0)).toPrintable());//偏向锁

            Thread t5 = new Thread() {
                String name = "5";

                public void run() {
                    out.printf(name);
                    for (A a : list3) {
                        synchronized (a) {
                            if (a == list3.get(10))
                                out.println("t5 预期是轻量级锁，A类的对象累计撤销达到40 不可以用偏向锁了" + 10 + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                        }
                    }
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            t5.start();
            Thread.sleep(5000);
            out.println("main 预期是偏向锁" + 10 + ClassLayout.parseInstance(list.get(10)).toPrintable());//偏向锁

            Thread t6 = new Thread() {
                String name = "6";

                public void run() {
                    out.printf(name);

                    for (int i = 0; i < 100; i++) {
                        A a = list3.get(i);
                        synchronized (a) {
                            if (a == list3.get(10)) {
                                out.println("t6 i=10 get(1)预期是无锁" + ClassLayout.parseInstance(list3.get(1)).toPrintable());//偏向锁
                                out.println("t6 i=10 get(10) 预期轻量级锁 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁
                            }
                            if (a == list3.get(19)) {
                                out.println("t6  i=19  get(10)预期是无锁" + 10 + ClassLayout.parseInstance(list3.get(10)).toPrintable());//偏向锁
                                out.println("t6  i=19  get(19) 满足重偏向条件20 但A类的对象累计撤销达到40 不可以用偏向锁了 " + i + ClassLayout.parseInstance(a).toPrintable());//偏向锁

                            }

                        }
                    }

                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            };
            t6.start();

            Thread.sleep(5000);


            out.println("由于A撤销锁次数达到默认的 BiasedLockingBulkRevokeThreshold=40 这里实例化的对象 是无锁状态" + ClassLayout.parseInstance(new A()).toPrintable());//偏向锁
            out.println("由于B撤销锁次数没达到默认的 BiasedLockingBulkRevokeThreshold=40 这里实例化的对象 是偏向锁可以偏向状态 理论上可以再次验证上面A类的相关操作" + ClassLayout.parseInstance(new A()).toPrintable());//偏向锁
            out.println("撤销偏向后状态" + 10 + ClassLayout.parseClass(A.class).toPrintable());//偏向锁

        }
    }
}
