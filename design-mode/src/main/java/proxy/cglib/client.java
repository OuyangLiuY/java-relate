package proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

public class client {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloImpl.class);
        enhancer.setCallback(new HelloMethodInterceptor());
        HelloImpl hello = (HelloImpl) enhancer.create();
        hello.hello("这是cglib的测试");
    }
}
