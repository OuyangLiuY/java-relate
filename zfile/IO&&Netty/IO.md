## Linux中 vfs  fd pagecache

**vfs：** 虚拟文件描述，linux系统中，vfs树，使用命令：`ls -l  /`

**fd：** 一切皆文件

任何程序都有：**0 ：标准输入，1：标准输出，2：报错输出；**

```shell
/proc/
/proc/$$
# $$ : 当前bash的pid $BASHPID
/proc/$$/fd # 命令：
lsof -op $$  # 查看当前程序的输入输出，等其他信息
< # 输入
> # 输出
| # 管道
```



**pagecache：** 用作**缓存磁盘文件**的内存就叫做page cache

页缓冲/文件缓冲，大小通常为**4k**，**在64位系统上为8k**，**由若干个磁盘块组成（物理上不一定连续），也即由若干个bufferCache组成**

```
用户进程启动read()系统调用后，内核会首先查看page cache里有没有用户要读取的文件内容，如果有（cache hit），那就直接读取，没有的话（cache miss）再启动I/O操作从磁盘上读取，然后放到page cache中，下次再访问这部分内容的时候，就又可以cache hit，不用忍受磁盘的龟速了（相比内存慢几个数量级）。
```

内核使用address_space结构来表示一个page cache；

```
struct address_space {
    struct inode            *host;              /* owning inode */
    struct radix_tree_root  page_tree;          /* radix tree of all pages */
    spinlock_t              tree_lock;          /* page_tree lock */
    unsigned int            i_mmap_writable;    /* VM_SHARED ma count */
    struct prio_tree_root   i_mmap;             /* list of all mappings */
    struct list_head        i_mmap_nonlinear;   /* VM_NONLINEAR ma list */
    spinlock_t              i_mmap_lock;        /* i_mmap lock */
    atomic_t                truncate_count;     /* truncate re count */
    unsigned long           nrpages;            /* total number of pages */
    pgoff_t                 writeback_index;    /* writeback start offset */
    struct address_space_operations *a_ops;     /* operations table */
    unsigned                long flags;         /* gfp_mask and error flags */
    struct backing_dev_info *backing_dev_info;  /* read-ahead information */
    spinlock_t              private_lock;       /* private lock */
    struct list_head        private_list;       /* private list */
    struct address_space    *assoc_mapping;     /* associated buffers */
};
```



## BIO

**同步阻塞**：调用，一直等到有数据才返回，否则一直等待

**同步非阻塞：** 一次调用，一次结果返回，只是可能返回的是空结果而已

## NIO

**异步非阻塞：** 异步调用获取，不阻塞

## TCP网路I/O



## 多路复用器

**注意：** 其实无论NIO，select，POLL都是要遍历所有的IO，询问状态，只不过，

**NIO**：这个遍历的成本在用户态内核态切换

**select,poll**：这个遍历的过程出发一次系统调用，用户态内核态的切换，过程中，把fds传递给内核，内核重新根据用户这次调用传过来的fds，遍历，修改状态。

### select/poll

![多路复用器(select-poll)](../../images/netty/IO/多路复用器(select-poll).jpg)

**select/poll：区别是select限制文件描述符是1024个字节，poll中没这个限制**

**多路复用器，select，poll的弊端：**

1. 每次都要重新，重复传递fds（内核开辟空间）
2. 每次，内核被调用之后，针对这次调用，触发一个遍历fds全量的复杂度

### epoll

![epoll(VS)select_poll](../../images/netty/IO/epoll(VS)select_poll.png)

epoll中底层方法：

1. **epoll_create()**

   ```
   epoll_create()创建一个epoll“实例”，size参数目前被忽略。
   epoll_create()返回一个引用新的epoll实例的文件描述符。
   当不再需要时，应该使用close()关闭epoll_create()返回的文件描述符。
   ```

   ```
   成功时：返回一个非负文件描述符；
   错误时：返回-1，并将errno设置为指示错误。
   ```

   

