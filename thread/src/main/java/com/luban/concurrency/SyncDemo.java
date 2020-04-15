package com.luban.concurrency;

public class SyncDemo {
  public static void main(String[] args) {
    Thread thread = new Thread(){
      @Override
      public void run() {
        while (!Thread.currentThread().interrupted()){
          try {
            Thread.sleep(10000);
          } catch (InterruptedException e) {
            interrupt();
            System.out.println(Thread.currentThread().interrupted());
            e.printStackTrace();
          }

        }
      }
    };

    thread.start();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    thread.interrupt();
  }

}
