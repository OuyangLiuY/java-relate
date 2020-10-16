package com.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class TcpMessageToDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("TcpMessageToDecoder 方法被掉用了");
        int len = in.readInt();
        byte[] content = new byte[len];
        in.readBytes(content);
        // 封装成MessageProtocol 对象,放入out,传递给下一个handler处理
        MessageProtocol protocol = new MessageProtocol();
        protocol.setLen(len);
        protocol.setContent(content);

        out.add(protocol);

    }
}
