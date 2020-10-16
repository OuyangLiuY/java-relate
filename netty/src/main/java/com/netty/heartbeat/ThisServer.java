package com.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ThisServer {
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
                    .handler(new LoggingHandler(LogLevel.INFO)) //增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供的 IdleStateHandler :   是netty提供的处理空闲状态的处理器
                            /* 参数:
                             * 1. readerIdleTime : 表示多长时间没有读,就会发送一个心跳检测包检测是否连接
                             * 2. writerIdleTime : 表示多长时间没有写,就会发送一个心跳检测包检测是否连接
                             * 3. allIdleTime : 表示多长时间没有读写,就会发送一个心跳检测包检测是否连接
                             *
                             * 4. Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                             * read, write, or both operation for a while.
                             * 5. 当IdleStateEvent触发后,就会传递给管道的一下handler去处理,通过调用下一个handler的userEventTriggered方法来处理具体的事件
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            pipeline.addLast(new ThisIsServerHandler());
                        }
                    });
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
