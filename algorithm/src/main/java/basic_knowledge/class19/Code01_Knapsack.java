package basic_knowledge.class19;

/**
 * 给定两个长度都为N的数组weights和values，
 * weights[i]和values[i]分别代表 i号物品的重量和价值。
 * 给定一个正数bag，表示一个载重bag的袋子，
 * 你装的物品不能超过这个重量。
 * 返回你能装下最多的价值是多少?
 */
public class Code01_Knapsack {

    public static int maxValue1(int[] w, int[] v, int bag) {
        if (w == null || v == null || w.length != v.length || bag < 0) {
            return -1;
        }
        return process1(w, v, 0, bag);
    }

    public static int process1(int[] w, int[] v, int index, int rest) {
        if (rest < 0) {
            return -1;
        }
        if (index == w.length) {
            return 0;
        }
        // 要当前位置得,但是
        int next = process1(w, v, index + 1, rest - w[index]);
        int p1 = 0;
        if (next != -1) {
            p1 = v[index] + next;
        }
        // 不要当前位置
        int p2 = process1(w, v, index + 1, rest);
        return Math.max(p1, p2);
    }

    public static int dp(int[] w, int[] v, int bag) {
        if (w == null || v == null || w.length != v.length || bag < 0) {
            return -1;
        }
        int N = v.length;
        int[][] dp = new int[N + 1][bag + 1];
        // index == w.length 说明最后一行都是0
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= bag; rest++) {
                // 不要当前位置
                int p1 = dp[index + 1][rest];
                // 要当前位置得,但是
                int next = rest - w[index] < 0 ? -1 : dp[index + 1][rest - w[index]];
                int p2 = 0;
                if (next != -1) {
                    p2 = v[index] + next;
                }
                dp[index][rest] = Math.max(p1, p2);
            }
        }
        return dp[0][bag];
    }

    public static void main(String[] args) {
        int[] weights = {3, 2, 4, 7, 3, 1, 7};
        int[] values = {5, 6, 3, 19, 12, 4, 2};
        int bag = 15;
        System.out.println(maxValue1(weights, values, bag));
        System.out.println(dp(weights, values, bag));
    }
}
