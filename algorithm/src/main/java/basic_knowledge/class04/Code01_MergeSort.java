package basic_knowledge.class04;

import java.util.Arrays;

import static utils.SortUtils.generateRandomArray;
import static utils.SortUtils.isEqual;

/**
 * 归并排序 算法
 */
public class Code01_MergeSort {

    // 递归实现
    public static void mergeSortOfRecursion(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int L, int R) {
        // base case 自己写的时候忘记这个临界条件
        if (L == R) {
            return;
        }
        int mid = (L + R) / 2;
        sort(arr, L, mid);
        sort(arr, mid + 1, R);
        merge(arr, mid, L, R);
    }

    // mid L , R
    private static void merge(int[] arr, int mid, int L, int R) {
        int[] help = new int[R - L + 1];
        int index = 0;
        //左指针（处理左边数据）
        int l = L;
        //右指针（处理右边数据）
        int r = mid + 1;
        //条件:左指针不得大于mid，右指针不得大于R
        while (l <= mid && r <= R) {
            help[index++] = arr[l] <= arr[r] ? arr[l++] : arr[r++];
        }
        // 要么左边数据全部拷贝完毕，只剩右边
        while (r <= R) {
            help[index++] = arr[r++];
        }
        // 要么右边数据全部拷贝完毕，只剩左边
        while (l <= mid) {
            help[index++] = arr[l++];
        }
        //最后将help排好序的数组放进arr中
        System.arraycopy(help, 0, arr, L, help.length);
    }

    // 归并排序非递归实现
    public static void mergeSortNotRecursion(int[] arr) {
        //
        if (arr == null || arr.length < 2) {
            return;
        }
        int N = arr.length;
        // 步长
        int mergeSize = 1;
        while (mergeSize < N) { // Log N
            // 当前左组的，第一个位置
            int L = 0;
            while (L < N) {
                // 长度不够
                if (mergeSize >= N - L) {
                    break;
                }
                int mid = L + mergeSize - 1;
                int R = mid + Math.min(mergeSize, N - mid - 1);
                merge(arr, mid, L, R);
                L = R + 1;
            }
            // 步长为 2^n
            mergeSize <<= 1;
        }
    }

    public static void main(String[] args) {
        int testTime = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            //System.out.println(Arrays.toString(arr1));
            int[] arr2 = copyArray(arr1);
            mergeSortOfRecursion(arr1);
            //System.out.println(Arrays.toString(arr1));
            mergeSortNotRecursion(arr2);
            //System.out.println(Arrays.toString(arr2));
            if (!isEqual(arr1, arr2)) {
                System.out.println("出错了！");
                System.out.println(Arrays.toString(arr1));
                System.out.println(Arrays.toString(arr2));
                break;
            }
        }
        System.out.println("测试结束");
    }


    private static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }
}
