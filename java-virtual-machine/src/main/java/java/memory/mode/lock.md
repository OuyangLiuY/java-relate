锁的释放 - 获取建立的 happens before 关系:
    锁是 java 并发编程中最重要的同步机制。锁除了让临界区互斥执行外，还可以让释放锁的线程向获取同一个锁的线程发送消息。
    
获取的示例代码：
    class MonitorExample {
        int a = 0;
        public synchronized void writer() {  //1
            a++;                             //2
        }                                    //3
        public synchronized void reader() {  //4
            int i = a;                       //5
            ……
        }                                    //6
    }
    假设线程 A 执行 writer() 方法，随后线程 B 执行 reader() 方法。根据 happens before 规则，这个过程包含的 happens before 关系可以分为两类：
    1.根据程序次序规则，1 happens before 2, 2 happens before 3; 4 happens before 5, 5 happens before 6。
    2.根据监视器锁规则，3 happens before 4。
    3.根据 happens before 的传递性，2 happens before 5。
锁释放和获取的内存语义:
    当A线程释放锁时，JMM会把该线程对应的本地内存中的共享变量刷新到主内存中。
    当B线程获取锁时，JMM会把该线程对应的本地内存置为无效。从而使得被监视器保护的临界区代码必须要从主内存中去读取共享变量。
    对比锁释放 - 获取的内存语义与 volatile 写 - 读的内存语义，可以看出：
        锁释放与 volatile 写有相同的内存语义；锁获取与 volatile 读有相同的内存语义。
    总结：
        1.线程 A 释放一个锁，实质上是线程 A 向接下来将要获取这个锁的某个线程发出了（线程 A 对共享变量所做修改的）消息。
        2.线程 B 获取一个锁，实质上是线程 B 接收了之前某个线程发出的（在释放这个锁之前对共享变量所做修改的）消息。
        3.线程 A 释放锁，随后线程 B 获取这个锁，这个过程实质上是线程 A 通过主内存向线程 B 发送消息。
锁内存语义的实现：
    借助 ReentrantLock 的源代码，来分析锁内存语义的具体实现机制。
    class ReentrantLockExample {
    int a = 0;
    ReentrantLock lock = new ReentrantLock();
    public void writer() {
        lock.lock();         // 获取锁 
        try {
            a++;
        } finally {
            lock.unlock();  // 释放锁 
        }
    }
    public void reader () {
        lock.lock();        // 获取锁 
        try {
            int i = a;
            ……
        } finally {
            lock.unlock();  // 释放锁 
        }
    }
    }
    在 ReentrantLock 中，调用 lock() 方法获取锁；调用 unlock() 方法释放锁。
    ReentrantLock 的实现依赖于 java 同步器框架 AbstractQueuedSynchronizer（本文简称之为 AQS）。
    AQS 使用一个整型的 volatile 变量（命名为 state）来维护同步状态，马上我们会看到，这个 volatile 变量是 ReentrantLock 内存语义实现的关键。
