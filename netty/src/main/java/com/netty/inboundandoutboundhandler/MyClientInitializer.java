package com.netty.inboundandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("MyClientInitializer 11 ");
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个自定义的handler,处理业务
        pipeline.addLast(new MyClientHandler());

    }
}
