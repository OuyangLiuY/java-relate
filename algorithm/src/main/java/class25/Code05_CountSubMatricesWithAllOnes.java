package class25;

import java.util.Stack;

/**
 * https://leetcode.com/problems/count-submatrices-with-all-ones
 * 给定一个二维数组matrix，其中的值不是0就是1，
 * 返回全部由1组成的子矩形数量
 */
public class Code05_CountSubMatricesWithAllOnes {

    public int numSubmat(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return 0;
        }
        int nums = 0;
        int[] height = new int[mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                height[j] = mat[i][j] == 0 ? 0 : height[j] + 1;
            }
            nums += countFromBottom(height);
        }
        return nums;
    }

    //
    private int countFromBottom(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        int nums = 0;
        int[] stack = new int[heights.length];
        int index = -1;
        for (int i = 0; i < heights.length; i++) {
            while (index != -1 && heights[stack[index]] >= heights[i]) {
                int cur = stack[index--];
                // 相等得时候最早进去得移除不用算，等下次再进来得时候一起算
                if (heights[cur] > heights[i]) {
                    int left = index == -1 ? -1 : stack[index];
                    int n = (i - left - 1);
                    // 算以cur，从范围left ~ i上最大得数，小得数此时不用算，等到i上得数变小得时候再去算
                    int down = Math.max(left == -1 ? 0 : heights[left], heights[i]);
                    // 总共得数为 [cur - Max(left , i)] * [n*(n+1)/2]
                    nums += (heights[cur] - down) * num(n);
                }
            }
            stack[++index] = i;
        }
        while (index != -1) { // stack中有数，那么是整个数组中最小得一些数
            int cur = stack[index--];
            int left = index == -1 ? -1 : stack[index];
            int n = (heights.length - left - 1);
            int down = left == -1 ? 0 : heights[left];
            nums += (heights[cur] - down) * num(n);
        }
        return nums;
    }


    private int num(int n) {
        return (n * (n + 1)) >> 1;
    }

    public static void main(String[] args) {
        int[][] ad = new int[3][3];
        ad[0][0] = 1;
        ad[0][1] = 0;
        ad[0][2] = 1;
        ad[1][0] = 1;
        ad[1][1] = 1;
        ad[1][2] = 1;
        Code05_CountSubMatricesWithAllOnes a = new Code05_CountSubMatricesWithAllOnes();
        System.out.println(a.numSubmat(ad));
    }
}
