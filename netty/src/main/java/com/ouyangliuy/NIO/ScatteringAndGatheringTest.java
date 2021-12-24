package com.ouyangliuy.NIO;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * scattering:将数据写入到buffer时,可以采用buffer数组,依次写入[[分散]
 * gathering:从buffer读取数据时,可以采用buffer数组,依次读
 *
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args)throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(6666);
        //绑定端口到socket,并启动
        serverSocketChannel.socket().bind(address);
        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept();
        //
        int messageLength = 8;
        while (true){
            int byteRead = 0;
            while(byteRead < messageLength){
                long read = socketChannel.read(byteBuffers);
                byteRead +=read;
                System.out.println("byteRead:=" + byteRead);
                Arrays.stream(byteBuffers).map(buffer -> "position = " + buffer.position() + ",limit = " + buffer.limit()).forEach(
                        System.out::println
                );
                //将所有的buffer进行flip
                Arrays.stream(byteBuffers).forEach(Buffer::flip);
                //将数据读取显示到客户端
                long byteWrite= 0;
                while (byteWrite < messageLength){
                    long write = socketChannel.write(byteBuffers);
                    byteWrite += write;
                }
                //将所有的buffer进行clear
                Arrays.asList(byteBuffers).forEach(Buffer::clear);
                System.out.println("byteRead = " + byteRead + ",byteWrite = " + byteWrite + ",messageLength = " + messageLength);
            }
        }
    }
}
