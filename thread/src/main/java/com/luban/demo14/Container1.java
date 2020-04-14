package com.luban.demo14;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 面试题：写一个固定容量同步容器，拥有Put和get方法，以及getCount方法，
 * 能够支持两个生产者线程以及10个消费者线程的阻塞调用
 * wait notifyAll
 *
 * 这里探究为什么大多数情况下wait和while是一起使用的，
 * 因为这里是有两个生产者线程并且容器容量是固定的，生产方法加了锁。
 * 如果容器满了，这时候第一个生产者线程拿到了锁，他会判断有没有满，如果满了就等待，在等待
 * 的时候会释放掉锁资源，这时候第二个生产者线程就会拿到锁，然后他也会判断是否满，因为容器是
 * 满了，第二个生产者线程也会等待并且释放锁，当消费者消费之后唤醒所有线程，这时候两个生产者线程
 * 都醒来了，因为要竞争锁资源，比如第一个生产者线程拿到了锁，他给容器又加到十了，陷入等待状态，
 * 锁资源释放掉，第二个生产者线程这时候拿到锁资源，他会继续执行（从上次睡眠的地方继续）,如果是if
 * 的话，他在wait阻塞之前就已经执行了一次if，所以不会再执行，而是继续往下执行，那这时候就超过了
 * 容器的容量。所以为了让他再一次判断，这里使用while
 */
public class Container1<T>{

	private final LinkedList<T> lists = new LinkedList<>();
	private final int MAX = 10;
	private int count = 0;
	
	public synchronized void put(T t){
		while (lists.size() == MAX) {//
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		lists.add(t);
		++count;
		this.notifyAll();
	}
	
	public synchronized T get(){
		T t = null;
		while (lists.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		t = lists.removeFirst();
		count--;
		this.notifyAll();
		return t;
	}
	
	public static void main(String[] args) {
		Container1<String> c = new Container1<>();
		for (int i = 0; i < 100; i++) {
			new Thread(()->{
				for (int j = 0; j < 5; j++) {
					System.out.println(c.get());
				}
			}, "c" + i).start();
		}
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 2; i++) {
			new Thread(()->{
				for (int j = 0; j < 25; j++) {
					c.put(Thread.currentThread().getName() + "" + j);
				}
			}, "p" + i).start();
		}
	}

}
