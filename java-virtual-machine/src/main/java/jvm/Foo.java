package jvm;

public class Foo {
    int i = 1;
    Foo(){
        System.out.println("1:"+i);
        int x = getValue();
        System.out.println("2:"+x);
    }
    {
        i = 2;
    }
    int getValue(){
        return i;
    }
}
class Bar extends  Foo{
    int j =1;
    Bar(){
        j=2;
    }
    {
        j=3;
    }
    @Override
    int getValue(){
        return j;
    }
    public static void main(String[] args) {
        Bar bar = new Bar();
        System.out.println("3:" + bar.getValue());
    }
    // 2 2 2

}