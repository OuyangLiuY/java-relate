package com.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TcpMessageToEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {

        System.out.println("TcpMessageToEncoder  方法被调用了");

        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
