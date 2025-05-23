Base:
    1>
    2> 所有实例域、静态域和数组元素存储在堆内存中，堆中数据线程共享。局部变量、方法参数和异常处理参数是线程私有，不存在内存可见性问题。
    3> java线程之间的通信由java内存模型（JMM）控制，JMM决定一个线程对共享变量的写入何时对另外一个线程可见。
    4> JMM定义了线程和主内存之间的抽象关系：线程之间的共享变量存储在住内存中，每个线程都有一个私有的我本地内存，本地内存中存储了该线程以读/写共享变量的副本。
        本地内存是JMM的一个抽象概念，并不真实存在。他涵盖了缓存，写缓冲区，寄存器以及其他硬件和编译器优化。


volatile：是什么？实现原理是什么？使用场景？遇到什么问题？怎么解决的？让你自己设计的话思路是什么？

重排序:
    1> 编译器优化的重排：
        编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序。
    2> 指令级别并行的重排序：
        现代处理器采用了指令级并行技术来将多条指令重叠执行。如果不存在数据依赖性，处理器可以改变语句对应机器指令的执行顺序。
    3> 内存系统的重排序：
        由于处理器使用缓存和读/写缓冲区，这使得加载和存储操作看上去可能是乱序执行。

JMM 属于语言级的内存模型，它确保在不同的编译器和不同的处理器平台之上，通过禁止特定类型的编译器重排序和处理器重排序，为程序员提供一致的内存可见性保证。
Happens-before
   1> 程序顺序规则：一个线程中的每个操作，happens-before 于该线程中的任意后续操作。
   2>监视器锁规则：对一个监视器锁的解锁，happens-before于随后对这个监视器锁的加锁。
   3> volatile变量规则：对一个volatile域的写，happens-before与任意后续对着干volatile域的读。
   4> 传递性：如果A happens-before B,且B happens-before C，那么A happens-before C。
