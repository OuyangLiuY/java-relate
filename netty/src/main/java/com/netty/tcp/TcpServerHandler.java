package com.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class TcpServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        /*System.out.println("TcpServerHandler : channelRead0 2 ");
        System.out.println("从客户端 " + ctx.channel().remoteAddress().toString().substring(1) + ", long = " + msg);
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, CharsetUtil.UTF_8);

        System.out.println("从服务器发来的消息:"+ message);
        System.out.println("消息接受量 : " + ++count);

        //服务器回送数据给客户端,回送一个随机数据
        ByteBuf response = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(response);*/


        //接受到数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务器接收到数据 : " + new String(content,CharsetUtil.UTF_8));
        System.out.println("长度 = " + len);
        System.out.println("服务器接收到的数据量 = " + (++count));

        //服务器回送数据给客户端,回送一个随机数据
        String string = UUID.randomUUID().toString();
        byte[] bytes = string.getBytes(CharsetUtil.UTF_8);
        MessageProtocol protocol = new MessageProtocol();
        protocol.setLen(bytes.length);
        protocol.setContent(bytes);
        ctx.writeAndFlush(protocol);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        System.out.println(" 异常 " + cause.getMessage());
        ctx.close();
    }
}
