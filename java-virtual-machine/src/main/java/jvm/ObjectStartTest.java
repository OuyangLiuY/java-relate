package jvm;

public class ObjectStartTest extends Parent {
    static ObjectStartTest startTest = new ObjectStartTest();
    public ObjectStartTest() {
        System.out.println("子类构造器");
    }
    static {
        i = 10;
        System.out.println("子类static代码块");
    }
    {
        i = 20;
        System.out.println(i);
        System.out.println("子类普通代码块");
    }
    static void methodStatic() {
        System.out.println(i);
        System.out.println("子类静态方法");
    }
    void method() {
        System.out.println("子类普通方法");
    }
    static int i = 0;
    public static void main(String[] args) {
    /*    //启动（Bootstrap）类加载器
        System.out.println(System.getProperty("sun.boot.class.path"));
        //扩展（Extension）类加载器
        System.out.println(System.getProperty("java.ext.dirs"));
        //系统（System）类加载器
        System.out.println(System.getProperty("java.class.path"));
        //线程上下文类加载器*/
        System.out.println(i);
        System.out.println("main 线程代码块");
        methodStatic();
        startTest.method();
        System.out.println("================");
        new ObjectStartTest().method();
    }
}
