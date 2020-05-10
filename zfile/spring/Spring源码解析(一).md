# Spring

IOC DI：？

Spring autowird

spring注入方式有几种：

byname，bytype，no，（是4中注入模型）

注入方式 set和constract两种。

自动装配：  永远用的是setter和constract

autowird 注入使用的field.set

lazy:

FactoryBean 和普通Bean有什么区别{

​	实例化的顺序不同，储存的地方不同

}

​	如何把一个对象放到spring容器中？{

​		以下三种方式：

​		FactoryBean,扩展用

​		@Bean 做配置用

​		AnnotationConfigApplicationContext ac = new ..

​		ac.getBeanFactory().registerSingleton("beanName",MyObject object);

​		//比较鸡肋，因spring可能已经初始化完了，有可能你需要的类注入不进来

​		//其他类有依赖？

​		//custom bean 引用了其他spring bean 的类

​		//ac.register(MyObject object)

​		// ac.refresh()	



}
https://blog.csdn.net/java_lyvee/article/details/102499560





bean definition
关于beanDefinition 的第一篇笔记  一共三篇 https://blog.csdn.net/java_lyvee/article/details/102633067
https://www.processon.com/view/link/5c15e10ae4b0ed122da86303

https://shimo.im/docs/Nj0bcFUy3SYyYnbI/ 《无标题》，可复制链接后用石墨文档 App 或小程序打开
