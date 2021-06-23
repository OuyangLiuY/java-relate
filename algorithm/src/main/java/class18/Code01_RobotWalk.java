package class18;

/**
 * 假设有排成一行的N个位置，记为1~N，N 一定大于或等于 2
 * 开始时机器人在其中的M位置上(M 一定是 1~N 中的一个)
 * 如果机器人来到1位置，那么下一步只能往右来到2位置；
 * 如果机器人来到N位置，那么下一步只能往左来到 N-1 位置；
 * 如果机器人来到中间位置，那么下一步可以往左走或者往右走；
 * 规定机器人必须走 K 步，最终能来到P位置(P也是1~N中的一个)的方法有多少种
 * 给定四个参数 N、M、K、P，返回方法数。
 */
public class Code01_RobotWalk {

    public static int ways1(int N, int start, int aim, int K) {
        if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
            return -1;
        }
        return process1(start, K, aim, N);
    }

    // 机器人当前来到的位置是cur，
    // 机器人还有rest步需要走
    // 最终目标aim
    // 有哪些位置 1~ N
    // 返回：机器人从cur出发，走过rest步之后，最终停在aim的方法数，是多少？
    private static int process1(int cur, int rest, int aim, int N) {
        if (rest == 0) { // 如果已经不需要走了，走完了！
            return cur == aim ? 1 : 0;
        }
        if (cur == 1) {
            return process1(2, rest - 1, aim, N);
        }
        if (cur == N) {
            return process1(N - 1, rest - 1, aim, N);
        }
        // cur - 1往左， cur + 1 往右走一步
        return process1(cur - 1, rest - 1, aim, N) + process1(cur + 1, rest - 1, aim, N);
    }

    public static int ways2(int N, int start, int aim, int K) {
        if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
            return -1;
        }
        int[][] dp = new int[N + 1][K + 1];
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= K; j++) {
                dp[i][j] = -1;
            }
        }
        return process2(start, K, aim, N, dp);
    }

    private static int process2(int cur, int rest, int aim, int N, int[][] dp) {

        if (dp[cur][rest] != -1) {
            return dp[cur][rest];
        }
        int ans = 0;
        if (rest == 0) { // 如果已经不需要走了，走完了！
            ans = cur == aim ? 1 : 0;
        } else if (cur == 1) {
            ans = process1(2, rest - 1, aim, N);
        } else if (cur == N) {
            ans = process1(N - 1, rest - 1, aim, N);
        } else {
            // cur - 1往左， cur + 1 往右走一步
            ans = process1(cur - 1, rest - 1, aim, N) + process1(cur + 1, rest - 1, aim, N);
        }
        dp[cur][rest] = ans;
        return ans;
    }

    // 分析：
    // 根据递归中条件进行分析：
    public static int ways3(int N, int start, int aim, int K) {
        if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
            return -1;
        }
        int[][] dp = new int[N + 1][K + 1];
        // 根据递归中条件进行分析：
        // 1. rest = 0, cur = aim : 1 说明终点这个位置为1
        dp[aim][0] = 1;
        // 第一列中aim行的第一个树数已赋值
        for (int rest = 1; rest <= K; rest++) {
            // 2. cur == 1;
            dp[1][rest] = dp[2][rest - 1];
            // 4. else
            for (int cur = 2; cur < N; cur++) {
                dp[cur][rest] = dp[cur - 1][rest - 1] + dp[cur + 1][rest - 1];
            }
            // 3. cur == N
            dp[N][rest] = dp[N - 1][rest - 1];
        }
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= K; j++) {
                System.out.print(dp[i][j] + ",");
            }
            System.out.println();
        }
        return dp[start][K];
    }

    public static void main(String[] args) {
        System.out.println(ways1(5, 2, 4, 6));
        System.out.println(ways2(5, 2, 4, 6));
        System.out.println(ways3(5, 2, 4, 6));
        int[][] test = new int[10][5];
        // 二维数组中，第一个代表是行，第二代表列
        // test[n] 代表向下第几行
        // test[0][m] 代表第一行中第m列
    }
}
