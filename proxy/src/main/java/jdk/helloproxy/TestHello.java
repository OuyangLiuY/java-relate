package jdk.helloproxy;

import java.lang.reflect.Proxy;

public class TestHello {


    /**
     * 类加载器(ClassLoader)用来加载动态代理类
     * 一个要实现接口的数组，从这点就可以看出，要想使用JDK动态代理，必须要有接口类
     * InvocactionHandler接口的一个实现
     * @param args
     */
    public static void main(String[] args) {

        HelloImpl hello = new HelloImpl();
        HelloInvocationHandler handler = new HelloInvocationHandler(hello);

        Hello instance = (Hello) Proxy.newProxyInstance(HelloImpl.class.getClassLoader(), hello.getClass().getInterfaces(), handler);

        instance.sayHello("main hello");
    }
}
