package concur.pool;

import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolTest {
    static class ExecutorsFixed {
        //一个固定大小的线程。如果执行的线程多于可执行的数量那就进行等待可用的线程。
        //底层实现是LinkedBlockingQueue
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            ExecutorService service = Executors.newFixedThreadPool(10);
           /* for (int i = 0; i < 16; i++) {
                service.execute(()->{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId());
                    System.out.println(Thread.currentThread().getName());
                });
            }*/
            List<Future<Long>> future = new ArrayList<>();
            for (int i = 0; i < 18; i++) {
                future.add(service.submit(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                    return Thread.currentThread().getId();
                }));
            }
            future.forEach(f -> {
                try {
                    System.out.println(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            service.shutdown();
            System.out.println("??" + service.isShutdown());
        }
    }

    static class ExecutorsSingle {
        //每次只能执行一个线程，底层是实现是LinkedBlockingQueue
        public static void main(String[] args) {
            ExecutorService service = Executors.newSingleThreadExecutor();
            for (int i = 0; i < 5; i++) {
                service.execute(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }

        }
    }

    static class ExecutorsCache {
        ///每次来一个线程就去创建一个新线程，线程可复用。默认60s线程没有被用到就会被回收。
        //底层是现实SynchronousQueue
        public static void main(String[] args) {
            ExecutorService service = Executors.newCachedThreadPool();
            System.out.println(service);
            for (int i = 0; i < 30; i++) {
                service.execute(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }
            System.out.println(service);
            System.out.println("结束！");
        }
    }

    static class ExecutorsScheduled {
        //创建固定大小的线程用来每间隔多少时间来执行。
        //底层实现是DelayedWorkQueue
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
            System.out.println(service);
            List<ScheduledFuture<String>> futures = new ArrayList<>(20);
            for (int i = 0; i < 20; i++) {
                ScheduledFuture<String> schedule = service.schedule(() -> {
                    return Thread.currentThread().getName();
                }, 1000, TimeUnit.MILLISECONDS);
                futures.add(schedule);

            }

            service.shutdown();
            System.out.println(service.isShutdown());
            futures.forEach(future -> {
                try {
                    System.out.println(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            System.out.println(service.isShutdown());
        }
    }

    static class ExecutorsStealing {
        // 创建一个根据cpu核心数大小容量的任务队列 stealing(剽窃，偷)。
        // 该线程是一个守护线程，底层实现是ForkJoinPool
        public static void main(String[] args) throws InterruptedException {
            ExecutorService service = Executors.newWorkStealingPool();
            System.out.println(service);
            System.out.println(Runtime.getRuntime().availableProcessors());
            for (int i = 0; i < 40; i++) {
                service.execute(() -> {
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                });

            }

            TimeUnit.MICROSECONDS.sleep(50000);
        }
    }

    static class ExecutorsForkJoin {
        static int[] sums = new int[10000000];
        static int MAX = 5000;
        static Random r = new Random();
        static {
            for (int i = 0; i < 10000000; i++) {
                sums[i] = r.nextInt(100);
            }
            long begin = System.currentTimeMillis();
            int sum = Arrays.stream(sums).sum();
            long end = System.currentTimeMillis();
            System.out.println(String.format("%sms", end - begin));
            System.out.println("sums:=" + sum);
        }
        /*static class ForkJoinTaskTest extends RecursiveAction {
            int start, end;
            ForkJoinTaskTest(int start, int end) {
                this.start = start;
                this.end = end;
            }
            @Override
            protected void compute() {
                long sum = 0L;
                if (end - start <= MAX) {
                    for (int i = start; i < end; i++) {
                        sum += sums[i];
                    }
                    System.out.println(sum);
                } else {
                    int middle = start + (end - start) / 2;
                    ForkJoinTaskTest s = new ForkJoinTaskTest(start, middle);
                    ForkJoinTaskTest e = new ForkJoinTaskTest(middle, end);
                    s.fork();
                    e.fork();
                }
            }
        }*/
        static class ForkJoinTaskTest extends RecursiveTask<Long> {
            int start, end;
            ForkJoinTaskTest(int start, int end) {
                this.start = start;
                this.end = end;
            }
            @Override
            protected Long compute() {
                if (end - start <= MAX) {
                    long sum = 0L;
                    for (int i = start; i < end; i++) {
                        sum += sums[i];
                    }
                    //System.out.println(sum);
                    return sum;
                }
                int middle = start + (end - start) / 2;
                ForkJoinTaskTest s = new ForkJoinTaskTest(start, middle);
                ForkJoinTaskTest e = new ForkJoinTaskTest(middle, end);
                s.fork();
                e.fork();
                return s.join() + e.join();
            }
        }
        public static void main(String[] args) throws InterruptedException {
            ForkJoinPool pool = new ForkJoinPool();
            long begin = System.currentTimeMillis();
            ForkJoinTaskTest task = new ForkJoinTaskTest(0, sums.length);
            pool.execute(task);
            long result = task.join();
            System.out.println(result);
            long end = System.currentTimeMillis();
            System.out.println(String.format("%sms", end - begin));
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
