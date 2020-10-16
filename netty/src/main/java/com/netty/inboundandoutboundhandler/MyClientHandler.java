package com.netty.inboundandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("channelRead0 3 ");
        System.out.println("服务器ip = " + ctx.channel().remoteAddress());
        System.out.println("收到服务器的数据 = " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 1 ");
        System.out.println("myClient 发送数据 :");

        ctx.writeAndFlush(12345L);

        /*
            1. abcdabcdabcdabcd 是16个字节,故服务端会调用两次decoder
            2. 该处理器的前一个handler是MyClientLongToByteEncoder
            3. MyClientLongToByteEncoder 的父类是 MessageToByteEncoder
            4. 父类 MessageToByteEncoder中有一个write方法:
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) { //判断当前msg 是不是应该被处理的类型
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf);
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else { //否则 就 跳过 encoder方法
                ctx.write(msg, promise);
            }
        } catch (EncoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new EncoderException(e);
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }

    5.  因此我们编写的Encoder 是要注意传入的数据类型和处理的数据类型一致
         */
       // ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));

    }
}
