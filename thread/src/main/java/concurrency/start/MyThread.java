package concurrency.start;

public class MyThread {

    static {
        System.loadLibrary("MyThreadNative");
    }
    public void run(){
        System.out.println("this is java Thread!");
    }

    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start0();
    }
    //gcc  -fPIC -I /home/software/jdk1.8.0_221/include -I /home/software/jdk1.8.0_221/include/linux  -shared -o MyThreadNative.so MyThread.c
    //gcc -o threadNew MyThread.c -I /home/software/jdk1.8.0_221/include -I /home/software/jdk1.8.0_221/include/linux -L /home/software/jdk1.8.0_221/jre/lib/amd64/server -ljvm -pthread
    public native void start0();
}
