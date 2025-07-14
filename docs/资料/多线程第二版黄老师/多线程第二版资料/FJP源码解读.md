```java
public ForkJoinPool() {
    // MAX_CAP      = 0x7fff  32767; 所以最大线程数。默认就是CPU核心数
    this(Math.min(MAX_CAP, Runtime.getRuntime().availableProcessors()),
         defaultForkJoinWorkerThreadFactory, null, false);
}
public ForkJoinPool(int parallelism,
                    ForkJoinWorkerThreadFactory factory, // 线程工厂
                    UncaughtExceptionHandler handler, // 线程异常处理器
                    boolean asyncMode) { // 执行模式：异步或者同步。默认是false
    this(checkParallelism(parallelism), // 非法参数校验
         checkFactory(factory),
         handler,
         asyncMode ? FIFO_QUEUE : LIFO_QUEUE, // 取任务的方式：base和top
         "ForkJoinPool-" + nextPoolId() + "-worker-");
    checkPermission();
}
// 最终构造器
private ForkJoinPool(int parallelism,
                     ForkJoinWorkerThreadFactory factory,
                     UncaughtExceptionHandler handler,
                     int mode,
                     String workerNamePrefix) {
    this.workerNamePrefix = workerNamePrefix;
    this.factory = factory;
    this.ueh = handler;
    this.config = (parallelism & SMASK) | mode; // SMASK = 0x0000 ffff  将config的低16位用于存放并行度
    // static final int MODE_MASK    = 0xffff << 16;  // 高16位保存mode
    // static final int LIFO_QUEUE   = 0; // 栈结构不需要占用位
    // static final int FIFO_QUEUE   = 1 << 16;
    long np = (long)(-parallelism); //以并行度64为例 AC 1111111111000000   TC： 1111111111000000
    this.ctl = ((np << AC_SHIFT) & AC_MASK) | ((np << TC_SHIFT) & TC_MASK);
}

// 将任务封装成FJT，因为FJP只执行FJT的任务
public ForkJoinTask<?> submit(Runnable task) {
        ForkJoinTask<?> job;
        if (task instanceof ForkJoinTask<?>)
            job = (ForkJoinTask<?>) task;
        else
            job = new ForkJoinTask.AdaptedRunnableAction(task);
        externalPush(job); // 核心提交任务方法
        return job；
}
final void externalPush(ForkJoinTask<?> task) {
      externalSubmit(task); // 核心方法
}
// 真正执行提交任务的方法
private void externalSubmit(ForkJoinTask<?> task) {
    int r;                                    // 随机数
    // 取随机数
    for (;;) { // 如果出现这种死循环，必然会有多个判断条件，多个执行分支
        WorkQueue[] ws; WorkQueue q; int rs, m, k;
        boolean move = false;
        if ((rs = runState) < 0) { // 判断线程池是否已经关闭
            tryTerminate(false, false);  
            throw new RejectedExecutionException();
        }
        else if ((rs & STARTED) == 0 || // STARTED状态位没有置位，所以需要初始化
                 ((ws = workQueues) == null || (m = ws.length - 1) < 0)) { // workQueues需要初始化
            // 注意：如果该分支不执行，但是由于||运算符的存在，这里ws和m变量已经初始化
            int ns = 0;
            rs = lockRunState();
            try {
                if ((rs & STARTED) == 0) { // 再次判断了一下STARTED状态位，为何？因为多线程操作，可能上面判断过后，已经被别的线程初始化了，所以没必要再进行CAS
                    U.compareAndSwapObject(this, STEALCOUNTER, null,
                                           new AtomicLong());
                    // p就是并行度：工作线程的数量
                    int p = config & SMASK; // ensure at least 4 slots
                    int n = (p > 1) ? p - 1 : 1; // n最小为1
                    // 就是取传入的数，最近的2的倍数
                    1 |= n >>> 1; n |= n >>> 2;  n |= n >>> 4;
                    n |= n >>> 8; n |= n >>> 16; n = (n + 1) << 1;
                    workQueues = new WorkQueue[n]; // 初始化workQueue队列（外部提交队列+工作窃取队列）
                    ns = STARTED; // 初始化成功，那么此时状态修改为STARTED状态
                }
            } finally {
                unlockRunState(rs, (rs & ~RSLOCK) | ns); // 最后释放锁
            }
        }
        // 在上一个分支执行完毕后，第二次循环将会到达这里。由于咱们的算法是用了一个全局队列:workQueues来存储两个队列：外部提交队列、内部工作队列（任务窃取队列），那么这时，是不是应该去找到我这个任务放在哪个外部提交队列里面。就是通过上面获取的随机种子r，来找到应该放在哪里？SQMASK = 1111110，所以由SQMASK的前面的1来限定长度，末尾的0来表明，外部提交队列一定在偶数位
        else if ((q = ws[k = r & m & SQMASK]) != null) {
            // 由于当前提交队列是外部提交队列，那么一定会有多线程共同操作，那么为了保证并发安全，那么这里需要上锁，也即对当前提交队列进行锁定
            if (q.qlock == 0 && U.compareAndSwapInt(q, QLOCK, 0, 1)) {
                ForkJoinTask<?>[] a = q.array; // 取提交队列的保存任务的数组array
                int s = q.top; // doug lea 大爷发现，从top往下放，类似于压栈过程，而栈顶指针就叫sp（stack pointer），所以简写为s
                boolean submitted = false; // initial submission or resizing
                try {                      // locked version of push
                    if (
                        (
                        a != null && // 当前任务数组已经初始化
                            a.length > s + 1 - q.base // 判断数组是否已经满了
                        ) 
                        ||
                        (a = q.growArray()) != null) { // 那么就初始化数组或者扩容
                        // 扩容之后，开始存放数据
                        int j = (((a.length - 1) & s) << ASHIFT) + ABASE; // j就是s也就是栈顶的绝对地址
                        U.putOrderedObject(a, j, task); // 放数据
                        U.putOrderedInt(q, QTOP, s + 1); // 对栈顶指针+1
                        // 为什么这里需要怎么写？注意：qtop和q.array没有volatile修饰
                        submitted = true;
                    }
                } finally {
                    U.compareAndSwapInt(q, QLOCK, 1, 0); // qlock是volatile的，由于volatile的特性，这个操作CAS出去，那么qlock线程可见，必然上面的task和qtop可见，且有序。
                }
                // 由于添加成功了，但是，没有工作线程，那么这时通过signalWork，创建工作线程并执行
                if (submitted) {
                    signalWork(ws, q);
                    return;
                }
            }
            move = true; // 由于当前线程无法获取初始计算的提交队列的锁，那么这时发生了线程竞争，那么设置move标志位，让线程在下一次循环的时候，重新计算随机数，让它寻找另外的队列。
        }
        else if (((rs = runState) & RSLOCK) == 0) { // 如果找到的这个wq没有被创建，那么创建他，但是，这里的RSLOCK的判断，在于，当没有别的线程持有RSLOCK的时候，才会进入。这是由于RSLOCK主管，runstate，可能有别的线程把状态改了，根本不需要再继续work了
            q = new WorkQueue(this, null); // 创建外部提交队列，由于ForkJoinWorkerThread FJWT为null，所以为外部提交队列
            q.hint = r; // r为什么保存为hint，r是随机数，通过r找到当前外部提交队列，处于WQS的索引下标
            q.config = k | SHARED_QUEUE; // SHARED_QUEUE = 1 << 31；这里就是将整形int的符号位置1，所以为负数，SHARED_QUEUE表明当前队列是共享队列（外部提交队列）。而k为当前wq处于wqs中的索引下标
            q.scanState = INACTIVE; // 由于当前wq并没有进行扫描任务，所以扫描状态位无效状态INACTIVE
            rs = lockRunState();           // 对wqs上锁操作：就是讲上面的队列放入到wqs的偶数位中
            if (rs > 0 &&  // 确保线程池处于运行状态
                (ws = workQueues) != null &&
                k < ws.length && ws[k] == null) // 由于可能两个线程同时进来操作，只有一个线程持有锁，那么只允许一个线程放创建的队列，但是这里需要注意的是：可能会有多个线程创建了WorkQueue，但是只有一个能成功
                ws[k] = q;     // 将wq放入全局队列wqs中
            unlockRunState(rs, rs & ~RSLOCK); // 解锁
        }
        else // 发生竞争时，让当前线程选取其他的wq来重试
            move = true;                 
        if (move)
            r = ThreadLocalRandom.advanceProbe(r); // 获取下一个不同的随机数
    }
}
// 上面的内容，都是初始化，放，but，执行线程没有啊，谁来执行这个放入到了外部提交队列中的任务。
final void signalWork(WorkQueue[] ws, WorkQueue q) {
    long c; int sp, i; WorkQueue v; Thread p;
    while ((c = ctl) < 0L) {                       // 符号位没有溢出：最高16位为AC，代表了工作活跃的线程数没有达到最大值
        if ((sp = (int)c) == 0) {                  // ctl的低32位代表了INACTIVE数。若此时sp=0，代表了没有空闲线程
            if ((c & ADD_WORKER) != 0L)            // ADD_WORKER = 0x0001L << (TC_SHIFT + 15) TC的最高位是不是0，若不是0，那么FJP中的工作线程代表了没有达到最大线程数
                tryAddWorker(c); // 尝试添加工作线程
            break;
        }
        // 此时，代表了空闲线程不为null
        if (ws == null)                            // 此时FJP的状态为NOT STARTED，TERMINATED
            break;
        if (ws.length <= (i = sp & SMASK))         // SMASK = 0xffff 取sp的低16位 TERMINATED
            break;
        if ((v = ws[i]) == null)                   // ctl低32位的低16位是不是存放了INACTIVE线程在wqs的下标i  TERMINATING
            break;
        int vs = (sp + SS_SEQ) & ~INACTIVE;        // SS_SEQ = 1 << 16; ctl低32位的高16位是不是存放了版本计数 version count   INACTIVE= 1 << 31;
        int d = sp - v.scanState;                  // screen CAS
        long nc = (UC_MASK & (c + AC_UNIT)) | // 把获取到的INACTIVE的线程，也即空闲线程唤醒，那么唤醒后，是不是应该对AC + 1（加1操作(c + AC_UNIT)），UC_MASK为高32位1，低32位0，所以(UC_MASK & (c + AC_UNIT))代表了，保留ctl的高32位值，也即AC+1和TC值
            (SP_MASK & v.stackPred); // SP_MASK为高32位0，低32位1，所以保留了低32位的值，v.stackPred 代表了一个出栈操作，让低32位的低16位更新为唤醒线程的下一个线程
        // 此时的nc就是计算好的下一个ctl，next ctl -> nc
        if (d == 0 && U.compareAndSwapLong(this, CTL, c, nc)) { // CAS 替换CTL
            v.scanState = vs;           // 记录版本信息放入scanState，此时为正数
            if ((p = v.parker) != null) // 唤醒工作线程
                U.unpark(p);
            break;
        }
        if (q != null && q.base == q.top)    // 队列为空，直接退出。由于队列是多线程并发的，所以有可能放入其中的任务已经被其他线程获取，所以此时队列为空
            break;
    }
}
// 尝试添加工作线程
private void tryAddWorker(long c) {
    boolean add = false;
    do {
        long nc = ((AC_MASK & (c + AC_UNIT)) | // active count +1 活跃线程数加1，此时只保留了高32位的高16位信息
                   (TC_MASK & (c + TC_UNIT))); // total count +1 总线程数加1，此时只保留了高32位的低16位信息
        // 此时nc为next ctl，也即活跃线程数+1，总线程数+1
        if (ctl == c) { // ctl没有被其他线程改变
            int rs, stop;           
            // 上锁并检查FJP的状态是否为STOP
            if ((stop = (rs = lockRunState()) & STOP) == 0) 
                add = U.compareAndSwapLong(this, CTL, c, nc); // 更新ctl的值。add表明是否添加成功
            unlockRunState(rs, rs & ~RSLOCK); // 解锁
            if (stop != 0) // 线程池已经停止
                break;
            if (add) { // 如果ac和tc添加1成功，也即nc替换成功，那么创建工作线程
                createWorker();
                break;
            }
        }
    } while (((c = ctl) & ADD_WORKER) != 0L  // 没有达到最大线程数
             && (int)c == 0); // 低32位0，如果有空闲线程，你添加他干甚？
}
// 创建工作线程
private boolean createWorker() {
    ForkJoinWorkerThreadFactory fac = factory;
    Throwable ex = null;
    ForkJoinWorkerThread wt = null;
    try {
        if (fac != null && (wt = fac.newThread(this)) != null) { // 从线程工厂中，创建线程
            wt.start(); // 并启动线程
            return true;
        }
    } catch (Throwable rex) {
        ex = rex;
    }
    deregisterWorker(wt, ex); // 如果创建出现异常，将ctl前面加的1回滚
    return false;
}
// 默认线程工厂
static final class DefaultForkJoinWorkerThreadFactory
    implements ForkJoinWorkerThreadFactory {
    public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        return new ForkJoinWorkerThread(pool); // 直接new ForkJoinWorkerThread
    }
}
// FJWT的构造器
protected ForkJoinWorkerThread(ForkJoinPool pool) {
    super("aForkJoinWorkerThread");
    this.pool = pool; // 保存对外的FJP的引用 
    this.workQueue = pool.registerWorker(this); // 将自己注册到FJP中，其实就是保存到FJP的奇数位中
}
// 将自己注册到FJP中
final WorkQueue registerWorker(ForkJoinWorkerThread wt) {
    UncaughtExceptionHandler handler;
    wt.setDaemon(true);                           //  FJWT的守护状态为守护线程
    if ((handler = ueh) != null) // 如果设置了线程的异常处理器，那么设置
        wt.setUncaughtExceptionHandler(handler);
    WorkQueue w = new WorkQueue(this, wt); // 创建工作线程，注意：创建外部提交队列时： WorkQueue w = new WorkQueue(this, null)
    int i = 0;                                    // 创建的工作队列所保存在wqs的索引下标
    int mode = config & MODE_MASK;                // 取设置的工作模式：FIFO、LIFO
    int rs = lockRunState(); // 上锁
    try {
        WorkQueue[] ws; int n;                    
        if ((ws = workQueues) != null && (n = ws.length) > 0) { // 日常判空操作
            int s = indexSeed += SEED_INCREMENT;  // indexSeed = 0 SEED_INCREMENT = 0x9e3779b9 ，减少hash碰撞
            int m = n - 1; // wqs长度-1，用于取模运算
            i = ((s << 1) | 1) & m;               // 找存放w的下标
            if (ws[i] != null) {                  // 发生了碰撞
                int probes = 0;                   // step by approx half n
                int step = (n <= 4) ? 2 : ((n >>> 1) & EVENMASK) + 2; // 发生碰撞二次寻址 EVENMASK     = 0xfffe;
                while (ws[i = (i + step) & m] != null) { // step保证为偶数，一个奇数+偶数一定等于奇数
                    if (++probes >= n) { // 寻址达到了极限，那么扩容
                        workQueues = ws = Arrays.copyOf(ws, n <<= 1); // 扩容容量为2倍
                        m = n - 1;
                        probes = 0;
                    }
                }
            }
            w.hint = s;                           // s作为随机数保存在wq的hint中
            w.config = i | mode;                  // 保存索引下标 + 模式
            w.scanState = i;                      // scanState为volatile，此时对它进行写操作，ss写成功，上面的变量一定可见，且不会和下面的ws[i]赋值发生重排序。注意这里的scanState就变成了odd，也即奇数，所以要开始扫描获取任务并执行啦
            ws[i] = w; // 放入全局队列中
        }
    } finally {
        unlockRunState(rs, rs & ~RSLOCK); // 解锁
    }
    wt.setName(workerNamePrefix.concat(Integer.toString(i >>> 1)));
    return w;
}


// CAS上把锁
private int lockRunState() {
    int rs;
    return ((((rs = runState) & RSLOCK) != 0 ||
             !U.compareAndSwapInt(this, RUNSTATE, rs, rs |= RSLOCK)) ?
            awaitRunStateLock() : rs);
}
// 等待获取锁
private int awaitRunStateLock() {
    Object lock;
    boolean wasInterrupted = false;
    for (int spins = SPINS, r = 0, rs, ns;;) {
        // 锁已经被释放，那么可以去CAS竞争锁
        if (((rs = runState) & RSLOCK) == 0) {
            if (U.compareAndSwapInt(this, RUNSTATE, rs, ns = rs | RSLOCK)) {
                if (wasInterrupted) {
                    try {
                        Thread.currentThread().interrupt();
                    } catch (SecurityException ignore) {
                    }
                }
                return ns;
            }
        }
        else if (r == 0) // 初始化随机数
            r = ThreadLocalRandom.nextSecondarySeed();
        else if (spins > 0) { // 随机，减少自旋次数
            r ^= r << 6; r ^= r >>> 21; r ^= r << 7; // 异或随机数
            if (r >= 0)
                --spins;
        }
        else if ((rs & STARTED) == 0 || (lock = stealCounter) == null)
            Thread.yield();   // 由于当前rs的STARTED状态位为0，代表了，当前FJP没有在运行了，那么没有必要再去睡眠了，因为这个状态维持时间会非常短
        // 光是睡眠不行，需要有人唤醒，所以这里必须置位 RSIGNAL 唤醒位，提示另外的线程需要唤醒它
        else if (U.compareAndSwapInt(this, RUNSTATE, rs, rs | RSIGNAL)) {
            synchronized (lock) {
                if ((runState & RSIGNAL) != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ie) {
                        if (!(Thread.currentThread() instanceof
                              ForkJoinWorkerThread))
                            wasInterrupted = true;
                    }
                }
                else
                    lock.notifyAll();
            }
        }
    }
}
// 解锁
private void unlockRunState(int oldRunState, int newRunState) {
    if (!U.compareAndSwapInt(this, RUNSTATE, oldRunState, newRunState)) {
        Object lock = stealCounter;
        runState = newRunState;              // clears RSIGNAL bit
        if (lock != null)
            synchronized (lock) { lock.notifyAll(); }
    }
}
```

