package com.simpleRpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * MyMessageEncoder  MyMessageDecoder解决粘包拆包问题
 */
public class MyMessageEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        //先发送内容长度
        out.writeInt(msg.getBytes().length);
        //发送具体的内容
        out.writeBytes(msg.getBytes());
    }
}