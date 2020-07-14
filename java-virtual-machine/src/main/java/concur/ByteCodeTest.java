package concur;

public class ByteCodeTest {
    static int method() {
        int a = 1;
        int b = 2;
        return (a + b) * 10;
    }

    public static void main(String[] args) {
        int result = method();
        System.out.println("result=" + result);
    }
}
