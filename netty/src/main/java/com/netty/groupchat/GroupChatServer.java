package com.netty.groupchat;

import com.netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private int port;

    public GroupChatServer(int port){
        this.port = port;
    }
    public void run() throws Exception {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //使用链式编程来做
            bootstrap.group(bossGroup,workerGroup) //设置两个线程
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列得连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象(匿名对象)
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });//给我们的worker group的EventLoop对应的管道设置处理器
            System.out.println("服务器启动成功");
            //绑定一个对象并同步,生成一个ChannelFuture对象
            //启动服务器(并绑定端口)
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new GroupChatServer(7776).run();

    }
}