2. **epoll_wait()**

   ```
   epoll_wait()系统调用等待文件描述符epfd引用的epoll实例上的事件。 
   maxevents参数必须大于零。
   该调用超时等待时间为timeout毫秒。
   timeout为-1时，会使epoll_wait()无限期地等待；
   timeout为 0时，即使没有可用的事件（返回码等于零），epoll_wait()也会立即返回。
   
   struct epoll_event定义如下：
   
       typedef union epoll_data
       {
           void    *ptr;
           int      fd;
           uint32_t u32;
           uint64_t u64;
       } epoll_data_t;
   
       struct epoll_event
       {
           uint32_t     events;    /* epoll事件 */
           epoll_data_t data;      /* 用户数据变量 */
       };
   
   The data of each returned structure will contain the same data
   the user set with an epoll_ctl(2) (EPOLL_CTL_ADD,EPOLL_CTL_MOD)
   while the events member will contain the returned event bit field.
   
   event成员可以下面的事件类型进行位运算“|”组成：
   
       EPOLLIN
           相关文件可用于read(2)操作。
   
       EPOLLOUT
           相关文件可用于write(2)操作。
   
       EPOLLRDHUP（自Linux 2.6.17开始）
           流套接字对等封闭连接，或关闭写的一半连接。
           （这个标志对于编写简单的代码特别有用，在使用边缘触发监视时检测对等体关闭。）
   
       EPOLLPRI
           有可用于read(2)操作的紧急数据。
   
       EPOLLERR
           关联的文件描述符发生错误情况。
           epoll_wait(2)总是等待这个事件; 没有必要将它设置为事件。
   
       EPO阻塞LLHUP
           在相关的文件描述符上发生挂起。
           epoll_wait(2)总是等待这个事件; 没有必要将它设置为事件。
   
       EPOLLET
           设置相关文件描述符的边缘触发行为。
           epoll的默认行为是水平触发级别。
           有关Edge和Level Triggered事件分布架构的更多详细信息，请参阅epoll(7)。
   
       EPOLLONESHOT（自Linux 2.6.2起）
           设置相关文件描述符的单次行为。
           这意味着在用epoll_wait(2)提取事件之后，相关的文件描述符被内部禁用，并且epoll接口不会报告其他事件。
           用户必须使用EPOLL_CTL_MOD调用epoll_ctl()来重新构建新的事件掩码的文件描述符。
   ```

   ```
   成功时：epoll_wait()返回为所请求的I/O准备好的文件描述符数;
          如果在请求的超时毫秒期间没有文件描述符就绪，则返回0；
   错误时：epoll_wait()返回-1, 并且适当设置errno。
   ```

   

3. **epoll_ctl()**

   ```txt
   该系统调用对由文件描述符epfd引用的epoll实例执行控制操作。 
   它要求对目标文件描述符fd执行op操作。
   
   op参数的有效值为：
   
   EPOLL_CTL_ADD
       在文件描述符epfd引用的epoll实例上注册目标文件描述符fd，并将event关联内部文件链接到fd。
   
   EPOLL_CTL_MOD
       更改与目标文件描述符fd相关联的event事件。
   
   EPOLL_CTL_DEL
       从epfd引用的epoll实例中删除目标文件描述符fd。如果event设为NULL，则会被忽略。
   ```

   ```
   成功时：epoll_ctl()返回0；
   错误时：epoll_ctl()返回-1，并且适当地设置errno。
   ```

**epoll底层调用过程**：

```txt
(1)由epoll_create(2)创建的epoll实例，它返回引用epoll实例的文件描述符。
(2)通过epoll_ctl(2)注册对特定文件描述符的兴趣。
   当前在epoll实例上注册的文件描述符集合有时被称为epoll集。
(3)最后，实际的等待由epoll_wait(2)开始。
```

