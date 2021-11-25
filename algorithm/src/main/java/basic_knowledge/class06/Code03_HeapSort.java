package basic_knowledge.class06;

import utils.SortUtils;

import java.util.PriorityQueue;

public class Code03_HeapSort {

    // 堆排序额外空间复杂度O(1)
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 使用heapInsert方法使其達到堆結構
        // 時間復雜度：O(N * LogN)
//        for (int i = 0; i < arr.length; i++) {
//            heapInsert(arr,i);
//        }
        // 使用heapify方法使其數組變成堆結構
        // 時間復雜度：O(N)
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }
        // 此時得到的數組就是一個大根堆的數組
        int heapSize = arr.length;
        SortUtils.swap(arr, 0, --heapSize);
        // 此時的時間復雜度是：O (N * LogN)
        while (heapSize > 0) { // O(N)
            //  每次都跟0位置最大的數交換，此時數組長度大於heapSize 之外數已經就是排好序之後的數了
            heapify(arr, 0, heapSize); // O(LogN)
            SortUtils.swap(arr, 0, --heapSize); // O(1)
        }

    }

    private static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            SortUtils.swap(arr, index, largest);
            index = largest;
            left = index * 2 + 1;
        }
    }

    private static void heapInsert(int[] arr, int index) {
        // index -> index-1 / 2
        while (arr[index] > arr[(index - 1) / 2]) {
            SortUtils.swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void main(String[] args) {

        // 默认小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        heap.add(6);
        heap.add(8);
        heap.add(0);
        heap.add(2);
        heap.add(9);
        heap.add(1);
        while (!heap.isEmpty()) {
            System.out.println(heap.poll());
        }

        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = SortUtils.generateRandomArray(maxSize, maxValue);
            int[] arr2 = SortUtils.copyArray(arr1);
            heapSort(arr1);
            SortUtils.comparator(arr2);
            if (!SortUtils.isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = SortUtils.generateRandomArray(maxSize, maxValue);
        SortUtils.printArray(arr);
        heapSort(arr);
        SortUtils.printArray(arr);
    }
}
