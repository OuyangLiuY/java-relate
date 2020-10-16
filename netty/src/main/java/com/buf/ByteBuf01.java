package com.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBuf01 {
    public static void main(String[] args) {
        //1.创建对象,该对象包含一个数组arr,是一个byte
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
