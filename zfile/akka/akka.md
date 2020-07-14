akka 高性能、高容错性的分布式&并行 应用架构。
-> 异步框架 
groovy sacala  
Actor 模型
共享内存， by 消息
Actor 消息 消息通信。

Akka
---
public
---
Akka:
Akka是JAVA虚拟机平台上构建高并发、分布式和容错应用的工具包和运行时。
Akka用Scala语言编写，同时提供了Scala和Java的开发接口。
Akka处理并发的方法基于Actor模型，Actor之间通信的唯一机制就是消息传递。

akka特点:
    1.对并发模型进行了更高的抽象
    2.是异步、非阻塞、高性能的事件驱动编程模型、
    3.是轻量级事件处理(1GB内存可容纳百万级别个Actor)
    4.它提供了一种称为actor的并发模型，其粒度比线程更小，可以在系统中启用大量的Actor
    5.它提供了一套容错机制，允许在Actor出现异常时进行一些恢复或重置操作
    6.Akka既可以在单机上构建高并发程序，也可以在网络中构建分布式程序，并提供位置透明的定位服务

akka可能的缺点：
    1.Actor的网络传输性能不是最优实现
    2.Actor并不是解决多线程问题，
    3.Akka不是处理工作流的最优选
    4.Akka不提供事务性的保证，而且几乎把所有分布式通信（即使是单机内分布式）中会出现的问题全部都交给开发人员自己考虑和设计。
    

```java
public void test(){
   	System.out.println("输出一个测试～！");
}
```



```shell
docker-compose version
```



