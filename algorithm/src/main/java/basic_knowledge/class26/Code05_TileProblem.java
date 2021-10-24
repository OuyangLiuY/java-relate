package basic_knowledge.class26;

/**
 *  用1*2的瓷砖，把N*2的区域填满
 *
 * 返回铺瓷砖的方法数
 */
public class Code05_TileProblem {

    // f() = f(n-1) + f(n-2)
    // 第一块瓷砖，竖着放，有 f(n-1)种
    // 第一。二块瓷砖，横着放，有f(n-2)种
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
