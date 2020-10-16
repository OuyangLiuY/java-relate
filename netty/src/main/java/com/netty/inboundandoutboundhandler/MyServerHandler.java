package com.netty.inboundandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("MyServerHandler : channelRead0 2 ");
        System.out.println("从客户端 " + ctx.channel().remoteAddress().toString().substring(1) + ", long = " + msg);


        //给客户端回送一个long
        ctx.writeAndFlush(10228L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
