public class IntegerTest {
    private static final int FIRST_BUCKET_SIZE = 8;
    public static void main(String[] args) {
        int i = 1;
        int zeroNumPos = Integer.numberOfLeadingZeros(8);
        Integer parseInt1 = Integer.parseInt("10");
        Integer parseInt2 = Integer.parseInt("10");
        System.out.println(parseInt1 == parseInt2);
        int pos = 9;
        /*System.out.println(FIRST_BUCKET_SIZE);
        System.out.println(i <<= 1<<(1<<(1<<1)));
        int zeros = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE + 32);
        System.out.println(zeros);*/
   /*     System.out.println((0x80000000 >>> zeroNumPos));
        System.out.println(Integer.toBinaryString((0x80000000 >>> zeroNumPos)));
        System.out.println(Integer.toBinaryString(0x80000000));
        System.out.println(Integer.toBinaryString(0x8));
        System.out.println(Integer.toBinaryString(28));
        System.out.println(Integer.numberOfLeadingZeros(0x8));
        String  str = "1000 0000 0000 0000 0000 0000 0000 0000";*/
    }
}
