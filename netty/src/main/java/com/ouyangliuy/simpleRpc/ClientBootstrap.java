package com.ouyangliuy.simpleRpc;

import com.ouyangliuy.simpleRpc.inter.HelloService;
import com.ouyangliuy.simpleRpc.inter.Result;

public class ClientBootstrap {
    private static String host = "127.0.0.1";
    private static int port = 9999;

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static void main(String[] args) {
        //连接netty，并获得一个代理对象
        HelloService bean = NettyClient.getBean(HelloService.class);
        //测试返回结果为java bean
        Result res = bean.hello("ffafa");
        System.out.println("res=====" + res.getContent());
        //测试返回结果为 String
        String str = bean.str();
        System.out.println("str=====" + str);
    }
}
