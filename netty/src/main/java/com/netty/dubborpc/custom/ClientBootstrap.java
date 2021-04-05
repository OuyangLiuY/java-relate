package com.netty.dubborpc.custom;


import com.netty.dubborpc.netty.NettyClient;
import com.netty.dubborpc.publicinterface.HelloService;

public class ClientBootstrap {
    //协议头
    public static final String providerName = "HelloService#hello#";
    private static int count;

    public static void main(String[] args) throws InterruptedException {

        NettyClient client = new NettyClient();

        HelloService service = (HelloService) client.getBean(HelloService.class, providerName);
        for (;;) {
            String res = service.sendMsg("你好,dubbo~  第[" + (++count) + "]次");
            System.out.println("调用的结果: res = " + res);
            Thread.sleep(2 * 1000);
        }
    }
}
