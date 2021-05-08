package class01;


import utils.SortUtils;

import java.util.Arrays;

/**
 * 选择排序，就是从 0 ~ N-1 中找到一个最小的值，然后依次放到 0,1...N-1 位置为止
 */
public class SelectSort {

    static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 0 ~ N-1 位置找到最小的 放到 0 位置
        // 1 ~ N-1 位置找到最小的 放到 1 位置
        // 2 ~ N-2 位置找到最小的 放到 2 位置
        for (int i = 0; i < arr.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++) {
                index = arr[j] < arr[index] ? j : index;
//                if(arr[index] > arr[j]){
//                    index = j;
//                }
            }
            swap(arr, i, index);
//            System.out.println("min = " + index);
        }
    }

    private static void swap(int[] arr, int i, int index) {
        int tmp = arr[i];
        arr[i] = arr[index];
        arr[index] = tmp;
    }


    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 10000;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = SortUtils.generateRandomArray(maxSize, maxValue);
            int[] arr2 = SortUtils.copyArray(arr1);
            selectionSort(arr1);
            SortUtils.comparator(arr2);
            if (!SortUtils.isEqual(arr1, arr2)) {
                succeed = false;
                SortUtils.printArray(arr1);
                SortUtils.printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = SortUtils.generateRandomArray(maxSize, maxValue);
        SortUtils.printArray(arr);
        selectionSort(arr);
        SortUtils.printArray(arr);
    }
}