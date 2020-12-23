package jdk;

import java.lang.reflect.Proxy;

public class JDKProxyTest {

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        UserMapper instance = (UserMapper) ProxyInstance.getProxyInstance(UserMapper.class);
        instance.query();
    }
}
