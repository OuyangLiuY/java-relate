package org.example.proxy.cglib.hello;


import net.sf.cglib.proxy.Enhancer;

/**
 * cglib是一种基于ASM的字节码生成库，用于生成和转换Java字节码.
 *
 * 而ASM是一个轻量但高性能的字节码操作框架。cglib是基于ASM的上层应用，对于代理没有实现接口的类，cglib非常实用。
 */
public class HelloCglib {

    public static void main(String[] args) {
        //Enhancer是CGLIB的核心工具类,是一个字节码增强器，它可以方便的对类进行扩展
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        //设置回调所需要的拦截器
        enhancer.setCallback(new MyMethodInterceptor());
        //
        PersonService personService = (PersonService) enhancer.create();
        System.out.println(personService.sayHello("hello cglib!"));
    }


}
// javassist提供的动态代理接口和javassist字节码。

