package basic_knowledge.class38;

public class Code03_MSumToN {
    public static boolean isMSum1(int num) {
        if (num < 0) {
            return false;
        }
        for (int i = 1; i <= num; i++) {
            int sum = i;
            for (int j = i + 1; j < num; j++) {
                if (j + sum > num) {
                    break;
                }
                if (j + sum == num) {
                    return true;
                }
                sum += j;
            }
        }
        return false;
    }

    public static boolean isMSum2(int num) {
        return num != (num & (~num + 1));
//        return num != (num & (-num));
//        return (num & (num - 1)) != 0;
    }

    public static void main(String[] args) {
     /*   for (int num = 1; num < 200; num++) {
            System.out.println(num + " : " + isMSum2(num));
        }*/
        System.out.println("test begin");
        for (int num = 1; num < 5000; num++) {
            if (isMSum1(num) != isMSum2(num)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test end");

    }
}
