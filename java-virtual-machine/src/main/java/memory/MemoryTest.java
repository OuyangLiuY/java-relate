package memory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryTest {
   static AtomicInteger num = new AtomicInteger();
    static MemoryTest lock = new MemoryTest();
    public static void main(String[] args) throws InterruptedException {
        Set<Person> set = new HashSet<Person>();
        Person p1 = new Person("唐僧", "pwd1", 25);
        Person p2 = new Person("孙悟空", "pwd2", 26);
        Person p3 = new Person("猪八戒", "pwd3", 27);
        set.add(p1);
        set.add(p2);
        set.add(p3);
        System.out.println("总共有:" + set.size() + " 个元素!"); //结果：总共有:3 个元素!
        System.out.println(p3);
        p3.setAge(2); //修改p3的年龄,此时p3元素对应的hashcode值发生改变

        set.remove(p3); //此时remove不掉，造成内存泄漏

        set.add(p3); //重新添加，居然添加成功
        System.out.println("总共有:" + set.size() + " 个元素!"); //结果：总共有:4 个元素!
        for (Person person : set) {
            System.out.println(person);
        }



        //方法1
/*
        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i % 2 == 0) {
                    System.out.println(Thread.currentThread().getName() + " -" + i);
                }
            }
        }, "t1").start();
        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i % 2 != 0) {
                    System.out.println(Thread.currentThread().getName() + " -" + i);
                }
            }
        }, "t2").start();
*/
        //
        //ReentrantLock lock = new ReentrantLock();

     /*   new Thread(()->{
            while (num.get() < 10000){
                synchronized (MemoryTest.lock){
                    //偶数
                    if((num.get() & 1) == 0){
                        System.out.println(Thread.currentThread().getName() + " - " + num.getAndIncrement());
                    }
                }
            }
        }, "t1").start();
        new Thread(()->{
            // lock.lock();
            while (num.get() < 10000){
                synchronized (MemoryTest.lock){
                    //奇数
                    if((num.get() & 1) ==1){
                        System.out.println(Thread.currentThread().getName() + " - " + num.getAndIncrement());
                    }
                }
            }
        }, "t2").start();*/
    //交替打印奇数和偶数 ： 1 , 2
  /*      new Thread(()->{
            lock.methodPrint();
        }, "t3").start();
        Thread.sleep(10);
        new Thread(()->{
            lock.methodPrint();
        }, "t4").start();*/
        //线程1：1
        //线程2：2
        //线程3：3
        //线程1：1
        //线程2：2
        //线程3：3

        new Thread(()->{
            lock.methodPrint2();
        }, "t6").start();
        Thread.sleep(10);
        new Thread(()->{
            lock.methodPrint2();
        }, "t7").start();
        Thread.sleep(10);
        new Thread(()->{
            lock.methodPrint2();
        }, "t8").start();
    }
    void methodPrint()   {
        while (num.get() < 10000){
            synchronized (lock){
                //奇数
                System.out.println(Thread.currentThread().getName() + " - " + num.getAndIncrement());
                lock.notifyAll();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    int[] nums = {1,2,3};
    void methodPrint2()   {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock){
                //奇数
                if(Thread.currentThread().getName().equals("t6")){
                    System.out.println(Thread.currentThread().getName() + " - " + nums[0]);
                }
                if(Thread.currentThread().getName().equals("t7")){
                    System.out.println(Thread.currentThread().getName() + " - " + nums[1]);
                }
                if(Thread.currentThread().getName().equals("t8")){
                    System.out.println(Thread.currentThread().getName() + " - " + nums[2]);
                }
                lock.notifyAll();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
