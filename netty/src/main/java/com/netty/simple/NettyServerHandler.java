package com.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
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
        //super.channelRead(ctx, msg);
        /*System.out.println("服务器读取线程 : " + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        System.out.println();
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();
        //将msg转化成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:" + channel.remoteAddress());*/

        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端,this is read ",CharsetUtil.UTF_8));
        //解决方案1 用户自定义普通任务
        ctx.channel().eventLoop().execute(()->{
            //这里有一个非常耗时的业务 -> 异步执行 -> 提交到channel对应的NioEventLoop 的taskQueue中
            try {
                Thread.sleep(10000);
                System.out.println("this is run taskQueue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ctx.channel().eventLoop().execute(()->{
            //这里有一个非常耗时的业务 -> 异步执行 -> 提交到channel对应的NioEventLoop 的taskQueue中
            try {
                Thread.sleep(20*1000);
                System.out.println("this is run taskQueue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //解决方案2 用户自定义定时任务,该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(()->{
            try {
                Thread.sleep(20*1000);
                System.out.println("this is run scheduleTaskQueue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },5, TimeUnit.SECONDS);


        System.out.println("go on ...");
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
