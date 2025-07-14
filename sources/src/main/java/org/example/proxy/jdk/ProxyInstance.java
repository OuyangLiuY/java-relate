package org.example.proxy.jdk;

import java.lang.reflect.Proxy;

public class ProxyInstance {


    public static Object getProxyInstance( Class cclass){

        Class[] classes = new Class[]{cclass};

        /**
         * (ClassLoader loader, 类加载器
         *  Class<?>[] interfaces, 需要代理对象的接口数组
         *  InvocationHandler h 执行器
         */
        Object instance = Proxy.newProxyInstance(ProxyInstance.class.getClassLoader(), classes, new UserInvocation());

        return instance;
    }
}
