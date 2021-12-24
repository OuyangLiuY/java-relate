package com.ouyangliuy.NIO;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    public static void main(String[] args) {

        ByteBuffer allocate = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            allocate.put((byte) i);
        }
        //读取
        allocate.flip();
        //得到一个只读buffer
        ByteBuffer buffer = allocate.asReadOnlyBuffer();
        System.out.println(buffer);
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        //ReadOnlyBufferException
        buffer.put((byte) 1000);
    }
}
