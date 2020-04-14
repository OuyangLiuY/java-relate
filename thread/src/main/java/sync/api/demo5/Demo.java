package sync.api.demo5;

import java.util.concurrent.TimeUnit;

/**
 * 脏读问题
 * 实际业务当中应该看是否允许脏读，不允许的情况下对读方法也要加锁
 */
public class Demo {

    String name;
    double balance;

    public synchronized void set(String name,double balance){
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance = balance;
    }

    public /*synchronized*/ double getBalance(String name){
        return this.balance;
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        new Thread(()->demo.set("huaan",100.0)).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(demo.getBalance("huaan"));//

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(demo.getBalance("huaan"));
    }

}