ReentrantLock 分为公平锁和非公平锁。 
    公平锁：
    使用公平锁时，加锁方法 lock() 的方法调用轨迹如下：
        1.ReentrantLock : lock()
        2.FairSync : lock()
        3.AbstractQueuedSynchronizer : acquire(int arg)
        4.ReentrantLock : tryAcquire(int acquires)
        在第 4 步真正开始加锁，下面是该方法的源代码：
            protected final boolean tryAcquire(int acquires) {
                final Thread current = Thread.currentThread();
                int c = getState();   // 获取锁的开始，首先读 volatile 变量 state
                if (c == 0) {
                    if (isFirst(current) &&
                        compareAndSetState(0, acquires)) {
                        setExclusiveOwnerThread(current);
                        return true;
                    }
                }
                else if (current == getExclusiveOwnerThread()) {
                    int nextc = c + acquires;
                    if (nextc < 0)  
                        throw new Error("Maximum lock count exceeded");
                    setState(nextc);
                    return true;
                }
                return false;
            }
    从上面源代码中我们可以看出，加锁方法首先读 volatile 变量 state。
    在使用公平锁时，解锁方法 unlock() 的方法调用轨迹如下：
        1.ReentrantLock : unlock()
        2.AbstractQueuedSynchronizer : release(int arg)
        3.Sync : tryRelease(int releases)
        在第 3 步真正开始释放锁，下面是该方法的源代码：
            protected final boolean tryRelease(int releases) {
                int c = getState() - releases;
                if (Thread.currentThread() != getExclusiveOwnerThread())
                    throw new IllegalMonitorStateException();
                boolean free = false;
                if (c == 0) {
                    free = true;
                    setExclusiveOwnerThread(null);
                }
                setState(c);           // 释放锁的最后，写 volatile 变量 state
                return free;
            }
        从上面的源代码我们可以看出，在释放锁的最后写 volatile 变量 state。
        公平锁在释放锁的最后写 volatile 变量 state；在获取锁时首先读这个 volatile 变量。
        根据 volatile 的 happens-before 规则，释放锁的线程在写 volatile 变量之前可见的共享变量，
        在获取锁的线程读取同一个 volatile 变量后将立即变的对获取锁的线程可见。
    非公平锁的获取:(因非公平锁的释放和公平锁完全一样，所以这里仅仅分析非公平锁的获取。)
        1.ReentrantLock : lock()
        2.NonfairSync : lock()
        3.AbstractQueuedSynchronizer : compareAndSetState(int expect, int update)
        在第 3 步真正开始加锁，下面是该方法的源代码：
            protected final boolean compareAndSetState(int expect, int update) {
                return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
            }
        该方法以原子操作的方式更新 state 变量，本文把 java 的 compareAndSet() 方法调用简称为 CAS。
        JDK 文档对该方法的说明如下：如果当前状态值等于预期值，则以原子方式将同步状态设置为给定的更新值。此操作具有 volatile 读和写的内存语义。
        这里我们分别从编译器和处理器的角度来分析,CAS 如何同时具有 volatile 读和 volatile 写的内存语义?
        前文我们提到过，编译器不会对 volatile 读与 volatile 读后面的任意内存操作重排序；
        编译器不会对 volatile 写与 volatile 写前面的任意内存操作重排序。
        组合这两个条件，意味着为了同时实现 volatile 读和 volatile 写的内存语义，
        编译器不能对 CAS 与 CAS 前面和后面的任意内存操作重排序。
        下面我们来分析在常见的 intel x86 处理器中，CAS 是如何同时具有 volatile 读和 volatile 写的内存语义的。
        下面是 sun.misc.Unsafe 类的 compareAndSwapInt() 方法的源代码：
            public final native boolean compareAndSwapInt(Object o, long offset,int expected,int x);   
        可以看到这是个本地方法调用。这个本地方法在 openjdk 中依次调用的 c++ 代码为：unsafe.cpp，atomic.cpp 和 atomicwindowsx86.inline.hpp。这个本地方法的最终实现在 openjdk 的如下位置：openjdk-7-fcs-src-b147-27jun2011\openjdk\hotspot\src\oscpu\windowsx86\vm\ atomicwindowsx86.inline.hpp（对应于 windows 操作系统，X86 处理器）。
        下面是对应于 intel x86 处理器的源代码的片段：
            // Adding a lock prefix to an instruction on MP machine
            // VC++ doesn't like the lock prefix to be on a single line
            // so we can't insert a label after the lock prefix.
            // By emitting a lock prefix, we can define a label after it.
            #define LOCK_IF_MP(mp) __asm cmp mp, 0  \
                                   __asm je L0      \
                                   __asm _emit 0xF0 \
                                   __asm L0:
            inline jint     Atomic::cmpxchg    (jint     exchange_value, volatile jint*     dest, jint     compare_value) {
              // alternative for InterlockedCompareExchange
              int mp = os::is_MP();
              __asm {
                mov edx, dest
                mov ecx, exchange_value
                mov eax, compare_value
                LOCK_IF_MP(mp)
                cmpxchg dword ptr [edx], ecx
              }
            }
        如上面源代码所示，程序会根据当前处理器的类型来决定是否为 cmpxchg 指令添加 lock 前缀。
        如果程序是在多处理器上运行，就为 cmpxchg 指令加上 lock 前缀（lock cmpxchg）。
        反之，如果程序是在单处理器上运行，就省略 lock 前缀（单处理器自身会维护单处理器内的顺序一致性，不需要 lock 前缀提供的内存屏障效果）。
        intel 的手册对 lock 前缀的说明如下：
            1.确保对内存的读 - 改 - 写操作原子执行。在 Pentium 及 Pentium 之前的处理器中，带有 lock 前缀的指令在执行期间会锁住总线，
            使得其他处理器暂时无法通过总线访问内存。很显然，这会带来昂贵的开销。
            从 Pentium 4，Intel Xeon 及 P6 处理器开始，intel 在原有总线锁的基础上做了一个很有意义的优化：
            如果要访问的内存区域（area of memory）在 lock 前缀指令执行期间已经在处理器内部的缓存中被锁定（即包含该内存区域的缓存行当前处于独占或以修改状态），
            并且该内存区域被完全包含在单个缓存行（cache line）中，那么处理器将直接执行该指令。
            由于在指令执行期间该缓存行会一直被锁定，其它处理器无法读 / 写该指令要访问的内存区域，因此能保证指令执行的原子性。
            这个操作过程叫做缓存锁定（cache locking），缓存锁定将大大降低 lock 前缀指令的执行开销，
            但是当多处理器之间的竞争程度很高或者指令访问的内存地址未对齐时，仍然会锁住总线。
            2.禁止该指令与之前和之后的读和写指令重排序。
            3.把写缓冲区中的所有数据刷新到内存中。
        上面的第 2 点和第 3 点所具有的内存屏障效果，足以同时实现 volatile 读和 volatile 写的内存语义。
        公平锁和非公平锁的内存语义做个总结：
            1.公平锁和非公平锁释放时，最后都要写一个 volatile 变量 state。
            2.公平锁获取时，首先会去读这个 volatile 变量。
            3.非公平锁获取时，首先会用 CAS 更新这个 volatile 变量, 这个操作同时具有 volatile 读和 volatile 写的内存语义。
        从本文对 ReentrantLock 的分析可以看出，锁释放 - 获取的内存语义的实现至少有下面两种方式：
            1.利用 volatile 变量的写 - 读所具有的内存语义。
            2.利用 CAS 所附带的 volatile 读和 volatile 写的内存语义。

