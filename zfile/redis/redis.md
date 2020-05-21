 INCRBY key incrementRedis:

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

## DB-Engines [DataBase](https://db-engines.com/en/)

# 什么是Reids？

Redis is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker. It supports data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs, geospatial indexes with radius queries and streams. Redis has built-in replication, Lua scripting, LRU eviction, transactions and different levels of on-disk persistence, and provides high availability via Redis Sentinel and automatic partitioning with Redis Cluster.

译文：

Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。 它支持多种类型的数据结构，如 [字符串（strings）](http://www.redis.cn/topics/data-types-intro.html#strings)， [散列（hashes）](http://www.redis.cn/topics/data-types-intro.html#hashes)， [列表（lists）](http://www.redis.cn/topics/data-types-intro.html#lists)， [集合（sets）](http://www.redis.cn/topics/data-types-intro.html#sets)， [有序集合（sorted sets）](http://www.redis.cn/topics/data-types-intro.html#sorted-sets) 与范围查询， [bitmaps](http://www.redis.cn/topics/data-types-intro.html#bitmaps)， [hyperloglogs](http://www.redis.cn/topics/data-types-intro.html#hyperloglogs) 和 [地理空间（geospatial）](http://www.redis.cn/commands/geoadd.html) 索引半径查询。 Redis 内置了 [复制（replication）](http://www.redis.cn/topics/replication.html)，[LUA脚本（Lua scripting）](http://www.redis.cn/commands/eval.html)， [LRU驱动事件（LRU eviction）](http://www.redis.cn/topics/lru-cache.html)，[事务（transactions）](http://www.redis.cn/topics/transactions.html) 和不同级别的 [磁盘持久化（persistence）](http://www.redis.cn/topics/persistence.html)， 并通过 [Redis哨兵（Sentinel）](http://www.redis.cn/topics/sentinel.html)和自动 [分区（Cluster）](http://www.redis.cn/topics/cluster-tutorial.html)提供高可用性（high availability）。

# Redis intstall Tutorial

## Centos教程

1. 配置环境：

   ```shell
   yum install wget gcc make
   ```

2. cd ~ && mkdir software

3. wget http://download.redis.io/releases/redis-5.0.5.tar.gz

4. tar xf/-zxvf redis**.tar.gz

5. cd redis-5.0.5/src

6. cat README.md

7. cd ..

8. 将客户端，服务器执行文件跟源码分离开：make PREFIX=/application/redis install

9. 配置环境变量：

   ```shell
   vi /etc/profile
   export REDIS_HOME=/application/redis
   export PATH=$PATH:$REDIS_HOME/bin
   ```

10. 配置为一个linux服务：

    ```shell
    cd utils
    ./install_server.sh
    ## 根据提示配置即可
    ```

11. 查看redis服务：

    ```shell
    ps -fe | grep redis	
    ```

# Redis 6.0之前的执行流程

![6.0_before_processor](../../images/redis/6.0_before_processor.png)

# Redis 6.x新特性：

Redis 6 加入多线程，**Redis 的多线程部分只是用来处理网络数据的读写和协议解析，执行命令仍然是单线程。**

加入多线程 IO 之后，整体的读流程如下:

1. 主线程负责接受建立请求，读事件到来(收到请求)，则放到一个全局等待读处理队列
2. 主线程处理完读事件之后，通过RR（round robin）将这些连接分配给这些IO线程，然后主线程等待状态
3. IO线程将请求数据读取并解析完成（这里只是读数据和解析并不执行）
4. 主线程执行所有命令并清空整个请求等待读处理队列（执行部分串行）

执行流程：

![6.0_after_processor](../../images/redis/6.0_after_processor.png)

默认并不开启多线程，需要参数设置，IO_THREADS_MAX_NUM 最大为128

networking.c文件

```c
/* Initialize the data structures needed for threaded I/O. */
void initThreadedIO(void) {
    io_threads_active = 0; /* We start with threads not active. */

    /* Don't spawn any thread if the user selected a single thread:
     * we'll handle I/O directly from the main thread. */
    if (server.io_threads_num == 1) return;

    if (server.io_threads_num > IO_THREADS_MAX_NUM) {
        serverLog(LL_WARNING,"Fatal: too many I/O threads configured. "
                             "The maximum number is %d.", IO_THREADS_MAX_NUM);
        exit(1);
    }

    /* Spawn and initialize the I/O threads. */
    for (int i = 0; i < server.io_threads_num; i++) {
        /* Things we do for all the threads including the main thread. */
        io_threads_list[i] = listCreate();
        if (i == 0) continue; /* Thread 0 is the main thread. */

        /* Things we do only for the additional threads. */
        pthread_t tid;
        pthread_mutex_init(&io_threads_mutex[i],NULL);
        io_threads_pending[i] = 0;
        pthread_mutex_lock(&io_threads_mutex[i]); /* Thread will be stopped. */
        if (pthread_create(&tid,NULL,IOThreadMain,(void*)(long)i) != 0) {
            serverLog(LL_WARNING,"Fatal: Can't initialize IO thread.");
            exit(1);
        }
        io_threads[i] = tid;
    }
}
```

加入队列:

```c
/* Return 1 if we want to handle the client read later using threaded I/O.
 * This is called by the readable handler of the event loop.
 * As a side effect of calling this function the client is put in the
 * pending read clients and flagged as such. */
int postponeClientRead(client *c) {
    if (io_threads_active &&
        server.io_threads_do_reads &&
        !ProcessingEventsWhileBlocked &&
        !(c->flags & (CLIENT_MASTER|CLIENT_SLAVE|CLIENT_PENDING_READ)))
    {
        c->flags |= CLIENT_PENDING_READ;
        listAddNodeHead(server.clients_pending_read,c);
        return 1;
    } else {
        return 0;
    }
}

/* When threaded I/O is also enabled for the reading + parsing side, the
 * readable handler will just put normal clients into a queue of clients to
 * process (instead of serving them synchronously). This function runs
 * the queue using the I/O threads, and process them in order to accumulate
 * the reads in the buffers, and also parse the first command available
 * rendering it in the client structures. */
int handleClientsWithPendingReadsUsingThreads(void) {
    /**...*/
}
```



# Redis中value的类型

## String

### 字符串类型：

- SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL]

