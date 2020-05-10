package dynamic.impl;

import dynamic.UserProxy;

public class UserProxyImpl implements UserProxy {
    @Override
    public void test(String ss) {
        System.out.println("UserProxyImpl : start !");
    }
}
