package com.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        System.out.println("client = " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server 我是 客户端,", CharsetUtil.UTF_8));
    
        StudentPOJO.Student.Builder student = StudentPOJO.Student.newBuilder().setId(1).setName("我是包子头");
        StudentPOJO.Student instance = StudentPOJO.Student.getDefaultInstance();
        ctx.writeAndFlush(instance);
        ctx.writeAndFlush(student);
    }

    //当通道有读取事件时,会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息 : = " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址:" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
