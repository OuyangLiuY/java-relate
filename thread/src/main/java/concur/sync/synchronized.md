笔记：
    1：http://note.youdao.com/noteshare?id=375f02ef5a2e1fef0b2e7cd199b25f6f
        ：linux下mutex命令
        
 /**
     * concur 上锁就是改变对象的对象头
     * 64 byte JVM
     * 对象内存中分三块区域：
     * 1.对象头    ---大小固定 ->96bit
     *         1.Mark Word ->64bit  --> unused:25,hashcode:31,unused:1,age:4,biased_lock:1,concur.lock:2.
     *         2.Class MeteData Address ->32bit
     * 2.实例数据   ---大小不固定
     * 3.填充数据
     * 使用sync时对象的状态：
     *  1.无状态
     *  2.偏向锁
     *  3.轻量锁
     *  4.重量锁
     *  5.gc标记
     */       
     
     单个锁也可以重偏向 
锁：
    1.偏向锁： 同一个线程执行同步代码块。
    2.轻量级锁： 多个线程线程交替执行同步代码块(无竞争)。
    3.重量级锁： 多个线程竞争执行同步代码块。
    
   当一个线程创建了多个具有偏向锁状态的实例时候，偏向状态的时候，多个线程交替执行的时候
   重偏向？ 如果
   批量偏向？ 20->
   批量撤销？ 40 ->