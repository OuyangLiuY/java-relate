package leetcode.editor.cn.leetcode.editor.cn;
//给定一个整数数组 nums 。区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
//
// 请你以下标 i （0 <= i <= nums.length ）为起点，元素个数逐次递增，计算子数组内的元素和。 
//
// 当元素和落在范围 [lower, upper] （包含 lower 和 upper）之内时，记录子数组当前最末元素下标 j ，记作 有效 区间和 S(i,
// j) 。 
//
// 求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 有效 区间和的个数。 
//
// 
//
// 注意： 
//最直观的算法复杂度是 O(n2) ，请在此基础上优化你的算法。 
//
// 
//
// 示例： 
//
// 
//输入：nums = [-2,5,-1], lower = -2, upper = 2,
//输出：3 
//解释：
//下标 i = 0 时，子数组 [-2]、[-2,5]、[-2,5,-1]，对应元素和分别为 -2、3、2 ；其中 -2 和 2 落在范围 [lower = 
//-2, upper = 2] 之间，因此记录有效区间和 S(0,0)，S(0,2) 。
//下标 i = 1 时，子数组 [5]、[5,-1] ，元素和 5、4 ；没有满足题意的有效区间和。
//下标 i = 2 时，子数组 [-1] ，元素和 -1 ；记录有效区间和 S(2,2) 。
//故，共有 3 个有效区间和。 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 10^4 
// 
// Related Topics 排序 树状数组 线段树 二分查找 分治算法 
// 👍 322 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
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
//leetcode submit region end(Prohibit modification and deletion)
