# Spring

IOC DI：？

Spring autowird

spring注入方式有几种：

byname，bytype，no，constructor（是4中注入模型）

注入方式 set和constructor两种。

自动装配：  永远用的是setter和constructor

## 什么是Spring的自动装配？

### Autowiring mode 自动注入模型

什么是自动注入模型：

自动装配模型是一种完成自动装配依赖的手段体现，每一种模型都使用了不同的技术去查找和填充bean；从spring官网上面可以看到spring只提出了4中自动装配模型（严格意义上是三种、因为第一种是no，表示不使用自动装配、使用），这四个模型分别用一个整形来表示，存在spring的beanDefinition当中，任何一个类默认是no这个装配模型，也就是一个被注解的类默认的装配模型是no也就是手动装配；
其中no用0来表示；bytype用2来表示；例如：

- 如果某个类X，假设X的bean对应的beanDefinition当中的autowireMode=2则表示这个类X的自动装配模型为bytype；如果autowireMode=1则表示为byname装配模型；



### IOC（DI）：依赖注入

什么是依赖注入：

**依赖注入是一个过程，主要通过setter和构造方法以及一些变体的方式完成把对象依赖、或者填充上的这个过程，不管手动装配还是自动装配都有这个过程。**



autowird 注入使用的field.set

lazy:

FactoryBean 和普通Bean有什么区别{

​	实例化的顺序不同，储存的地方不同

}

如何把一个对象放到spring容器中？{

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
