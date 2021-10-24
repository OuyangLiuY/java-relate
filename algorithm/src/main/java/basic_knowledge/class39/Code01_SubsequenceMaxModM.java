package basic_knowledge.class39;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * // 给定一个非负数组arr，和一个正数m。 返回arr的所有子序列中累加和%m之后的最大值。
 */
public class Code01_SubsequenceMaxModM {

    // 不考虑数据情况下的暴力解
    public static int max1(int[] arr, int m) {
        Set<Integer> set = new HashSet<>();
        process1(arr, 0, 0, set);
        int max = Integer.MIN_VALUE;
        for (Integer cur : set) {
            max = Math.max(max, cur % m);
        }
        return max;
    }

    private static void process1(int[] arr, int index, int sum, Set<Integer> set) {
        if (index == arr.length) {
            set.add(sum);
        } else {
            // 1.不算当前位置的数
            process1(arr, index + 1, sum, set);
            // 2.算当前位置的数
            process1(arr, index + 1, sum + arr[index], set);
        }
    }

    // 根据index和sum来考虑的动态规划
    // 求出以arr的i个位置，任意组合的情况下满足sum的位置，
    // 将表填完，只要某个格子是true的情况下，那就要求的任意子序列
    public static int max2(int[] arr, int m) {
        int sum = 0;
        for (int cur : arr) {
            sum += cur;
        }
        int N = arr.length;
        int M = sum + 1;
        boolean[][] dp = new boolean[N][M];
        // index 来到了 N位置，那么
        for (int i = 0; i < N; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0]] = true;
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                // 情况1，不需要当前位置的数
                dp[i][j] = dp[i - 1][j];
                // 情况2，需要当前位置的数
                if (j - arr[i] >= 0) {
                    dp[i][j] |= dp[i - 1][j - arr[i]];
                }
            }
        }
        int ans = 0;
        for (int i = 0; i <= sum; i++) {
            if (dp[N - 1][i]) {
                ans = Math.max(ans, i % m);
            }
        }
        return ans;
    }

    // 根据index和m来考虑的动态规划
    public static int max3(int[] arr, int m) {
        int N = arr.length;
        boolean[][] dp = new boolean[N][m];
        for (int i = 0; i < N; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0] % m] = true;
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j] = dp[i - 1][j];
                int cur = arr[i] % m;
                if (cur <= j) {
                    dp[i][j] |= dp[i - 1][j - cur];
                } else {
                    dp[i][j] |= dp[i - 1][j + m - cur];
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (dp[N - 1][i]) {
                ans = i;
            }
        }
        return ans;
    }


    // m 数据很大，arr中数据也很大，但是长度比较短
    public static int max4(int[] arr, int m) {
        TreeSet<Integer> lSet = new TreeSet<>();
        int mid = (arr.length - 1) / 2;
        process4(arr, 0, 0, mid, m, lSet);
        TreeSet<Integer> rSet = new TreeSet<>();
        process4(arr, mid + 1, 0, arr.length - 1, m, rSet);
        int ans = 0;
        for (Integer leftMod : lSet) {
            ans = Math.max(ans, leftMod + rSet.floor(m - 1 - leftMod));
        }
        return ans;
    }

    private static void process4(int[] arr, int start, int sum, int end, int m, TreeSet<Integer> set) {
        if (start == end + 1) {
            set.add(sum % m);
        } else {
            process4(arr, start + 1, sum, end, m, set);
            process4(arr, start + 1, sum + arr[start], end, m, set);
        }
    }
    public static int[] generateRandomArray(int len, int value) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * value);
        }
        return ans;
    }

    public static void main(String[] args) {
        int len = 10;
        int value = 100;
        int m = 76;
        int testTime = 500000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(len, value);
            int ans1 = max1(arr, m);
            int ans2 = max2(arr, m);
            int ans3 = max3(arr, m);
            int ans4 = max4(arr, m);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");
    }
}