- SETNX：

- SETEX key seconds value：

  Set the value and expiration of a key。（设置可以的有效期）

- GETSET key value

- get:

- append:

- setrange:

- GETRANGE:

- STRLEN

**note:**

1. 命令前M：multiple，表示多个操作。
2. 命令后EX:expiration 表示设置一个失效时间。
3. 命令后NX：Set the value of a key, only if the key does not exist.

### 数值类型：

-  INCR key：
-  INCRBY key increment
- INCRBYFLOAT key increment
-  DECR key
- DECRBY key decrement

note: set k1 999 STRLEN k1 :3 incr k1 STRLEN k1 4

**使用场景：**抢购，秒杀，详情页，点赞数，加评论，可以降低数据库的压力。

### BitMap类型：

-  SETBIT key offset value

  Sets or clears the bit at offset in the string value stored at key

- BITCOUNT key [start end]

  Count set bits in a string.[start 和 end]指的是字节，一个字节8位

- BITOP operation destkey key [key ...]

   Perform bitwise operations between strings。

   eg：BITOP and/or destkey k1 k2

-  BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]

**使用场景：**

1. 统计用户一年的登陆天数，
2. 活跃用户统计，根据登陆后将某一个bit为设置为1,根据操作bit为来统计

## List

-  LPUSH key element [element ...]

  Prepend one or multiple elements to a list

**使用场景：**

1. 可以当作栈类型，使用同向命令
2. 可以当作队列类型，使用反向队列
3. 可以当作数组类型，
4. 可以做阻塞队列，单播队列，多个redis服务连接起来，读取某个key的值，如果该key不存在或者没有值，则等待到另外一个redis服务往这个key中set值，然后另外一个redis服务就可以获取这个key中的值。

## Hash

- Hset key name "zhangsan"
- Hget key

**note:**可以对field中的值进行数值计算,redis的value是key-value形式。

**使用场景：**比如点赞，收藏，详情页

## Set

- SDIFF key [key ...]

  Subtract multiple sets

- SDIFFSTORE destination key [key ...]

   Subtract multiple sets and store the resulting set in a key

