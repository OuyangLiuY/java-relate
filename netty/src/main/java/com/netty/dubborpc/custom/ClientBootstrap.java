package com.netty.dubborpc.custom;


import com.netty.dubborpc.netty.NettyClient;
import com.netty.dubborpc.publicinterface.HelloService;

public class ClientBootstrap {
    //协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {

        NettyClient client = new NettyClient();
        HelloService service = (HelloService) client.getBean(HelloService.class, providerName);
        String res = service.sendMsg("你好,dubbo~");
        System.out.println("调用的结果: res = " + res);
    }
}
