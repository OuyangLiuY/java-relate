package sort;

import java.util.Arrays;

/**
 * 合并排序？
 *
 * @author chopsticks
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arrs = new int[]{4, 3, 10, 2, 6, 20, 11, 30, 15};

        int[] result = sortMethod(arrs);

        for (int aa : result) {

            System.out.print(aa + "\t");
        }
    }

    private static int[] sortMethod(int[] arrs) {

        int[] arr = Arrays.copyOf(arrs, arrs.length);

        if (arr.length < 2) {
            return arr;
        }

        int middle = (int) Math.floor(arr.length / 2);

        int[] left = Arrays.copyOfRange(arr, 0, middle);
        int[] right = Arrays.copyOfRange(arr, middle, arr.length);

        return mergeMethod(sortMethod(left), sortMethod(right));

    }

    private static int[] mergeMethod(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        while (left.length > 0 && right.length > 0) {
            if (left[0] <= right[0]) {
                result[i++] = left[0];
                left = Arrays.copyOfRange(left, 1, left.length);
            } else {
                result[i++] = right[0];
                right = Arrays.copyOfRange(right, 1, right.length);
            }
        }
        while (left.length > 0) {
            result[i++] = left[0];
            left = Arrays.copyOfRange(left, 1, left.length);
        }

        while (right.length > 0) {
            result[i++] = right[0];
            right = Arrays.copyOfRange(right, 1, right.length);
        }

        return result;
    }
}
