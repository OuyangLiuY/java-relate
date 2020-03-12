package com.ywb.service;


import com.ywb.annotation.CustomService;

@CustomService("MyServiceImpl")
public class MyServiceImpl implements MyService {
    @Override
    public String query(String name, int age) {
        return "name=" + name + "; age=" + age;
    }
}
