#### volatile 变量自身具有下列特性:

​    1> 可见性 ： 对一个 volatile 变量的读，总是能看到（任意线程）对这个 volatile 变量最后的写入。
​    2> 原子性 ： 对任意单个 volatile 变量的读 / 写具有原子性，但类似于 volatile++ 这种复合操作不具有原子性。
​    

#### volatile 写 - 读的内存语义：

   当写一个 volatile 变量时，JMM 会把该线程对应的本地内存中的共享变量刷新到主内存。
   当读一个 volatile 变量时，JMM 会把该线程对应的本地内存置为无效。线程接下来将从主内存中读取共享变量。
   总结：
        线程 A 写一个 volatile 变量，实质上是线程 A 向接下来将要读这个 volatile 变量的某个线程发出了（其对共享变量所在修改的）消息。
        线程 B 读一个 volatile 变量，实质上是线程 B 接收了之前某个线程发出的（在写这个 volatile 变量之前对共享变量所做修改的）消息。
        线程 A 写一个 volatile 变量，随后线程 B 读这个 volatile 变量，这个过程实质上是线程 A 通过主内存向线程 B 发送消息。
      

```java
class VolatileBarrierExample {
        int a;
        volatile int v1 = 1;
        volatile int v2 = 2;
        void readAndWrite() {
            int i = v1;           // 第一个 volatile 读 
            int j = v2;           // 第二个 volatile 读 
            a = i + j;            // 普通写 
            v1 = i + 1;          // 第一个 volatile 写 
            v2 = j * 2;          // 第二个 volatile 写 
        }
        …                    // 其他方法 
    }       
```
针对 readAndWrite() 方法，编译器在生成字节码时可以做如下的优化：
        第一个volatile读
        [LoadLoad屏障] : 禁止上面的volatile读和下面的volatile读重排序。
        []:这里省略了LoadStore屏障，因为下面的菩提写根本不可能越过上面的volatile读。
        第二个volatile读
        [] : 这里省略了LoadLoad屏障，因为下面根本没有普通读操作。
        [LoadStore屏障] ：禁止下面的普通写和上面的volatile读重排序
        普通写
        [StoreStore屏障] ：禁止上面的普通写和下面的volatile写重排序
        第一个volatile写
        [] 省略StoreLoad屏障，仅仅插入StoreStore屏障即可，因为下面跟着一个volatile写。
        [StoreStore屏障] ：禁止上面的volatile写于下面的volatile写重排序
        第二个volatile写
        [StoreLoad屏障] ： 防止volatile写 于后面可能有的volatile读/写重排序
    注意：
        最后的 StoreLoad 屏障不能省略。因为第二个 volatile 写之后，方法立即 return。
        此时编译器可能无法准确断定后面是否会有 volatile 读或写，为了安全起见，编译器常常会在这里插入一个 StoreLoad 屏障。

#### volatile使用场景：

​    1.根据经验，volatile最适合使用的地方是一个线程写，其他线程读的场合，如果有多个线程并发写操作，仍然需要使用锁或者线程安全的容器或者原子变量来代替。 
​    2.状态标志：也许实现 volatile 变量的规范使用仅仅是使用一个布尔状态标志，用于指示发生了一个重要的一次性事件，例如完成初始化或请求停机 

```java
	volatile boolean shutdownRequested; 
    ... 
    public void shutdown() {     shutdownRequested = true; } 
    public void doWork() {     while (!shutdownRequested) {         // do stuff    }}
```



​    线程1执行doWork()的过程中，可能有另外的线程2调用了shutdown，所以boolean变量必须是volatile。  
​    3.一次性安全发布（双重检查锁定单例模式的实现）

```java
	//注意volatile！！！！！！！！！！！！！！！！！ 
      private volatile static Singleton instance;     
      public static Singleton getInstance(){       
        //第一次null检查         
        if(instance == null){       
                synchronized(Singleton.class) {    //1                 
                //第二次null检查                   
                    if(instance == null){          //2                  
                    	instance = new Singleton();    //3              
                    }          
                }               
            }      
         return instance;      
      }
```



​      在缺乏同步的情况下，可能会遇到某个对象引用的更新值（由另一个线程写入）和该对象状态的旧值同时存在。
​      这就是造成著名的双重检查锁定（double-checked-locking）问题的根源，其中对象引用在没有同步的情况下进行读操作，
​      产生的问题是您可能会看到一个更新的引用，但是仍然会通过该引用看到不完全构造的对象。
​      如果不用volatile，则因为内存模型允许所谓的“无序写入”，可能导致失败。——某个线程可能会获得一个未完全初始化的实例。 
​      考察上述代码中的 //3 行。此行代码创建了一个 Singleton 对象并初始化变量 instance 来引用此对象。这行代码的问题是：在Singleton 构造函数体执行之前，变量instance 可能成为非 null 的！
​      什么？这一说法可能让您始料未及，但事实确实如此。  
​       1.线程 1 进入 getInstance() 方法。    
​       2.由于 instance 为 null，线程 1 在 //1 处进入synchronized 块。    
​       3.线程 1 前进到 //3 处，但在构造函数执行之前，使实例成为非null。    
​       4.线程 1 被线程 2 预占。    
​       5.线程 2 检查实例是否为 null。因为实例不为 null，线程 2 将instance 引用返回，返回一个构造完整但部分初始化了的Singleton 对象。    
​       6.线程 2 被线程 1 预占。    
​       7.线程 1 通过运行 Singleton 对象的构造函数并将引用返回给它，来完成对该对象的初始化。
   4.独立观察
​    安全使用 volatile 的另一种简单模式是：定期 “发布” 观察结果供程序内部使用。 
​        1.【例如】假设有一种环境传感器能够感觉环境温度。一个后台线程可能会每隔几秒读取一次该传感器，并更新包含当前文档的 volatile 变量。然后，其他线程可以读取这个变量，从而随时能够看到最新的温度值。    
​    使用该模式的另一种应用程序就是收集程序的统计信息。
​        2.例】如下代码展示了身份验证机制如何记忆最近一次登录的用户的名字。将反复使用lastUser 引用来发布值，以供程序的其他部分使用。    
​     5.“volatile bean” 模式
​        volatile bean 模式的基本原理是：很多框架为易变数据的持有者（例如 HttpSession）提供了容器，但是放入这些容器中的对象必须是线程安全的。
​        在 volatile bean 模式中，JavaBean 的所有数据成员都是 volatile 类型的，并且 getter 和 setter 方法必须非常普通——即不包含约束
​   

参考链接地址：https://www.infoq.cn/article/java-memory-model-4        

​        