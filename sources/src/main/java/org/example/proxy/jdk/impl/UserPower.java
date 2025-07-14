package org.example.proxy.jdk.impl;

import org.example.proxy.jdk.proxy.UserService;

public class UserPower implements UserService {

    UserService userService;
    public UserPower(UserService userService){
        this.userService = userService;
    }

    @Override
    public void query() {
        System.out.println("power start log ");
        userService.query();
    }
}
