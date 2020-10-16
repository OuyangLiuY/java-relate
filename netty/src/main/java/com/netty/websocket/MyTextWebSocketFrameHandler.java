package com.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

// 该类表示一个文本帧(frame)
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器受到消息 " + msg);
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 " + LocalDateTime.now() + " ," + msg));
    }

    //客户端连接之后,触发的事件
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的值,asLongText 是唯一的, asShortText 不是唯一的
        System.out.println("handlerAdded 被调用了," + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用了," + ctx.channel().id().asShortText());
    }
    //客户端断开连接之后,触发的事件
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用了," + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生" + cause.getMessage());
        ctx.channel().close();
    }
}
