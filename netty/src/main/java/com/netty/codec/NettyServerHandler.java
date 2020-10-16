package com.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/**
 * 1.自定义一个Handler 需要继续netty规定好的某个HandlerAdapter
 * 2. 这时我们自定义一个Handler,才能称为一个handler
 */
public class NettyServerHandler  extends ChannelInboundHandlerAdapter {
    //读取数据实际


    /**
     * 1.ChannelHandlerContext ctx;上下文对象,含有pipeline通道,channel 地址
     * 2.Object msg; 就是客户端发送的数据 默认Object
     *
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       //读取从客户端发送的StudentPoJO.Student

        StudentPOJO.Student student = (StudentPOJO.Student) msg;

        System.out.println(" 客户端发送的数据 : " + "id = " + student.getId() + ", name = " + student.getName());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        //write 方法 + flush方法
        //将数据写入到缓存,并刷新
        //一般讲,我们对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端,this is readComplete",CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
