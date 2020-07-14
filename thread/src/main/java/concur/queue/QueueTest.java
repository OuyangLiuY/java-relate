package concur.queue;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class QueueTest {
    static class ArrayBlockQueueTest {
        public static void main(String[] args) {
            try {
                ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(10);
                //线程安全放入元素
                //Inserts the specified element at the tail of this queue, waiting
                //  for space to become available if the queue is full
                //如果队列已满，就一直等到有空余的空间才插入
                arrayBlockingQueue.put("234");
                //线程不安全的放入元素
                arrayBlockingQueue.add("5656");
                //线程安全的获取元素，如果队列为空，则一直等待到队列不为空再拿去一个数据
                arrayBlockingQueue.take();
                //Inserts the specified element at the tail of this queue, waiting
                //  up to the specified wait time for space to become available if
                //  the queue is full
                //如果队列满的话一直等待特定时间，如果超时没有空间就不插入
                arrayBlockingQueue.offer("666", 2, TimeUnit.SECONDS);
                //插入元素，如果队列已满，则不插入
                arrayBlockingQueue.offer("777");
                //线程安全的获取元素，如果队列为空，返回空
                arrayBlockingQueue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class LinkedBlockingQueueTest {
        public static void main(String[] args) {
            try {
                //publicLinkedBlockingQueue() { this(Integer.MAX_VALUE);}
                LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
                //线程安全的放入元素
                linkedBlockingQueue.add("123123");
                //如果队列已满，就一直等到有空余的空间才插入
                linkedBlockingQueue.put("vvv");
                //线程安全的获取元素，如果队列为空，则一直等待到队列不为空再拿去一个数据
                linkedBlockingQueue.take();
                //如果队列满的话就插入失败
                linkedBlockingQueue.offer("qeqw");
                //直接获取一个元素，如果队列为空直接返回null
                linkedBlockingQueue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class LinkedTransferQueueTest {
        public static void main(String[] args) {
            try {
                //publicLinkedBlockingQueue() { this(Integer.MAX_VALUE);}
                LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();
                new Thread(() -> {
                    for (int i = 0; i < 5; i++) {
                        try {
                            transferQueue.transfer(Thread.currentThread().getName() + "-" + i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "t1").start();
                Thread.sleep(1000);
                new Thread(() -> {
                    System.out.println(Arrays.toString(transferQueue.toArray()));
                    for (int i = 0; i < 6; i++) {
                        try {
                            Thread.sleep(1000);
                            System.out.println(transferQueue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "t2").start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class DelayQueueTest {
        public static void main(String[] args) {
            DelayQueue queue = new DelayQueue<>();
            //queue.add();
           /* queue.add();
            queue.take();
            queue.remove();
            queue.offer();
            queue.size();*/
        }
    }

    static class DelayWorkQueueTest {
        public static void main(String[] args) throws InterruptedException {
            //DelayedWorkQueue delayedWorkQueue = new DelayedWorkQueue();
            SynchronousQueue queue = new SynchronousQueue<Runnable>();
            new Thread(() -> {
                System.out.println("take start");
                for (int i = 0; i < 5; i++) {
                    try {

                        System.out.println(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "t1").start();
            Thread.sleep(1000);
            new Thread(() -> {
                System.out.println("put start");
                // System.out.println(Arrays.toString(queue.toArray()));
                for (int i = 0; i < 6; i++) {
                    try {
                        Thread.sleep(1000);
                        queue.put(Thread.currentThread().getName() + "-" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "t2").start();
        }
    }

    static class User implements Comparable<User> {
        int priority;
        String username;
        public int getPriority() {
            return priority;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        @Override
        public int compareTo(User o) {
            return this.priority - o.getPriority() > 0 ? 1 : -1;
        }
    }
    static class PriorityQueueTest {
        String s = "ddd";
        public static void main(String[] args) {
            PriorityBlockingQueue<User> queue = new PriorityBlockingQueue<>();
            for (int i = 0; i < 10; i++) {
                User user = new User();
                int pr = new Random().nextInt(10);
                user.setPriority(pr);
                user.setUsername("测试数据 - " + i);
                queue.add(user);
            }
            for (int i = 0; i < 10; i++) {
                User poll = queue.poll();
                System.out.println("优先级是：" + poll.getPriority() + "," + poll.getUsername());
            }
        }
    }
}
