    package com.NIO;

    import java.net.InetSocketAddress;
    import java.nio.ByteBuffer;
    import java.nio.channels.*;
    import java.util.Iterator;
    import java.util.Set;

    public class NIOServer {
        public static void main(String[] args) throws  Exception{
            //创建serverSocketChannel -> serverSocket
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //得到一个selector对象
            Selector selector = Selector.open();
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(6666));
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //把serverSocketChannel 注册到selector 关心事件为OP_ACCEPT
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("注册后的selection数量= " + selector.keys().size());
            //循环等待连接
            while (true){
                if(selector.select(1000) == 0){ //没有事件发生
                    System.out.println("服务器等待了1s,无连接");
                    continue;
                }
                //如果返回的 > 0 就获取到相关的selectionKey集合
                //1.如果返回的 > 0 ,表示获取到关注的事件
                //2.selector.selectedKeys 方法返回的关注事件的集合
                // 通过 selectionKeys 反向获取通道
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                //selectionKeys 遍历
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()){
                    //获取selectionKey
                    SelectionKey selectionKey = it.next();
                    // 根据key 对应的通道发生的相关事件做相应的处理
                    if(selectionKey.isAcceptable()){ //如果是OP_ACCEPT ,有新的客户端连接
                        //该客户端生成一个SocketChannel
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        System.out.println("客户端连接成功,生成了socketChannel = " + socketChannel.hashCode());
                        //将socketChannel设置为非阻塞
                        socketChannel.configureBlocking(false);
                        //将socketChannel 注册到selector,关注事件为OP_READ ,同时给该socketChannel 关联一个buffer
                        socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        System.out.println("客户端连接后注册selector的selection数量= " + selector.keys().size());
                    }
                    if(selectionKey.isReadable()){ // 发生OP_READ
                        //通过key反向获取对应的channel
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        //获取到该channel关联的buffer
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        channel.read(byteBuffer);
                        System.out.println("从客户端发出来数据: " + new String(byteBuffer.array()));
                        channel.close();
                    }
                    //手动从集合中移除selectionKey,防止重复操作
                    it.remove();
                }
            }
        }
    }
