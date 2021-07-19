package class26;

/**
 * 一个人可以一次往上迈1个台阶，也可以迈2个台阶
 * <p>
 * 返回这个人迈上N级台阶的方法数
 */
public class Code02_PeopleStep {

    // 思路：走一个台阶，剩余 f(n-1)种
    // 思路：走2个台阶，剩余 f(n-2)种
    public static int res(int n) {
        if (n == 1 || n == 2) {
            return n;
        }
        int[][] base = {{1, 1}, {1, 0}};
        int[][] re = Utils.matrixPower(base, n - 2);
        // fn = |2,1| * |re|
        return 2 * re[0][0] + re[1][0];
    }
}
