# FutureTask全解

```java
// 特别注意：FutureTask如果不是在线程池中运行，那么调用cancel方法可能会导致业务线程，在后面执行业务的时候响应了不属于他自己的中断，因为这个中断是被调用线程由于标志FutureTask线程把任务执行完闭，而不是用于中断业务的执行
public class FutureTask<V> implements RunnableFuture<V> {
    // 由于FutureTask实现了RunnableFuture，RunnableFuture继承了Runnable，而线程池只执行Runnable的run方法，所以run方法，是FutureTask放入线程，执行的第一个方法
    public void run() {
        // 状态必须是NEW新建状态，且成功的将其runner修改为当前线程，代表了什么？代表了FT就保留了当前正在执行他的线程，所以我们就可以通过runner对象，在cancel中中断线程
        if (state != NEW ||
            !UNSAFE.compareAndSwapObject(this, runnerOffset,
                                         null, Thread.currentThread()))
            return;
        try {
            Callable<V> c = callable;
            // 查看内部执行的执行体，也即用户定义的函数callable对象是否为空，且当前状态是否已经被改变。也即执行前，状态必须是NEW新建状态
            if (c != null && state == NEW) {
                V result;
                boolean ran;
                try {
                    // 执行用户定义函数
                    result = c.call();
                    // 执行成功
                    ran = true;
                } catch (Throwable ex) {
                    // 捕捉用户定义的函数执行异常结果
                    result = null;
                    // 标志执行失败
                    ran = false;
                    // 设置执行失败的结果outcome
                    setException(ex);
                }
                if (ran)
                    // 执行成功，那么设置正常执行结果outcome
                    set(result);
            }
        } finally {
            // 执行完毕后，不持有thread的引用
            runner = null;
            // 判断状态是否被中断，如果是中断处理，那么调用handlePossibleCancellationInterrupt处理中断信息
            int s = state; // 保留当前state的快照值
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
    }
    
    // 当FutureTask正在被线程执行的时候，那么别的线程调用Cancel方法且指定了中断线程时，将会调用该方法
    private void handlePossibleCancellationInterrupt(int s) {
        // 如果快照状态为INTERRUPTING，那么循环等待它变为INTERRUPTED
        if (s == INTERRUPTING)
            while (state == INTERRUPTING)
                Thread.yield(); // 使用线程让步来等待，让出当前执行的CPU资源
    }
    // 线程执行体正常执行完成，那么调用该函数设置FutureTask正常完成，请参考setException的实现，一模一样，只是最终状态不一样而已
    protected void set(V v) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = v;
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL); // final state
            finishCompletion();
        }
    }

    // 当任务执行时发生了异常，那么调用该方法，将FutureTask变为异常完成状态。直接给出结论：在框架设计或者源码编写的时候，只要涉及到高并发，90%在做状态转换时，会出现中间状态。例如：A->B，A->中间状态->B。
    protected void setException(Throwable t) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = t;
            // 不保证可见性，但是保证不会发生重排序，也即不会重排序到上一行代码中
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL);
            finishCompletion();
        }
    }

    // 完成整个FutureTask的执行
    private void finishCompletion() {
        for (WaitNode q; (q = waiters) != null;) {
            // 如果有别的线程等待当前任务完成，那么将他们唤醒。算法：单向链表遍历。通过CAS操作，保证了可见性，而上面的ordered保证了写入的时候顺序写入。（注意：站在JMM模型上，这个结论是对的，站在底层CPU级别这是错误的理论，但庆幸是，面试官连这个都不知道，就算知道也仅此而已）
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                for (;;) {
                    // 等待线程不为空，那么唤醒，调用LockSupport.unpark唤醒
                    Thread t = q.thread;
                    if (t != null) {
                        q.thread = null;
                        LockSupport.unpark(t);
                    }
                    WaitNode next = q.next;
                    if (next == null)
                        break;
                    q.next = null; // 断开保留下一个任务的链，帮助GC(Help GC)
                    q = next;
                }
                break;
            }
        }
        // 此时，所有等待任务完成的线程全部都已经唤醒了。回调子类的钩子函数。
        done();
        // 任务完成了，必定不需要持有执行体了，所以置空
        callable = null;        
    }

    // 外部线程可以通过该方法的调用，取消任务的执行，mayInterruptIfRunning指明是否中断调用线程。此时，任务可能处于哪些状态：1、没有被执行 2、正在执行中 3、已经执行完成
    public boolean cancel(boolean mayInterruptIfRunning) {
        // 如果当前任务已经完成，那么返回false，此时可能正在完成中，可能已经完成
        if (!(state == NEW &&
              UNSAFE.compareAndSwapInt(this, stateOffset, NEW,
                                       mayInterruptIfRunning ? INTERRUPTING : CANCELLED)))
            return false;
        try {    // 如果指定了中断线程
            if (mayInterruptIfRunning) {
                try {
                    Thread t = runner;
                    if (t != null)
                        t.interrupt();
                } finally { // 最终将状态置为中断结束
                    UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED);
                }
            }
        } finally {
            // 最终还是调用完成任务并唤醒等待线程
            finishCompletion();
        }
        return true;
    }
}
```

