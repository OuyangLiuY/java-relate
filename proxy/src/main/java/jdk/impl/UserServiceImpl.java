package jdk.impl;

import jdk.proxy.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public void query() {
        System.out.println("UserServiceImpl start log !");
    }
}
