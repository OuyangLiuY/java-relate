package com.netty.inboundandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer  extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("MyServerInitializer 11 ");
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个出站的handler对数据进行一个编码
        //pipeline.addLast(new MyServerByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 服务器回送消息给客户端是的encoder,出站的handler
        pipeline.addLast(new MyClientLongToByteEncoder());

        /**注意: 数据编解码必须放在处理业务逻辑的前面,否则不生效*/

        //加入一个自定义的handler,处理业务
        pipeline.addLast(new MyServerHandler());

    }
}
