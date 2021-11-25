package basic_knowledge.class02;

public class Code02_EvenTimes {
    // arr中，只有一种数，出现奇数次
    public static void printOddTimesNum1(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }
    // arr中，有两种数，出现奇数次
    public static void printOddTimesNum2(int[] arr){
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        // eor 的值是这个两个奇数 异或值
        //提取出最右的1
        int rightOne = eor & (-eor);
        int arther = 0;
        for (int i = 0; i < arr.length; i++) {
            // arr[1] = 111100011110000
            // rightOne=000000000010000
            // 比如这两个奇数一个是 3(0011),一个是4(0100)
            // (arr[i] & rightOne) != 0 => 找到当前位置这个数为1的值，然后用这个奇数个值(3)去做异或,则可以得到这个奇数3
            if ((arr[i] & rightOne) != 0){
                arther ^= arr[i];
            }
        }
        // 奇数3再去跟这个eor去做异或，则可以得到另外一个奇数4
        System.out.println(arther + " " + (eor ^ arther));
    }

    public static int bit_1_Counts(int n){
        int counts = 0;
        //n:     00111011101000
        //取反    11000100010111
        //-n:+1  11000100011000
        // n&-n: 00000000001000
        while(n != 0){
            // 提取出最右侧的1来的
            int rightOne = n & ((~n) + 1);
            counts ++;
            n ^= rightOne;
        }
        return counts;
    }


    public static void main(String[] args) {
        int a = 5;
        int b= 7;
        a = a^ b;
        b = a^ b;
        a = a^b;
        System.out.println(a);
        System.out.println(b);
        int[] arr1 = { 3, 3, 2, 3,2,3, 1, 1, 1, 3, 1, 1, 1 };
        printOddTimesNum1(arr1);
        int n = 100;
        System.out.println(n & ((~n) + 1));
        System.out.println(n & (-n));

        System.out.println(((~n) + 1));
        System.out.println((-n));
        System.out.println(bit_1_Counts(n));
    }
}
