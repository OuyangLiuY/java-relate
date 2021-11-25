package basic_knowledge.class01;

import utils.SortUtils;

public class BubbleSort {


    private static void bubbleSort(int [] arr){
        if(arr == null || arr.length < 2) return;
        // 0 ~ N-1
        // 0 ~ N-2
        // 0 ~ N-3
        for (int e = arr.length - 1; e > 0 ; e--) {
            for (int i = 0; i < e; i++) {
                if(arr[i]  > arr[i + 1]){
                    SortUtils.swapNoEqualIndex(arr,i,i + 1);
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
            bubbleSort(arr1);
            SortUtils.comparator(arr2);
            if (!SortUtils.isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = SortUtils.generateRandomArray(maxSize, maxValue);
        SortUtils.printArray(arr);
        bubbleSort(arr);
        SortUtils.printArray(arr);
    }
}
