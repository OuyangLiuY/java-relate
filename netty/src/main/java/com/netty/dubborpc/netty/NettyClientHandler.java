package com.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler  extends ChannelInboundHandlerAdapter implements Callable<String> {

    private ChannelHandlerContext context;
    private String result;  //返回的结果
    private String param;  //  客户端调用方法时,传入的参数

    @Override
    public synchronized  String call() throws Exception {
        //发送消息,
        System.out.println("发送消息...");
        this.context.writeAndFlush(param);
        this.context.writeAndFlush("param...");
        System.out.println("call 1");
        wait(); // 等待channelRead获取结果 result
        System.out.println("call 2 ");
        return result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        this.context = ctx;
    }

    @Override
    public synchronized  void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        this.result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println();
        ctx.close();
    }

    void setParam(String param){
        this.param = param;
    }
}
