package com.netty.dubborpc.provider;

import com.netty.dubborpc.netty.NettyServer;

//启动一个服务提供者,就是nettyServer
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",6789);
    }
}
