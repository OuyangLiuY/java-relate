package jdk.helloproxy;

public class HelloImpl implements Hello {

    @Override
    public void sayHello(String hello) {
        System.out.println("Hello Impl :");
        System.out.println(hello);
    }
}
