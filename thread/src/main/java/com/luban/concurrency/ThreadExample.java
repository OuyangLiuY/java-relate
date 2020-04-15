package com.luban.concurrency;

public class ThreadExample {
    static Thread thread;
    static int i = 0;

    public static void main(String[] args) throws InterruptedException {
        traditional();



        thread.start();
        Thread.sleep(500);
        i=19;
    }
        public static void traditional () {
            thread = new Thread() {
                public void run() {
                    while (true) {
                        assert i==9;
                        return;
                       // assert i==10;
//                        if (i == 9) {
//                            return;
//                        }
                    }
                }
            };
        }
}
