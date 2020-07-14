package jdk.impl;

import jdk.proxy.UserService;

public class UserTx implements UserService {

    UserService userService;

    public UserTx(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void query() {
        System.out.println("UserTx user start log ");
        userService.query();
    }
}
