package jvm;

public class Parent {
    {
        System.out.println("父类普通代码块");
    }
    static {
        System.out.println("父类static代码块");
    }
    public Parent() {
        System.out.println("父类构造器");
    }
}
