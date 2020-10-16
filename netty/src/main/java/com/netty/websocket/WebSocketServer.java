package com.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
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
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象(匿名对象)
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            //因为基于http协议,使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写 ,ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                            说明:
                                1.http数据在传输过程中是分段的,HttpObjectAggregator,可以将多个段聚合
                                2.这就是为什么当浏览器发送大量数据时,就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            1.对应webSocket,它的数据是以帧(frame)形式传递的
                            2.可以看到WebSocketFrame下面有6个子类
                            3.浏览器请求时 ws:localhost:7000/hello 表示请求的uri
                            4.WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议,保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义处理器
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
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
