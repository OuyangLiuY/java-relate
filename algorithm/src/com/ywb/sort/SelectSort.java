package com.ywb.sort;

import java.util.Arrays;

/**
 * 选择排序
 *
 * @author chopsticks
 */
public class SelectSort {

    public static void main(String[] args) {
        int[] arrs = new int[]{4, 3, 10, 2, 6, 20, 11, 30, 15};

        int[] result = sortMethod(arrs);

        for (int aa : result) {

            System.out.print(aa + "\t");
        }

    }

    /**
     * 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置,
     * 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
     * 重复第二步，直到所有元素均排序完毕。
     *
     * @param arrs
     * @return
     */
    private static int[] sortMethod(int[] arrs) {

        int[] arr = Arrays.copyOf(arrs, arrs.length);

        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }

            if (i != min) {
                int temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }
        }

        return arr;
    }
}