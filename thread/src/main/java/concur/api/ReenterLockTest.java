package concur.api;

import java.util.concurrent.locks.ReentrantLock;


/**
 * reentrantlock用于替代synchronized
 * 使用reentrantlock可以完成同样的功能
 * reentrantlock必须要手动释放锁
 * 使用syn锁定的话如果遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，
 * 因此经常在finally中进行锁的释放
 */
/**
 * 使用reentrantlock可以进行“尝试锁定”tryLock，这样无法锁定，或者在指定时间内无法锁定，
 * 线程可以决定是否继续等待
 * 可以使用tryLock进行尝试锁定，不管锁定与否，方法都将继续执行
 * 可以根据tryLock的返回值来判定是否锁定
 * 也可以指定tryLock的时间，由于tryLock(time)抛出异常，所以要注意unclock的处理，必须放到finally中
 */
public class ReenterLockTest extends Thread{
	//ReentrantLock可以指定是否为公平锁，true为公平，默认为false
	private static ReentrantLock lock = new ReentrantLock(true);

	@Override
	public void run() {
		for (int i = 0; i < 10 ; i++) {
			lock.lock();
			boolean locked = false;
			try {
				System.out.println(Thread.currentThread().getName() + "获得锁");
				//使用lockInterruptibly来锁定可以对Interrupt方法作出响应
				lock.lockInterruptibly();
				locked = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		ReenterLockTest reenterLockTest = new ReenterLockTest();
		Thread t1 = new Thread(reenterLockTest);
		Thread t2 = new Thread(reenterLockTest);
		t1.setName("t1");

		//t2.start();
		t1.start();
		t2.setName("t2");
		//t2.interrupt();

		System.out.println(null != null);
	}

}
