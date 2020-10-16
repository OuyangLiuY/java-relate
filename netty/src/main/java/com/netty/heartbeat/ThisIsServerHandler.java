package com.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class ThisIsServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = "";
            switch (event.state()){
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                default:
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时时间-- " + eventType);
            System.out.println("服务器处理事件 ... ");
            ctx.close();
        }
    }
}
