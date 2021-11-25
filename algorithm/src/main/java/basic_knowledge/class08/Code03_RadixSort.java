package basic_knowledge.class08;

import utils.SortUtils;

/**
 * 基数排序
 */
public class Code03_RadixSort {
    // only for no-negative value
    public static void radixSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        radixSort(arr, 0, arr.length - 1, maxBits(arr));
    }

    // 求最大位数
    private static int maxBits(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int cur : arr) {
            max = Math.max(max, cur);
        }
        int count = 0;
        while (max != 0) {
            count++;
            max = max / 10;
        }
        return count;
    }

    public static void radixSort(int[] arr, int L, int R, int digit) {
        int i, j = 0;
        int radix = 10;
        // 准备当前数大小的辅助空间
        int[] help = new int[R - L + 1];
        for (int d = 1; d <= digit; d++) { // 有多少位，就进出多少次
            // 辅助数组是当前数的和
            // count[0] 当前位(d位)是0的数字有多少个
            // count[1] 当前位(d位)是(0和1)的数字有多少个
            // count[2] 当前位(d位)是(0、1和2)的数字有多少个
            // count[i] 当前位(d位)是(0~i)的数字有多少个
            int[] count = new int[radix];
            for (i = L; i <= R; i++) {
                j = getDigit(arr[i], d);
                count[j]++;
            }
            for (i = 1; i < radix; i++) {
                count[i] = count[i] + count[i - 1];
            }
            // 从右往左将arr上的数，依次找到并放入对应的位置
            for (i = R; i >= L; i--) {
                j = getDigit(arr[i], d);
                help[--count[j]] = arr[i];
                //count[j]--;
            }
            for (i = L, j = 0; i <= R; i++, j++) {
                arr[i] = help[j];
            }
        }
    }
    // 取得当前位数d上的值
    private static int getDigit(int v, int d) {
        return ((v / (int) Math.pow(10, d - 1)) % 10);
    }
    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }
    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 100000;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = SortUtils.copyArray(arr1);
            radixSort(arr1);
            SortUtils.comparator(arr2);
            if (!SortUtils.isEqual(arr1, arr2)) {
                succeed = false;
                SortUtils.printArray(arr1);
                SortUtils.printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = generateRandomArray(maxSize, maxValue);
        SortUtils.printArray(arr);
        radixSort(arr);
        SortUtils.printArray(arr);

    }
}
