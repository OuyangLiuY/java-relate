package org.example.proxy.jdk.impl;

import org.example.proxy.jdk.proxy.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public void query() {
        System.out.println("UserServiceImpl start log !");
    }
}
