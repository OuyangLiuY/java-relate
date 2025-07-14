package org.example.proxy.dynamic.impl;

import org.example.proxy.dynamic.UserProxy;

public class UserProxyImpl implements UserProxy {
    @Override
    public void test(String ss) {
        System.out.println("UserProxyImpl : start !");
    }
}
