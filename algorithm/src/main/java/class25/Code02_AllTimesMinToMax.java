package class25;

import java.util.Stack;

/**
 * 给定一个只包含正数的数组arr，arr中任何一个子数组sub，
 * 一定都可以算出(sub累加和 )* (sub中的最小值)是什么，
 * 那么所有子数组中，这个值最大是多少？
 */
public class Code02_AllTimesMinToMax {
    // 暴力解法 时间复杂度O(N^3)
    // 思路1：以每一个位置当做最小值做比较
    public static int max1(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                int minMum = Integer.MAX_VALUE;
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += arr[k];
                    minMum = Math.min(minMum, arr[k]);
                }
                max = Math.max(max, minMum * sum);
            }
        }
        return max;
    }

    // 使用单调栈解法，时间复杂度 O(N)
    public static int max2(int[] arr) {
        int N = arr.length;
        // sum 数组arr的前缀和
        int[] sums = new int[N];
        sums[0] = arr[0];
        for (int i = 1; i < N; i++) {
            sums[i] = sums[i - 1] + arr[i];
        }
        int max = Integer.MIN_VALUE;
        // 只存位置
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < N; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                int index = stack.pop();
                max = Math.max(max, (stack.isEmpty() ? (sums[i - 1]) : (sums[i - 1] - sums[stack.peek()])) * arr[index]);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int index = stack.pop();
            max = Math.max(max, (stack.isEmpty() ? (sums[N - 1]) : (sums[N - 1] - sums[stack.peek()])) * arr[index]);
        }
        return max;
    }

    public static int[] gerenareRondomArray() {
        int[] arr = new int[(int) (Math.random() * 20) + 10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 101);
        }
        return arr;
    }

    public static void main(String[] args) {
        int testTimes = 2000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            int[] arr = gerenareRondomArray();
            if (max1(arr) != max2(arr)) {
                System.out.println("FUCK!");
                System.out.println("max1 = " + max1(arr));
                System.out.println("max2 = " + max2(arr));
                break;
            }
        }
        System.out.println("test finish");
    }
}
