package basic_knowledge.class20;

// 测试链接：https://leetcode.com/problems/longest-palindromic-subsequence/
// palindromic
// 一般情况，子序列是不连续的，

/**
 * 给定一个字符串str，返回这个字符串的最长回文子序列长度
 * 比如 ： str = “a12b3c43def2ghi1kpm”
 * 最长回文子序列是“1234321”或者“123c321”，返回长度7
 */


public class Code01_PalindromeSubsequence {

    // 这里使用思路二解题
    //解题思路二：重暴力递归，然后改动态规划
    public static int longestPalindromeSubseq(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] chars = str.toCharArray();
        return process(chars, 0, chars.length - 1);
    }

    public static int process(char[] str, int L, int R) {
        if (L == R) {
            return 1;
        }
        // 相邻两个字符是否相同
        if (L == R - 1) {
            return str[L] == str[R] ? 2 : 1;
        }
        // 三种情况，
        // 算L，不算R
        int p1 = process(str, L, R - 1);
        // 不算L，算R
        int p2 = process(str, L + 1, R);
        // 不算L，不算R
        int p3 = process(str, L + 1, R - 1);
        // 两个都要的情况下，必须是str[L] == str[R]
        int p4 = str[L] == str[R] ? (2 + process(str, L + 1, R - 1)) : 0;
        return Math.max(Math.max(p1, p2), Math.max(p3, p4));
    }


    public static int longestPalindromeSubseq1(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] chars = str.toCharArray();
        int N = chars.length;
        int[][] dp = new int[N][N];
        dp[N - 1][N - 1] = 1;
        for (int i = 0; i < N - 1; i++) {
            dp[i][i] = 1;
            dp[i][i + 1] = chars[i] == chars[i + 1] ? 2 : 1;
        }
        //从下往上赋值
        for (int L = N - 3; L >= 0; L--) {
            for (int R = L + 2; R < N; R++) {
                /*int p1 = dp[L][R - 1];
                // 不算L，算R
                int p2 = dp[L + 1][R];
                // 不算L，不算R
                int p3 = dp[L + 1][R - 1];
                // 两个都要的情况下，必须是str[L] == str[R]
                int p4 = chars[L] == chars[R] ? (2 + dp[L + 1][R - 1]) : 0;
                dp[L][R] = Math.max(Math.max(p1, p2), Math.max(p3, p4));
                */
                //根据以上条件可以得知，当前位置的数，一定大于左边的数，一定大于下面的数，一定大于坐下的数，故可以优化如下
                dp[L][R] = Math.max(dp[L][R - 1], dp[L + 1][R]);
                if (chars[L] == chars[R]) {
                    dp[L][R] = Math.max(dp[L][R], 2 + dp[L + 1][R - 1]);
                }
            }
        }
        return dp[0][chars.length - 1];
    }

    // 解题思路一：str正序的长顺序对，str逆序的最长顺序对，两者相同就是最大会文子序列
    public static int longestPalindromeSubseq3(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }
        char[] str = s.toCharArray();
        char[] reverse = reverse(str);
        return longestCommonSubsequence(str, reverse);
    }

    public static char[] reverse(char[] str) {
        int N = str.length;
        char[] reverse = new char[str.length];
        for (int i = 0; i < str.length; i++) {
            reverse[--N] = str[i];
        }
        return reverse;
    }

    private static int longestCommonSubsequence(char[] str1, char[] str2) {
        int N = str1.length;
        int M = str2.length;
        int[][] dp = new int[N][M];
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        for (int i = 1; i < N; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }
        for (int j = 1; j < M; j++) {
            dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (str1[i] == str2[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        return dp[N - 1][M - 1];
    }
}
