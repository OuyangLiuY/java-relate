package com.netty.tcp;

import com.netty.inboundandoutboundhandler.MyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpServer {
    public static void main(String[] args) throws Exception{
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
                    .childHandler(new TcpServerInitializer());//自定义初始化类handler
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
