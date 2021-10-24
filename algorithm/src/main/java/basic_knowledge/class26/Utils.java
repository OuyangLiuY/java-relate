package basic_knowledge.class26;

public class Utils {
    /**
     * 求当前矩阵得p次方
     * 时间复杂度 O(LogN)
     *
     * @param m
     * @param p
     * @return m^p
     */
    public static int[][] matrixPower(int[][] m, int p) {
        int[][] res = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            res[i][i] = 1;
        }
        // m得一次方
        int[][] t = m;
        for (; p != 0; p >>= 1) {
            if ((p & 1) != 0) {
                res = muliMatrix(res, t);
            }
            t = muliMatrix(t, t);
        }
        return res;
    }

    /**
     * 矩阵 m1 * m2
     * @param m1
     * @param m2
     * @return m1 * m2
     */
    private static int[][] muliMatrix(int[][] m1, int[][] m2) {
        int[][] res = new int[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    res[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return res;
    }
}
