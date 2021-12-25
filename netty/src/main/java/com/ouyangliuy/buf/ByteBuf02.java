package com.ouyangliuy.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        //1.创建对象,该对象包含一个数组arr,是一个byte
        ByteBuf buffer = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);

        //使用相关的方法
        if(buffer.hasArray()){
            byte[] array = buffer.array();

            //
            System.out.println(new String(array,CharsetUtil.UTF_8).trim());

            System.out.println("byte:" + buffer);
            System.out.println(buffer.arrayOffset());
            System.out.println(buffer.readerIndex());
            System.out.println(buffer.writerIndex());
            System.out.println(buffer.maxCapacity());
        }

    }
}
