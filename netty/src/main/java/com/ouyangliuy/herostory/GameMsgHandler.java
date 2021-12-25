package com.ouyangliuy.herostory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext msg, Object o) throws Exception {

        System.out.println("客户端的消息： msg = " + msg);

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();
        //
        byte[] bytes
                 = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        System.out.println("收到的字节：");
        for (byte aByte : bytes) {
            System.out.print(aByte);
            System.out.println(",");
        }
        System.out.println();
    }
}
