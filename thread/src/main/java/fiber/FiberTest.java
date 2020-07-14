package fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;

public class FiberTest {

    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        int size = 10000;
        Fiber<Void>[] fibers = new Fiber[size];

        for (int i = 0; i < fibers.length; i++) {
            fibers[i] = new Fiber<Void>((SuspendableCallable<Void>) () -> {

                calc();
                return null;
            });
        }
        for (int i = 0; i < fibers.length; i++) {
            fibers[i].start();
        }
        for (int i = 0; i < fibers.length; i++) {
            fibers[i].join();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }




    static void calc(){
        int result  =0 ;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 200; j++) {
                result +=i;
            }
        }
        System.out.println(result);
    }
}
