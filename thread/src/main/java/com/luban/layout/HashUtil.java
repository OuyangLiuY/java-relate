package com.luban.layout;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class HashUtil {
    public static void countHash(Object object) throws NoSuchFieldException, IllegalAccessException {
        // 手动计算HashCode
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        long hashCode = 0;
        for (long index = 7; index > 0; index--) {
            // 取Mark Word中的每一个Byte进行计算
            hashCode |= (unsafe.getByte(object, index) & 0xFF) << ((index - 1) * 8);
        }
        String code = Long.toHexString(hashCode);
        System.out.println("util-----------0x"+code);

    }
}
