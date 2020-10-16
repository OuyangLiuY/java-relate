package com.netty.inboundandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyClientLongToByteEncoder extends MessageToByteEncoder<Long> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("encode 2 ");
        System.out.println("MyClientLongToByteEncoder de encode 方法被调用");
        System.out.println("msg = " + msg);
        out.writeLong(msg);
    }
}
