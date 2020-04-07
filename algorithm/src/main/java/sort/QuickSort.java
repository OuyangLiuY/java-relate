package sort;

import java.util.Arrays;

/**
 * 快速排序？
 *
 * @author chopsticks
 */
public class QuickSort {
    public static void main(String[] args) {

        int[] arrs = new int[]{4, 3, 10, 2, 6, 20, 11, 30, 15};

        int[] result = sortMethod(arrs);

        for (int aa : result) {
            System.out.print(aa + "\t");
        }
    }

    private static int[] sortMethod(int[] arrs) {
        int[] arr = Arrays.copyOf(arrs, arrs.length);

        return quickSort(arr, 0, arr.length - 1);
    }

    private static int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }

    private static int partition(int[] arr, int left, int right) {
        int p = left;
        int index = p + 1;
        for (int i = index; i <= right; i++) {
            if (arr[i] < arr[p]) {
                swap(arr, i, index);
                index++;
            }
        }
        swap(arr, p, index - 1);
        return index - 1;
    }

    private static void swap(int[] arr, int p, int i) {
        int temp = arr[p];
        arr[p] = arr[i];
        arr[i] = temp;
    }
}
