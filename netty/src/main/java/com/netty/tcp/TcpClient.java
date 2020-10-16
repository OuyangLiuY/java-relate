package com.netty.tcp;

import com.netty.inboundandoutboundhandler.MyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class TcpClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //注意客户端使用的不是ServerBootStrap而是Bootstrap
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类(反射)
                    .handler(new TcpClientInitializer());
            System.out.println(" 客户端 is ok ...");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
