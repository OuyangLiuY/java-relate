package com.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler handler;

    //编写,获取代理对象
    public Object getBean(final Class<?> clazz, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{clazz}, ((proxy, method, args) -> {
            if (handler == null) {
                initClient();
            }
            System.out.println("执行了代理对象...");
            //args[0] 是传入的参数
            handler.setParam(providerName + args[0]);
            return executor.submit(handler).get();
        }));
    }

    public static void initClient() {
        handler = new NettyClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        //注意客户端使用的不是ServerBootStrap而是Bootstrap
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类(反射)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(handler); //加入自己的处理器
                        }
                    });
            //ChannelFuture cf = bootstrap.connect("127.0.0.1", 6789).sync();
            System.out.println(" 客户端 is ok ...");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6789).sync();
            //关闭通道进行监听，客户端这里为什么不能这样去监听呢，因为阻塞监听之后，
            // 就不能执行initClient之后的代码，也不会执行run方法去和服务端通信了
            //channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         /*finally {  //为什么不能这样写呢，只有在当前channel阻塞监听过程中才能使用finally去关闭
            group.shutdownGracefully();
            System.out.println("client finally");
        }*/
    }
}
