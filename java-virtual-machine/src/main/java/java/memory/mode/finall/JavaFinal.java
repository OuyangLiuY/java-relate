package java.memory.mode.finall;

public class JavaFinal {
    int i;
    final int j;
    static JavaFinal obj;
    JavaFinal(){
        i = 1;
        j = 2;
    }
    public static void  writer(){
        obj = new JavaFinal();
    }
    public static void reader(){
        JavaFinal object = obj;
        int a = object.i;
        int b = object.j;
    }
}
