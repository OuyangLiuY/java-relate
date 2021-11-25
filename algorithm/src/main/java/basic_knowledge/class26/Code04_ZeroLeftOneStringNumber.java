package basic_knowledge.class26;

/**
 * 给定一个数N，想象只由0和1两种字符，组成的所有长度为N的字符串
 *
 * 如果某个字符串,任何0字符的左边都有1紧挨着,认为这个字符串达标
 *
 * 返回有多少达标的字符串
 */
public class Code04_ZeroLeftOneStringNumber {
    public static int getNum1(int n) {
        if (n < 1) {
            return 0;
        }
        return process(1, n);
    }

    private static int process(int i, int n) {
        if (i == n - 1) {
            return 2;
        }
        if (i == n) {
            return 1;
        }
        return process(i + 1, n) + process(i + 2, n);
    }
    // 分析：只有第一个数是1时才有，故，只为1时，有f(n-1)种，只为(1,0)时，有f(n-2)种
    public static int getNum3(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        int[][] base = { { 1, 1 }, { 1, 0 } };
        int[][] res = Utils.matrixPower(base, n - 2);
        return 2 * res[0][0] + res[1][0];
    }


    public static void main(String[] args) {
        for (int i = 0; i != 20; i++) {
            System.out.println(getNum1(i));
        //    System.out.println(getNum2(i));
            System.out.println(getNum3(i));
            System.out.println("===================");
        }
    }
}
