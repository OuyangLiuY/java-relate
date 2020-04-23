

linkedBlockingQueue  - > 
arrayBlockingQueue
delayquue ->priority
transferqueue
synblockingQueue


1.CPU密集型:线程数=CPU核数+1(+1是防止任务暂停带来影响，多出来的1个线程，使得可以充分利用空闲时间) N+1
    
2.I/O密集型:线程数=(1+(I/O耗时/CPU耗时))*核数  
    线程数 = N(cpu核数)*(1+wt(线程等待时间)/st(线程运行时间))
    ：系统大量的时间用来处理I/O交互的时候，

线程的状态：
    在线程池中的：等待锁，等待
    5中状态：running shutdown(停止，等待执行完毕(已经提交的并且正在执行的任务)之后就停止，不可以接收新任务) shutdownnow(立即停止所以的执行)  tidying(活动线程) terminated(结束)
Executors.newFixedThreadPool(5);// 创建一个固定(Fixed)大小为5的线程池
Executors.newSingleThreadExecutor();// 创建一个只有一个线程执行的任务队列。
Executors.newScheduledThreadPool(4);// 创建固定大小的线程用来每间隔多少时间来执行。
Executors.newWorkStealingPool();// 创建一个根据cpu核心数大小容量的任务队列 stealing(剽窃，偷)。
守护线程：(如果主线程结束)，后台执行。
ForkJoin -> 
interface Future
FutureTask      //callable  没有阻塞等待结果的，所以需要Future来接受这个callable返回的值。   
Executors.newCachedThreadPool(); //每次来一个线程就去创建一个新线程，线程可复用。默认60s线程没有被用到就会被回收。


线程：
线程模型分类：
    1.用户级线程：（ULT）
        用户程序实现，不依赖操作系统核心，应用提供创建、同步、调度和管理线程的函数来控制用户线程。
        不需要用户态/内核态切换，速度快。内核对ULT无感知，线程阻塞则进程阻塞
    2.内核级线程：（KLT）
        操作系统的内核创建的，使用内核保持线程的状态和上下文
 
 
多线程设计模式：
fork/join
自己写一个线程池？

AQS---juc？
synchronized是jvm内部实现的，
LOCK是JDK实现的，可以看到源码。
工作中如何选用锁？
    sync：方便简单？----优化好---不公平锁
    lock：手动释放锁 ---- 高级功能 -- 可以设置是否是公平锁

wait -> 线程放入waitSet中
notifi？ waitSet中的线程随机拿出 一个放入到等待队列中，  
notifiall？   
 
JMM   
happens before
akka
Docker -- k8s
netty 
spring 
eureka