package com.ouyangliuy.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class AllocateMemory {


    public static void main(String[] args) {

        PooledByteBufAllocator allocator = new PooledByteBufAllocator();
        int[] init = {1,250,500,512,600,1024,4096,800000};
        for (int i = 0; i < init.length; i++) {
            ByteBuf buffer = allocator.buffer(init[i]);
            System.out.println(buffer.capacity());
        }

        ByteBuf byteBuf = allocator.directBuffer(1);
    }
}
