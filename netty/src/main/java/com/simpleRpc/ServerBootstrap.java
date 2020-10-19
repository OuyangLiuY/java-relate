package com.simpleRpc;

import com.simpleRpc.inter.HelloServiceImpl;

public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServerHandler.setClassNameMapping(new HelloServiceImpl());
        NettyServer.start(9999);
    }
}
