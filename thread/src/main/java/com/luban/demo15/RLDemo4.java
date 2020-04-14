package com.luban.demo15;

import java.util.concurrent.locks.ReentrantLock;

public class RLDemo4 extends Thread{
	//ReentrantLock可以指定是否为公平锁，true为公平，默认为false
	private static ReentrantLock lock = new ReentrantLock(true);

	@Override
	public void run() {
		for (int i = 0; i < 100 ; i++) {
			lock.lock();
			try {
				System.out.println(Thread.currentThread().getName() + "获得锁");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		RLDemo4 rlDemo4 = new RLDemo4();
		Thread t1 = new Thread(rlDemo4);
		Thread t2 = new Thread(rlDemo4);
		t1.start();
		t2.start();
	}

}
