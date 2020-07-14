package my.test;

public class VolatileTest {

    static  volatile int race = 0;
    public static void increase(){
        race++;
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[20];
        for (int i = 0; i < 20 ; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }
        if(Thread.activeCount() > 1)
            Thread.yield();
        System.out.print(race);
    }
}
