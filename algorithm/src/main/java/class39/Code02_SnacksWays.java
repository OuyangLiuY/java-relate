package class39;

public class Code02_SnacksWays {

    public static int ways1(int[] arr, int w) {
        return process(arr, 0, w);
    }

    // 从左往右的经典模型
    // 还剩的容量是rest，arr[index...]自由选择，
    // 返回选择方案
    // index ： 0～N
    // rest : 0~w
    private static int process(int[] arr, int index, int rest) {
        if (rest < 0) { // 容量没了
            return -1;
        }
        if (index == arr.length) { // 无零食可以选
            return 1;
        }
        // 要当前位置
        int p1 = process(arr, index + 1, rest - arr[index]);
        // 不要当前位置
        int p2 = process(arr, index + 1, rest);
        return (p1 != -1 ? p1 : 0) + p2;
    }

    public static int ways2(int[] arr, int w) {
        int N = arr.length;
        int[][] dp = new int[N + 1][w + 1];
        // 当前index来到了N位置，不管，w多少，都是1
        for (int i = 0; i <= w; i++) {
            dp[N][i] = 1;
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = 0; j <= w; j++) {
                dp[i][j] = (j - arr[i] >= 0 ? dp[i + 1][j - arr[i]] : 0) + dp[i + 1][j];
            }
        }
        return dp[0][w];
    }

    //
    public static int ways3(int[] arr, int w) {
        int N = arr.length;
        int[][] dp = new int[N][w + 1];
        // 容量为0得情况
        for (int i = 0; i < N; i++) {
            dp[i][0] = 1;
        }
        if (arr[0] <= w) {
            dp[0][arr[0]] = 1;
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= w; j++) {
                dp[i][j] = dp[i - 1][j] + (j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0);
            }
        }
        int ans = 0;
        for (int i = 0; i <= w; i++) {
            ans += dp[N - 1][i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {4, 3, 2, 9};
        int w = 8;
        System.out.println(ways1(arr, w));
        System.out.println(ways2(arr, w));
        System.out.println(ways3(arr, w));
    }
}
