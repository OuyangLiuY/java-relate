package com.netty.dubborpc.provider;

import com.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sendMsg(String msg) {

        System.out.println("msg = " + msg);
        if(msg != null){
            return "你好客户端,我收到了你的消息 [ " + msg + " ]";
        }
        return "你好客户端,我收到了你的消息.";
    }
}
