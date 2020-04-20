package concur;


public class Test implements  Runnable{
    int state;

    /**
     * 对象的布局
     *
     *
     */
    public void run() {
        synchronized (Test.class){
            System.out.println("q111");
        }
    }
    public static void main(String[] args) {
        //System.out.println(ClassLayout.parseInstance(Test.class).toPrintable());
    }


}
