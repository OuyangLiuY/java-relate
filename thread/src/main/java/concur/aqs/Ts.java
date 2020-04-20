package concur.aqs;

public class Ts {

    static int x =20;
    static  boolean  flag =false;
    public static void main(String[] args) {


        new Thread(()->{
            x =40;
            flag =true;
        }).start();
        new Thread(()->{
            if(flag)
                System.out.println(x);
        }).start();
    }
}
