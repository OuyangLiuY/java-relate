package com.tinygame.herostory;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new HttpServerCodec(),//http
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new GameMsgHandler()
                );
            }
        });

        ChannelFuture future = b.bind(12345).sync();

        if (future.isSuccess()){
            System.out.println("服务器启动成功！");
            logger.debug("服务器启动成功!");
        }

        //等待服务器信道关闭
        future.channel().closeFuture().sync();
    }
}
