package com.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class TcpClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
       /* byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes,CharsetUtil.UTF_8);
        System.out.println("客户端受到消息:"+ message);
        System.out.println("消息接受量 : " + ++count);*/

        //客户端接收到数据
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("客户断接收到数据 : " + new String(content,CharsetUtil.UTF_8));
        System.out.println("长度 = " + len);
        System.out.println("客户端接收到的数据量 = " + (++count));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {

            String msg = "今天天气冷了, 要吃火锅的~";
            byte[] content = msg.getBytes(CharsetUtil.UTF_8);
            int len = msg.getBytes(CharsetUtil.UTF_8).length;
            MessageProtocol protocol = new MessageProtocol();
            protocol.setContent(content);
            protocol.setLen(len);
            ctx.writeAndFlush(protocol);


            /*ByteBuf buf = Unpooled.copiedBuffer("hello server" + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);*/
        }
    }
}
