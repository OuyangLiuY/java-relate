package sort;

import java.util.Arrays;

/**
 * 希尔排序
 *
 * @author chopsticks
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] arrs = new int[]{4, 3, 10, 2, 6, 20, 11, 30, 15};

        int[] result = shellSort(arrs);

        for (int aa : result) {

            System.out.print(aa + "\t");
        }
    }

    private static int[] sortMethod(int[] arrs) {

        int[] arr = Arrays.copyOf(arrs, arrs.length);

        int gap = 1;
        while (gap < arr.length) {
            gap = gap * 3 + 1;
        }
        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) {
                int temp = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > temp) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = temp;

            }
            gap = (int) Math.floor(gap / 3);
        }
        return arr;
    }


    private static int[] shellSort(int[] a) {
        double gap = a.length;//增量长度
        int dk, sentinel, k;
        while (true) {
            gap = (int) Math.ceil(gap / 2);//逐渐减小增量长度
            dk = (int) gap;//确定增量长度
            for (int i = 0; i < dk; i++) {
                //用增量将序列分割，分别进行直接插入排序。随着增量变小为1，最后整体进行直接插入排序
                for (int j = i + dk; j < a.length; j = j + dk) {
                    k = j - dk;
                    sentinel = a[j];
                    while (k >= 0 && sentinel < a[k]) {
                        a[k + dk] = a[k];
                        k = k - dk;
                    }
                    a[k + dk] = sentinel;
                }
            }
            //当dk为1的时候，整体进行直接插入排序
            if (dk == 1) {
                break;
            }
        }
        return a;
    }
}
