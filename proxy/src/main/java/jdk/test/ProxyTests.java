package jdk.test;

import jdk.impl.UserPower;
import jdk.impl.UserServiceImpl;
import jdk.impl.UserTx;
import jdk.proxy.UserService;

public class ProxyTests {

    public static void main(String[] args) {

        UserService target = new UserPower(new UserServiceImpl());
        UserService proxy = new UserTx(target);
        proxy.query();
    }
}
