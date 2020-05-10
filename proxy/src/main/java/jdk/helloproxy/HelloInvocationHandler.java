package jdk.helloproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloInvocationHandler implements InvocationHandler {

    Object target;

    public HelloInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("invoke method !");
        System.out.println("method name : " + method.getName());
        Object object = method.invoke(target, args);
        return object;
    }
}
