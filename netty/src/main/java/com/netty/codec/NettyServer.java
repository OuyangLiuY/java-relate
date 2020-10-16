package com.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1.创建boss和worker group
        //2.boss group 只处理连接请求,真正和客户端业务处理,会交给worker group
        //3.两个都是无限循环
        //4.boss group 和 workerGroup  含有的NioEventLoopGroup 的个数,默认是其核心数的2倍
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
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
                           //在pipeline 加入 protobuf decoder
                           // 指定对哪种对象进行解码
                           pipeline.addLast("decoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                           pipeline.addLast(new NettyServerHandler());
                       }
                   });//给我们的worker group的EventLoop对应的管道设置处理器
           System.out.println("服务器 is ready .....");
           //绑定一个对象并同步,生成一个ChannelFuture对象
           //启动服务器(并绑定端口)
           ChannelFuture cf = bootstrap.bind(7000).sync();
           //对关闭通道进行监听
           cf.channel().closeFuture().sync();
       }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
       }
    }
}
