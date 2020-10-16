package com.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个netty提供的httpServerCodec codec = > [coder decoder]
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //2. 增加一个自己的地定义code
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }
}