- SADD key member [member ...]

  Add one or more members to a set

- SCARD key

  Get the number of members in a set

- SINTER key [key ...]

  Intersect multiple sets.求集合的交集

- SUNION key [key ...]

  Add multiple sets。求并集

- SMEMBERS key

   Get all the members in a set

- SRANDMEMBER key [count]

  Get one or multiple random members from a set

  count类型：

  正数：取出一个去重的结果集合，（不能超过总的结果集）

  负数：取出一个带重复的结果集，count是多少就取出多少个，跟结果集的多少无关

   0 ： 不返回数据

**使用场景：**

1. 无序性，随机性。
2. 集合操作，并集合，交集合，例如共同好友，共同关注等等
3. 做随机时间，抽奖等，

## Sorted_sets

BZPOPMAX key [key ...] timeout
  summary: Remove and return the member with the highest score from one or more sorted sets, or block until one is available
  since: 5.0.0

  BZPOPMIN key [key ...] timeout
  summary: Remove and return the member with the lowest score from one or more sorted sets, or block until one is available
  since: 5.0.0

  ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
  summary: Add one or more members to a sorted set, or update its score if it already exists
  since: 1.2.0

  ZCARD key
  summary: Get the number of members in a sorted set
  since: 1.2.0

  ZCOUNT key min max
  summary: Count the members in a sorted set with scores within the given values
  since: 2.0.0

  ZINCRBY key increment member
  summary: Increment the score of a member in a sorted set
  since: 1.2.0

  ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
  summary: Intersect multiple sorted sets and store the resulting sorted set in a new key
  since: 2.0.0

  ZLEXCOUNT key min max
  summary: Count the number of members in a sorted set between a given lexicographical range
  since: 2.8.9

  ZPOPMAX key [count]
  summary: Remove and return members with the highest scores in a sorted set
  since: 5.0.0

  ZPOPMIN key [count]
  summary: Remove and return members with the lowest scores in a sorted set
  since: 5.0.0

  ZRANGE key start stop [WITHSCORES]
  summary: Return a range of members in a sorted set, by index
  since: 1.2.0

  ZRANGEBYLEX key min max [LIMIT offset count]
  summary: Return a range of members in a sorted set, by lexicographical range
  since: 2.8.9

  ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
  summary: Return a range of members in a sorted set, by score
  since: 1.0.5

  ZRANK key member
  summary: Determine the index of a member in a sorted set
  since: 2.0.0

  ZREM key member [member ...]
  summary: Remove one or more members from a sorted set
  since: 1.2.0

  ZREMRANGEBYLEX key min max
  summary: Remove all members in a sorted set between the given lexicographical range
  since: 2.8.9

  ZREMRANGEBYRANK key start stop
  summary: Remove all members in a sorted set within the given indexes
  since: 2.0.0

  ZREMRANGEBYSCORE key min max
  summary: Remove all members in a sorted set within the given scores
  since: 1.2.0

  ZREVRANGE key start stop [WITHSCORES]
  summary: Return a range of members in a sorted set, by index, with scores ordered from high to low
  since: 1.2.0

  ZREVRANGEBYLEX key max min [LIMIT offset count]
  summary: Return a range of members in a sorted set, by lexicographical range, ordered from higher to lower strings.
  since: 2.8.9

  ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
  summary: Return a range of members in a sorted set, by score, with scores ordered from high to low
  since: 2.2.0

  ZREVRANK key member
  summary: Determine the index of a member in a sorted set, with scores ordered from high to low
  since: 2.0.0

  ZSCAN key cursor [MATCH pattern] [COUNT count]
  summary: Incrementally iterate sorted sets elements and associated scores
  since: 2.8.0

  ZSCORE key member
  summary: Get the score associated with the given member in a sorted set
  since: 1.2.0

  ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
  summary: Add multiple sorted sets and store the resulting sorted set in a new key
  since: 2.0.0

**note:** 

1. 物理内存左小右大，不随命令发生变化，zrang，zrevrang
2. 集合操作，并集，交集，可以进行权重/或者聚合，例如：根据score的权重进行并集计算，ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
3. 排序的底层实现是**Skip List（跳跃表）**。

## Redis的进阶使用