concurrent 包的实现：
     由于 java 的 CAS 同时具有 volatile 读和 volatile 写的内存语义，因此 Java 线程之间的通信现在有了下面四种方式：
        1. A 线程写 volatile 变量，随后 B 线程读这个 volatile 变量。
        2. A 线程写 volatile 变量，随后 B 线程用 CAS 更新这个 volatile 变量。
        3. A 线程用 CAS 更新一个 volatile 变量，随后 B 线程用 CAS 更新这个 volatile 变量。
        4. A 线程用 CAS 更新一个 volatile 变量，随后 B 线程读这个 volatile 变量。
     Java 的 CAS 会使用现代处理器上提供的高效机器级别原子指令，这些原子指令以原子方式对内存执行读 - 改 - 写操作，这是在多处理器中实现同步的关键（从本质上来说，能够支持原子性读 - 改 - 写指令的计算机器，是顺序计算图灵机的异步等价机器，因此任何现代的多处理器都会去支持某种能对内存执行原子性读 - 改 - 写操作的原子指令）。
     同时，volatile 变量的读 / 写和 CAS 可以实现线程之间的通信。把这些特性整合在一起，就形成了整个 concurrent 包得以实现的基石。
     如果我们仔细分析 concurrent 包的源代码实现，会发现一个通用化的实现模式：
        1. 首先，声明共享变量为 volatile；
        2. 然后，使用 CAS 的原子条件更新来实现线程之间的同步；
        3. 同时，配合以 volatile 的读 / 写和 CAS 所具有的 volatile 读和写的内存语义来实现线程之间的通信。
        原文地址：https://www.infoq.cn/article/java-memory-model-5