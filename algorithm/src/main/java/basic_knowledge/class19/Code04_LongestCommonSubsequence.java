package basic_knowledge.class19;

/**
 * 最大公共子序列问题
 * https://leetcode.com/problems/longest-common-subsequence/
 */
public class Code04_LongestCommonSubsequence {

    public static int longestCommonSubsequence(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        return process(str1, str2, str1.length - 1, str2.length - 1);
    }

    private static int process(char[] str1, char[] str2, int i1, int i2) {
        if (i1 == 0 && i2 == 0) {
            return str1[i1] == str2[i2] ? 1 : 0;
        } else if (i1 == 0) {
            if (str1[i1] == str2[i2]) {
                return 1;
            } else {
                return process(str1, str2, i1, i2 - 1);
            }
        } else if (i2 == 0) {
            if (str1[i1] == str2[i2]) {
                return 1;
            } else {
                return process(str1, str2, i1 - 1, i2);
            }
        } else {
            // 算字符i1，算i2或不算i2
            int p1 = process(str1, str2, i1 - 1, i2);
            // 算字符i2，算i1或不算i1
            int p2 = process(str1, str2, i1, i2 - 1);
            // 都算
            int p3 = str1[i1] == str2[i2] ? (1 + process(str1, str2, i1 - 1, i2 - 1)) : 0;
            return Math.max(p1, Math.max(p2, p3));
        }
    }

    public static int longestCommonSubsequence2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int N = str1.length;
        int M = str2.length;
        int[][] dp = new int[N][M];
        //条件1
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        for (int i = 1; i < M; i++) {
            dp[0][i] = str1[0] == str2[i] ? 1 : dp[0][i - 1];
        }
        for (int i = 1; i < N; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                int p1 = dp[i - 1][j];
                int p2 = dp[i][j - 1];
                int p3 = str1[i] == str2[j] ? (1 + dp[i - 1][j - 1]) : 0;
                dp[i][j] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[N - 1][M - 1];
    }

}
