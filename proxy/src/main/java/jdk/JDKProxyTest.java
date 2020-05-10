package jdk;

import java.lang.reflect.Proxy;

public class JDKProxyTest {

    public static void main(String[] args) {
        UserMapper instance = (UserMapper) ProxyInstance.getProxyInstance(UserMapper.class);
        instance.query();
    }
}
