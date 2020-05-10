package jdk;

import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserInvocation implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("invoke start!");
        Select annotation = method.getAnnotation(Select.class);
        String sql  = annotation.value()[0];

        System.out.println(sql);
        return null;
    }
}
