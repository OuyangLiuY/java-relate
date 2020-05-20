Redis:

问题：

1. redis为什么是key，value的，为什么不是支持SQL的
2. redis是多线程还是单线程，（回答单线程的可以回家了...）
3. redis的持久化开启热RDB和AOF下重启服务是如何加载的？（10个9个人会答错）
4. redis如果做集群如何规划？AKF/CAP如何实现和设计的？
5. 10万用户一年365天登陆情况如何用redis存储，并快速检索任意时间窗内的活跃用户？
6. redis的5中value类型用过几种？能举例吗？
7. 100万并发4G数据，10万并发400G数据，如何设计redis存储方式？



学习目录：

1、redis的前世今生

2、redis为什么是key，value的，为什么不是支持SQL的

redis db 写同步？

Value是 五种类型：

本地方法：计算向数据移动，I/O优化

整体模型是串行化，原子：并行 和 串行 谁更优？

分布式，一致性，CAP？一致性，可用性 ，分区容错性

3、redis的NIO&线程模型

4、redis是多线程还是但线程？

worker是单线程， I/O是多线程的，支持并发，连接数很多，在Linux是使用的epoll多路，



5、redis5.x的安装部署方式

6、redis的5大数据类型

7、10万用户一年365天登陆情况如何用redis存储，并快速检索任意时间窗内的活跃用户？

使用bitset 设置哪天登录了set一次，然后通过bitcount获取记录为1的次数，Strlen才46位

8、细节见真知：计算向数据移动、而非数据向计算移动

9、linux系统的支持：fork、copy on write

redis 6.x以后IO是多路

mencached：是key，value类型，都是value只支持String类型

JSON可以表示任何数据。

String ：

- 字符串操作 STRLEN key1 取的是字节数‘

  使用场景：session，kv缓存，数值计数器，fs文件系统（小文件系统，）内存

- 数值操作

- 二进制位 -> bitmap -> 什么二进制？如何操作二进制？

  - 二进制：

List：

Hash：

Set：

Sorted Set:

zset:



epoll:多路复用。也是一种NIO。

为什么要出现多路复用：

```
JVM：一个线程的成本大概1M。1.线程多了cpu调度成本浪费 2.内存成本
```



用户空间和内核空间提供了共享空间，

共享空间：

- 其中包括红黑树
- 链表

**顺序性**：每个连接内的命令是顺序处理的

Hbase：二进制安全的

redis：也是二进制安全的