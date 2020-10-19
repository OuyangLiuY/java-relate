package com.netty.dubborpc.netty;

import com.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据实际
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead");
        System.out.println("收到消息 msg = " + msg);
        // 消息协议 ...
        if(msg.toString().startsWith("HelloServer#hello#")){
            String res = new HelloServiceImpl().sendMsg(msg.toString().substring(msg.toString().lastIndexOf("#")+1));
            ctx.writeAndFlush(res);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server exceptionCaught");
        ctx.close();
    }
}
