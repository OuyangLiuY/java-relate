package class05;


import utils.SortUtils;

import java.util.Arrays;

public class Code01_PartitionAndQuickSort {
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // arr[L...R],以arr[R]位置的数做划分
    // 将小于等于[R]数据放到左边，将大于[R]的数放到右边
    // arr = {1,3,4,2,5,8,2}
    // 1,2,4,3,5,8,2 -> index = 3, lessEqual = 2
    // 1,2,2,3,5,8
    public static int partition(int[] arr, int L, int R) {
        if (L > R) {
            return -1;
        }
        if (L == R) {
            return L;
        }
        int lessEqual = L - 1;
        int index = L;
        while (index < R) {
            // 以数组中R位置的数比较
            if (arr[index] <= arr[R]) {
                swap(arr, index, ++lessEqual);
            }
            index++;
        }
        // 最后将arr[R]这个数据跟arr[L...R-1] 的中点位置的数据替换
        swap(arr, ++lessEqual, R);
        return lessEqual;
    }

    // arr[L...R] 玩荷兰国旗问题的划分，以arr[R]做划分值
    // 将小于等于[R]数据放到左边，将大于[R]的数放到右边,最中间的数必须是R位置的数
    public static int[] netherLandsFlag(int[] arr, int L, int R) {
        if (L > R) {
            return new int[]{-1, -1};
        }
        if (L == R) {
            return new int[]{L, R};
        }
        int less = L - 1; // < 区间 右边
        int more = R; // >区间，左边界
        int index = L;
        while (index < more) { // 当前位置，不能和 >区的左边界撞上
            if (arr[index] == arr[R]) {
                index++;
            } else if (arr[index] < arr[R]) { // <
//                swap(arr,less + 1, index);
//                less ++;
//                index++; //等同于下面表达式
                swap(arr, ++less, index++);
            } else { // >
                swap(arr, index, --more);
            }
        }
        swap(arr, more, R); //最后跟R位置上数据交换
        return new int[]{less + 1, more};
    }

    public static int[] netherLandsFlag2(int[] arr, int L, int R) {
        if (L > R) {
            return new int[]{-1, -1};
        }
        if (L == R) {
            return new int[]{L, R};
        }
        //从左向右方向指针
        int index = L;
        // 从右边向左方向移动的指针
        int more = R;  // > 区 左边界
        int lsess = L - 1; // < 区 右边界
        while (index < more) { // 当前位置，不能和大于区域的左边界相撞
            if (arr[index] < arr[R]) { // <
                swap(arr, index++, ++lsess);
            } else if (arr[index] == arr[R]) { // ==
                index++;
            } else { // >
                swap(arr, index, --more);
            }
        }
        swap(arr, more, R); // 将R位置上的数放到中间大小数值位置
        return new int[]{lsess + 1, more};
    }

    // 快速排序1.0
    public static void quickSort1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        processor1(arr, 0, arr.length - 1);
    }

    private static void processor1(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        // 一次排序的结果：
        // L..R partition arr[R] [ <=arr[R] arr[R] >arr[R] ]
        // **因为每一次partition都会搞定一个数的位置且不会再变动，所以排序能完成**
        int M = partition(arr, L, R);
        processor1(arr, L, M - 1);
        processor1(arr, M + 1, R);
    }

    // 快速排序2.0
    public static void quickSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        processor2(arr, 0, arr.length - 1);
    }

    private static void processor2(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        // 一次排序的结果：
        // L..R partition arr[R] [ <=arr[R] arr[R] >arr[R] ]
        // **因为每一次partition都会搞定一批数的位置且不会再变动，所以排序能完成**
        int[] M = netherLandsFlag2(arr, L, R);
        processor2(arr, L, M[0] - 1);
        processor2(arr, M[1] + 1, R);
    }

    // 快速排序3.0
    public static void quickSort3(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        processor3(arr, 0, arr.length - 1);
    }

    private static void processor3(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        // 随机快排序
        swap(arr, L + (int) (Math.random() * (R - L + 1)), R);
        int[] M = netherLandsFlag2(arr, L, R);
        processor3(arr, L, M[0] - 1);
        processor3(arr, M[1] + 1, R);
    }


    public static void main(String[] args) {
/*        int[] arr = new int[]{1,3,4,2,5,8,6,1,3,2};
       // quickSort1(arr);
        System.out.println(Arrays.toString(arr));
        quickSort3(arr);
        System.out.println(Arrays.toString(arr));
        int res = partition(arr, 0, arr.length - 1);
        System.out.println(res);
        System.out.println(Arrays.toString(arr));

        int[] res = netherLandsFlag2(arr,0,arr.length - 1);
        System.out.println(Arrays.toString(res));
        System.out.println(Arrays.toString(arr));*/

        int maxSize = 100000;
        int maxValue = 1000;
        int testTimes = 1;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int[] array1 = SortUtils.generateRandomArray(maxSize, maxValue);
            int[] array2 = SortUtils.copyArray(array1);
            int[] array3 = SortUtils.copyArray(array1);
            quickSort1(array1);
            quickSort2(array2);
            quickSort3(array3);
            if (!SortUtils.isEqual(array1, array2)) {
                System.out.println("出错了...");
                System.out.println(Arrays.toString(array1));
                System.out.println(Arrays.toString(array2));
                System.out.println(Arrays.toString(array3));
            }
        }
        System.out.println("测试结束");
    }
}
