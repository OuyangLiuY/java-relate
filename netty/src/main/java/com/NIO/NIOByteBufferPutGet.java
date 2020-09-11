package com.NIO;

import java.nio.ByteBuffer;

public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(64);
        allocate.putInt(100);
        allocate.putLong(1232L);
        allocate.putChar('中');
        allocate.putShort((short) 6);
        allocate.flip();
        System.out.println(allocate.getInt());
        System.out.println(allocate.getLong());
        System.out.println(allocate.getChar());
        System.out.println(allocate.getShort());
        System.out.println(allocate.getShort());
    }
}
