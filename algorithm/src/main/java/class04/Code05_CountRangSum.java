package class04;

// leetcode 327题 : https://leetcode-cn.com/problems/count-of-range-sum/

import java.util.LinkedList;

/**
 * 给定一个数组arr，两个整数lower和upper，
 * 返回arr中有多少个子数组的累加和在[lower,upper]范围上
 */

public class Code05_CountRangSum {

    public static int countRangeSum(int[] arr,int lower,int upper){
        // 辅助数组为 0 ~ i 的和
        long[] sums = new long[arr.length];
        sums[0] = arr[0];
        for (int i = 1; i <arr.length ; i++) {
            sums[i] = sums[i-1] + arr[i];
        }
        return process(sums,0,sums.length -1,lower,upper);
    }

    private static int process(long[] sums, int L, int R,int lower, int upper) {
        int res = 0;
        if(L == R){ // base case 这个数是arr中 0~L 的和，所以这个数只要在[lower.upper] 区间上即可
            return sums[L] >= lower && sums[L] <= upper ? 1:0;
        }
        int mid = (R + L) / 2;
        int left = process(sums, L, mid, lower, upper);
        int right = process(sums, mid +1, R, lower, upper);
        return left + right + merge(sums,L,mid,R,lower,upper);
    }

    private static int merge(long[] arr, int L, int M, int R, int lower, int upper) {
        int res = 0;
        int windowL = L;
        int windowR = L;
        // [windowL, windowR)
        for (int i = M +1; i <= R ; i++) {
            long  min = arr[i] - upper;
            long  max = arr[i] - lower;
            while (windowR <= M && arr[windowR] <= max){
                windowR++;
            }
            while (windowL <= M && arr[windowL] < min){
                windowL++;
            }
            res += windowR - windowL;
        }
        long[] help = new long[R-L + 1];
        int index = 0;
        int p1 = L;
        int p2 = M + 1;
        while (p1 <= M && p2 <= R){
            help[index++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M){
            help[index++] = arr[p1++];
        }
        while (p2 <= R){
            help[index++] = arr[p2++];
        }
        for (int i = 0; i < help.length; i++) {
            arr[L+i] = help[i];
        }
        return res;
    }
}
