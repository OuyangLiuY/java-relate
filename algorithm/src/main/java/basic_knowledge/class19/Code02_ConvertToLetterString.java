package basic_knowledge.class19;

/**
 * 规定1和A对应、2和B对应、3和C对应...26和Z对应
 * 那么一个数字字符串比如"111”就可以转化为:
 * "AAA"、"KA"和"AK"
 * 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
 */
public class Code02_ConvertToLetterString {
    // str只含有数字字符0~9
    // 返回多少种转化方案
    public static int number(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return process(str.toCharArray(), 0);
    }

    // str[0..i-1]转化无需过问
    // str[i.....]去转化，返回有多少种转化方法
    public static int process(char[] str, int i) {
        if (i == str.length) {
            return 1;
        }
        // i没到最后，说明有字符
        if (str[i] == '0') { // 如果有单个0则不能转化
            return 0;
        }
        //单个字符有可能性
        int ways = process(str, i + 1);
        // 两个字符组合
        if (i + 1 < str.length && ((str[i] - '0') * 10 + (str[i + 1] - '0')) < 27) {
            ways += process(str, i + 2);
        }
        return ways;
    }

    public static int dp(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        int[] dp = new int[N + 1];
        dp[N] = 1;
        for (int index = N - 1; index >= 0; index--) {
            if (str[index] != '0') { // 如果有单个0则不能转化
                //单个字符有可能性
                int ways = dp[index + 1];
                // 两个字符组合
                if (index + 1 < str.length && ((str[index] - '0') * 10 + (str[index + 1] - '0')) < 27) {
                    ways += dp[index + 2];
                }
                dp[index] = ways;
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        System.out.println(number("7210231231232031203123"));
        System.out.println(dp("7210231231232031203123"));
    }
}
