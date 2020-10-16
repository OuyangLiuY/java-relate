package com.netty.tcp;

import com.netty.inboundandoutboundhandler.MyByteToLongDecoder2;
import com.netty.inboundandoutboundhandler.MyClientLongToByteEncoder;
import com.netty.inboundandoutboundhandler.MyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("TcpServerInitializer 方法被调用了 ");
        ChannelPipeline pipeline = ch.pipeline();
        //加入解码器
        pipeline.addLast(new TcpMessageToDecoder());
        //加入编码器
        pipeline.addLast(new TcpMessageToEncoder());
        //加入一个自定义的handler,处理业务
        pipeline.addLast(new TcpServerHandler());

    }
}
