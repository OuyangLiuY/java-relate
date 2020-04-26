package jvm;

public class DealLoopTest {
    static {
        System.out.println("DealLoopTest...");
    }

    static class DealLoopClass {
        static {
            if (true) {
                System.out.println(Thread.currentThread()
                        + "init DeadLoopClass");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*while (true) {      // 模拟耗时很长的操作
                }*/
            }
        }
    }

    public static void main(String[] args) {
        new Thread(()->{
            System.out.println(Thread.currentThread() + " start");
            DealLoopClass dlc = new DealLoopClass();
            System.out.println(Thread.currentThread() + " run over");
        },"t1").start();

        new Thread(()->{
            System.out.println(Thread.currentThread() + " start");
            DealLoopClass dlc = new DealLoopClass();
            System.out.println(Thread.currentThread() + " run over");
        },"t2").start();
    }
}
