package classloader;

public class HelloWord {
    static {
        System.out.println("静态块");
    }
    public HelloWord() {
        System.out.println("构造函数");
    }
}
