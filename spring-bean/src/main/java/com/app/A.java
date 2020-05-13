package com.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;


@Component
public class A {
    //
    @Autowired
        B b;

    public void setB(B b) {
       // this.b = b;
      /*  System.out.println("spring 找到符合的setter");
        System.out.println("和属性无关，甚至可以不要属性");
        System.out.println("可以直接调用，这个A里面就没有任何属性");
        System.out.println(b);
        System.out.println("在不考虑注解的情况下");
        System.out.println("如果配置了自动装配则不需要手动配置");*/
    }
}
