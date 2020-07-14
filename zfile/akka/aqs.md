
AQS:
https://note.youdao.com/ynoteshare1/index.html?id=59cc299252a906c899d65198ab366a0f&type=note
ReentrantReadLock();
lock();
unlock();

 读写锁:
     ReentrantReadWriterLock();
     
java编译器、内存做了优化，所以才会可见性
MESI:     

（java内存模型）JMM
屏蔽了各种硬件和操作系统对内存访问的差异性 ---多线程情况下会有编译器优化和内存优化
sync
volatile
final
happens-before

reentrant的优势：synchronized提供了便捷性的隐式获取释放锁机制（基于JVM机制）;
但是它缺少获取锁与释放锁的可操作性，可中断，超时等待，且它为独占式在高并发场景下性能大打折扣。

​     

