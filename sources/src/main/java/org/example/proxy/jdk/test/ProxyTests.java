package org.example.proxy.jdk.test;

import org.example.proxy.jdk.impl.UserPower;
import org.example.proxy.jdk.impl.UserServiceImpl;
import org.example.proxy.jdk.impl.UserTx;
import org.example.proxy.jdk.proxy.UserService;

public class ProxyTests {

    public static void main(String[] args) {

        UserService target = new UserPower(new UserServiceImpl());
        UserService proxy = new UserTx(target);
        proxy.query();
    }
}
