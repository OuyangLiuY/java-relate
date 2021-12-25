package com.ouyangliuy.hander.rpc;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
    1，先假设一个需求，写一个RPC
    2，来回通信，连接数量，拆包？
    3，动态代理呀，序列化，协议封装
    4，连接池
    5，就像调用本地方法一样去调用远程的方法，面向java中就是所谓的 面向interface开发
 */
public class RPCMain {
    public static void main(String[] args) {
        int f = 0x14141414;
        System.out.println(f);
        System.out.println();
    }

    public static <B> B proxyCreate(Class<B> interfaceInfo){
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?> [] infos = {interfaceInfo};

        return (B) Proxy.newProxyInstance(classLoader, infos,(proxy,method,args)->{
                //如何设计我们的consumer对于provider的调用过程
                //1.调用 服务 方法，参数， ===> 封装成 message [content]
                String name = interfaceInfo.getName();
                String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            MyContent content = new MyContent();
            content.setArgs(args);
            content.setName(name);
            content.setMethodName(methodName);
            content.setParameterTypes(parameterTypes);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(content);
            byte[] msgBody = out.toByteArray();
            //2.requestId + message ，本地缓存 ==> 协议：[header<>][msgBody]
            MyHeader header = createHeader(msgBody);

            out.reset();
            oout = new ObjectOutputStream(out);
            oout.writeObject(header);
                //3.连接池取得数据
                //4.发送 ===> 走IO out ==> 走netty (event   驱动)
                //5.如果从IO，
                return null;

        });
    }

    private static MyHeader createHeader(byte[] msg) {
        MyHeader header = new MyHeader();
        int size = msg.length;
        int f = 0x14141414;
        long requestID =  Math.abs(UUID.randomUUID().getLeastSignificantBits());
        //0x14  0001 0100
        header.setFlag(f);
        header.setDataLen(size);
        header.setRequestID(requestID);
        return header;
    }
}
