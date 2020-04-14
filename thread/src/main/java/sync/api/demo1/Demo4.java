package sync.api.demo1;

public class Demo4 {

    private static int count = 10;

    //synchronize关键字修饰静态方法锁定的是类的.class文件
    //静态方法中synchronize锁定代码块，锁定的对象不能是类的实例，只能是类的.class文件。
    public synchronized static void test(){
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public static void test2(){
        synchronized (Demo4.class){//这里不能替换成this
            count--;
        }
    }

}
