package com.ouyangliuy.simpleRpc;

import com.ouyangliuy.simpleRpc.inter.HelloServiceImpl;

public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServerHandler.setClassNameMapping(new HelloServiceImpl());
        NettyServer.start(9999);
    }
}
