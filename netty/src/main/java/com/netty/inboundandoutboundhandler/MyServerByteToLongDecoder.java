package com.netty.inboundandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyServerByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *  decode 会根据接收的数据,,被调用多次,直到确定没有新的元素被添加到list,或者是byteBuf没有更多的可读字节为止
     * @param ctx 上下文信息
     * @param in 入站数据字节
     * @param out   将需要处理的数据放到下一个要处理的list中
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyServerByteToLongDecoder : decode 1 ");
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
