package concur.sync;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class HashUtil {

    public static String  countHash(Object obj) throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        long hashcode = 0;
        for(long index = 7;index >0 ;index --){
            hashcode |= (unsafe.getByte(obj,index)  & 0xFF) << ((index - 1) *8);
        }
        String code = Long.toHexString(hashcode);
        return "hashUtil -----：0x"+code;
    }
}
