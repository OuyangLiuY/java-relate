package com.ywb.sort;

import java.util.Arrays;

/**
 * 插入排序
 *
 * @author chopsticks
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] arrs = new int[]{4, 3, 10, 2, 6, 20, 11, 30, 15};

        int[] result = sortMethod(arrs);

        for (int aa : result) {

            System.out.print(aa + "\t");
        }
    }

    /**
     * 将第一待排序序列第一个元素看做一个有序序列，把第二个元素到最后一个元素当成是未排序序列
     * 从头到尾依次扫描未排序序列，将扫描到的每个元素插入有序序列的适当位置。（如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入到相等元素的后面
     *
     * @param arrs
     * @return
     */
    private static int[] sortMethod(int[] arrs) {

        int[] arr = Arrays.copyOf(arrs, arrs.length);

        for (int i = 1; i < arr.length; i++) {
            //记录要插入的数
            int temp = arr[i];
            //从已经排序好的最右边开始比较，找到比其小的数
            int j = i;
            while (j > 0 && temp < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }

            if (j != i) {
                arr[j] = temp;
            }
        }

        return arr;
    }
}
