package jvm;

public class StaticTest {

    static StaticTest st = new StaticTest();

    static {   //静态代码块
        System.out.println("1");
    }

    {       // 实例代码块
        System.out.println("2");
    }

    StaticTest() {    // 实例构造器
        System.out.println("3");
        System.out.println(b);
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFunction() {   // 静态方法
        System.out.println("4");
        System.out.println( "b=" + b);
    }

    int a = 110;    // 实例变量
    static int b = 112;     // 静态变量
    static class Test2{



        public static void main(String[] args) {
            System.out.println(st.a);
            staticFunction();
        }

    }

    static class  Test3{
        static int value = 666;

        public static void main(String[] args) throws Exception{
            new Test3().method();
        }

        private void method(){
            int value = 123;
            System.out.println(Test3.value);
        }
    }
}
