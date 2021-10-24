package basic_knowledge.class21;

/**
 * 给定5个参数，N，M，row，col，k
 * 表示在N*M的区域上，醉汉Bob初始在(row,col)位置
 * Bob一共要迈出k步，且每步都会等概率向上下左右四个方向走一个单位
 * 任何时候Bob只要离开N*M的区域，就直接死亡
 * 返回k步之后，Bob还在N*M的区域的概率
 */
public class Code05_BobDie {

    public static double livePosibility1(int row, int col, int k, int N, int M) {
        // 一个点可以有4中方式，所有一共有4的k次方中可能
        return (double) process(row, col, k, N, M) / Math.pow(4, k);
    }

    private static long process(int row, int col, int rest, int N, int M) {
        if (row < 0 || row == N || col < 0 || col == M) {
            return 0;
        }
        // 还在棋盘中！
        if (rest == 0) {
            return 1;
        }
        // 还在棋盘中！还有步数要走
        long up = process(row - 1, col, rest - 1, N, M);
        long down = process(row + 1, col, rest - 1, N, M);
        long left = process(row, col - 1, rest - 1, N, M);
        long right = process(row, col + 1, rest - 1, N, M);
        return up + down + left + right;
    }

    public static double livePosibility2(int row, int col, int k, int N, int M) {
        // 位置，还有步数，三个变量
        long[][][] dp = new long[N][M][k + 1];
        // rest == 0 情况
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                dp[i][j][0] = 1;
            }
        }
        for (int rest = 1; rest <= k; rest++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    long up = pick(dp, r - 1, c, rest - 1, N, M);
                    long down = pick(dp, r + 1, c, rest - 1, N, M);
                    long left = pick(dp, r, c - 1, rest - 1, N, M);
                    long right = pick(dp, r, c + 1, rest - 1, N, M);
                    dp[r][c][rest] = up + down + left + right;
                }
            }
        }
        return (double) dp[row][col][k] / Math.pow(4, k);
    }

    public static long pick(long[][][] dp, int r, int c, int rest, int N, int M) {
        if (r < 0 || r == N || c < 0 || c == M) {
            return 0;
        }
        return dp[r][c][rest];
    }

    public static void main(String[] args) {
        System.out.println(livePosibility1(6, 6, 10, 50, 50));
        System.out.println(livePosibility2(6, 6, 10, 50, 50));
    }
}
