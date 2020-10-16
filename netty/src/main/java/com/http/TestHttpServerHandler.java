package com.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 1 SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter
 * 2. HttpObject 客户端和服务端相互通讯的数据被封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断msg是不是 httpRequest
        if(msg instanceof HttpRequest){
            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址 = " + ctx.channel().remoteAddress());
            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello  我是服务器", CharsetUtil.UTF_8);

            //构造一个http响应 , 即 httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            response.headers().set(HttpHeaderNames.ACCEPT_CHARSET,CharsetUtil.UTF_8);
           //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
