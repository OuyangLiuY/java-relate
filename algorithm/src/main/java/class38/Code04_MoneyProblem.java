package class38;

import java.util.Arrays;

/**
 * int[] d，d[i]：i号怪兽的能力
 * int[] p，p[i]：i号怪兽要求的钱
 * 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的怪兽。
 * 如果你当前的能力，小于i号怪兽的能力，你必须付出p[i]的钱，贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上；如果你当前的能力，大于等于i号怪兽的能力，你可以选择直接通过，你的能力并不会下降，你也可以选择贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上。
 * 返回通过所有的怪兽，需要花的最小钱数。
 */
public class Code04_MoneyProblem {
    //返回是最小钱数
    public static long process1(int[] d, int[] p, int ability, int index) {
        // 从0号 开始尝试,直到最后一个位置，
        // 所以base case要定位到最后一个位置上
        if (index == d.length) {
            return 0;
        }
        // 这个怪兽能力大于当前能力,那你要通过，那必须贿赂
        if (ability < d[index]) {
            return p[index] + process1(d, p, d[index] + ability, index + 1);
        } else {
            // 1.贿赂
            long p1 = p[index] + process1(d, p, d[index] + ability, index + 1);
            // 2.不贿赂
            long p2 = process1(d, p, ability, index + 1);
            return Math.min(p1, p2);
        }
    }

    //返回是最小钱数
    public static long minMoney1(int[] d, int[] p) {
        return process1(d, p, 0, 0);
    }

    //返回是最小钱数
    public static int minMoney2(int[] d, int[] p) {
        int allMoney = 0;
        for (int i = 0; i < d.length; i++) {
            allMoney += p[i];
        }
        int N = p.length;
        for (int money = 0; money < allMoney; money++) {
            if (process2(d, p, money, N - 1) != -1) {
                return money;
            }
        }
        return allMoney;
    }

    // 从0....index号怪兽，花的钱，必须严格==money
    // 如果通过不了，返回-1
    // 如果可以通过，返回能通过情况下的最大能力值
    public static long process2(int[] d, int[] p, int money, int index) {
        if (index < 0) { // 一个怪兽也没遇到呢
            return money == 0 ? 0 : -1;
        }
        // index >= 0
        // 1) 不贿赂当前index号怪兽
        long preMaxAbility = process2(d, p, money, index - 1);
        long p1 = -1;
        if (preMaxAbility != -1 && preMaxAbility >= d[index]) {
            p1 = preMaxAbility;
        }
        // 2) 贿赂当前的怪兽 当前的钱 p[index]
        long preMaxAbility2 = process2(d, p, money - p[index], index - 1);
        long p2 = -1;
        if (preMaxAbility2 != -1) {
            p2 = d[index] + preMaxAbility2;
        }
        return Math.max(p1, p2);
    }

    // minMoney1思路得动态规划
    public static long fun1(int[] d, int[] p) {
        int N = d.length;
        int sum = 0;
        for (int num : d) {
            sum += num;
        }
        int[][] dp = new int[N + 1][sum + 1];
        for (int j = 0; j < sum + 1; j++) {
            dp[0][j] = 0;
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = 0; j <= sum; j++) {
                // 防止数组列越界
                if (j + d[i] > sum) {
                    continue;
                }
                if (j < d[i]) {
                    dp[i][j] = p[i] + dp[i + 1][j + d[i]];
                } else {
                    dp[i][j] = Math.min(p[i] + dp[i + 1][j + d[i]], dp[i + 1][j]);
                }
            }
        }
        return dp[0][0];
    }

    // minMoney2思路得动态规划
    public static long fun2(int[] d, int[] p) {
        int N = d.length;
        int sum = 0;
        for (int num : p) {
            sum += num;
        }
        int[][] dp = new int[N + 1][sum + 1];
        // dp[i][j]含义：
        // 能经过0～i的怪兽，且花钱为j（花钱的严格等于j）时的武力值最大是多少？
        // 如果dp[i][j]==-1，表示经过0～i的怪兽，花钱为j是无法通过的，或者之前的钱怎么组合也得不到正好为j的钱数
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= sum; j++) {
                dp[i][j] = -1;
            }
        }
        // 经过0～i的怪兽，花钱数一定为p[0]，达到武力值d[0]的地步。其他第0行的状态一律是无效的
        dp[0][p[0]] = d[0];
        for (int i = 1; i < N; i++) {
            for (int j = 0; j <= sum; j++) {
                // 1) 不贿赂当前index号怪兽
                // 前题是之前得能力大于当前得能力
                if (dp[i - 1][j] >= d[i]) {
                    // 两种可能性中，选武力值最大的
                    dp[i][j] = dp[i - 1][j];
                }
                // 可能性一，为当前怪兽花钱
                // 存在条件：
                // j - p[i]要不越界，并且在钱数为j - p[i]时，要能通过0～i-1的怪兽，并且钱数组合是有效的。
                // j >= p[i] 防止越界。不然 j + p[i] 就会超过sum
                if (j >= p[i] && dp[i - 1][j - p[i]] != -1) {
                    dp[i][j] = Math.max(dp[i][j], d[i] + dp[i - 1][j - p[i]]);
                }
            }
        }
        int ans = sum;
        for (int i = 0; i <= sum; i++) {
            if (dp[N - 1][i] != -1) {
                ans = i;
                break;
            }
        }
        return ans;
    }


    public static int[][] generateTwoRandomArray(int len, int value) {
        int size = (int) (Math.random() * len) + 1;;
        int[][] arrs = new int[2][size];
        for (int i = 0; i < size; i++) {
            arrs[0][i] = (int) (Math.random() * value) + 1;
            arrs[1][i] = (int) (Math.random() * value) + 1;
        }
        return arrs;
    }


    public static void main(String[] args) {
        int len = 10;
        int value = 200;
        int testTimes = 1;
        for (int i = 0; i < testTimes; i++) {
            int[][] arrs = generateTwoRandomArray(len, value);
            int[] d = arrs[0];
            int[] p = arrs[1];
            System.out.println(Arrays.toString(d));
            System.out.println(Arrays.toString(p));
            long ans1 = minMoney1(d, p);
            long ans2 = minMoney2(d, p);
            long ans3 = fun1(d, p);
            long ans4 = fun2(d, p);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4) {
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                System.out.println(ans4);
                System.out.println("oops!");
            }
        }

    }

}
