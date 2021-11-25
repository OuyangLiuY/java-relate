package basic_knowledge.class26;

/**
 * 第一年农场有1只成熟的母牛A，往后的每年：
 * <p>
 * 1）每一只成熟的母牛都会生一只母牛
 * <p>
 * 2）每一只新出生的母牛都在出生的第三年成熟
 * <p>
 * 3）每一只母牛永远不会死
 * <p>
 * 返回N年后牛的数量
 */
public class Code03_TheCowProblem {

    // 思路：牛n年之后，要算前一年得数量 f(n-1) 但是呢还要算三年之后成年之后又生得牛f(n-3)
    public static int res(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2 || n == 3) {
            return n;
        }
        // [ 1 ,1 ]
        // [ 1, 0 ]
        int[][] base = {{1, 1, 0}, {0, 0, 1}, {1, 0, 0}};
        // fn = |2,1| * |re|
        int[][] res = Utils.matrixPower(base, n - 3);
        // f(n) = |f3,f2,f1|*|res|
        return 3 * res[0][0] + 2 * res[1][0] + res[2][0];
    }
}
