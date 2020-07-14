public class Test {
    public static void main(String[] args) {
        String str = "abc";
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "c" + b + a;
        String bb = new String("abc");
        System.out.println(str.intern());
        System.out.println(bb.intern() == str);
        System.out.println(d.intern() == str);
        System.out.println(str == bb);
        if(bb != null){

        }
    }
}
