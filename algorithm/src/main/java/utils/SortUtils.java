package utils;

import java.util.Arrays;

public class SortUtils {

    public static int[] generateRandomArray(int maxSize, int maxValue) {
        // Math.random()   [0,1)  是0 ~ 1 的左闭又开区间
        // Math.random() * N   [0,N)  是0 ~ 1 的左闭又开区间
        // (int)(Math.random() * N)  [0, N-1] 的左闭又开区间
        int[] arr = new int [(int) (Math.random() * (maxSize + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxValue + 1)) - (int)(Math.random() * (maxValue + 1));
        }
        return arr;
    }

    public static void printArray(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i])  return false;
        }
        return true;
    }

    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    public static int[] copyArray(int[] arr) {
        if (arr == null) return null;

        int[] res = new int[arr.length];
        System.arraycopy(arr, 0, res, 0, arr.length);
        return res;
    }
    //  6:110 7:111
    //  111
    // i和j是一个位置的话，会出错
    public static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];// 110 ^ 111 = 001
        arr[j] = arr[i] ^ arr[j];// 001 ^ 111 = 110
        arr[i] = arr[i] ^ arr[j];// 001 ^ 110 = 111
    }
}
