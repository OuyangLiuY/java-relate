package basic_knowledge.class01;

import utils.SortUtils;

public class InsertionSort {
    public static void  insertionSort(int[] arr){
        if (arr == null || arr.length < 2) return;

        for (int i = 1; i < arr.length; i++) {
            for (int j = i-1; j >= 0 ; j--) {
                if(arr[j] > arr[j + 1]){
                    SortUtils.swapNoEqualIndex(arr,j,j+1);
                }
            }
        }
    }

    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = SortUtils.generateRandomArray(maxSize, maxValue);
            int[] arr2 = SortUtils.copyArray(arr1);
            insertionSort(arr1);
            SortUtils.comparator(arr2);
            if (!SortUtils.isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = SortUtils.generateRandomArray(maxSize, maxValue);
        SortUtils.printArray(arr);
        insertionSort(arr);
        SortUtils.printArray(arr);
    }
}
