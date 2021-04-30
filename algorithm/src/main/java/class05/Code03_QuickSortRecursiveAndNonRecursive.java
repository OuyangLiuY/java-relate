package class05;

import utils.SortUtils;

import java.util.Arrays;
import java.util.Stack;

public class Code03_QuickSortRecursiveAndNonRecursive {

    public static int[] netherLandsFlag(int[] arr, int L, int R) {
        if (L == R) {
            return new int[]{L, R};
        }
        int index = L;
        int more = R;
        int less = L - 1;
        while (index < more) { // 左边界和右边界不能碰上
            if (arr[index] < arr[R]) {
                SortUtils.swap(arr, index++, ++less);
            } else if (arr[index] > arr[R]) {
                // 大于的时候，index不动，下次继续判断index位置的值
                SortUtils.swap(arr, index, --more);
            } else {
                index++;
            }
        }
        SortUtils.swap(arr, more, R);
        return new int[]{less + 1, more};
    }

    public static void processor(int[] arr, int L, int R) {
        if (L >= R) { // base case 递归结束的条件
            return;
        }
        //为了让R这个位置的值尽量是L...R上的中间值
        SortUtils.swap(arr, L + (int) (Math.random() * ((R - L) + 1)), R);
        int[] res = netherLandsFlag(arr, L, R);
        processor(arr, L, res[0] - 1);
        processor(arr, res[1] + 1, R);
    }

    public static void quickSortRecursive(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        processor(arr, 0, arr.length - 1);
    }

    private static void quickSortNonRecursive(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int R = arr.length - 1;
        SortUtils.swap(arr, (int) (Math.random() * (R + 1)), R); // 这就是随机的意思
        int[] equalArea = netherLandsFlag(arr, 0, R);
        int el = equalArea[0];
        int er = equalArea[1];
        Stack<Oop> stack = new Stack<>();
        stack.push(new Oop(0, el - 1));
        stack.push(new Oop(er + 1, R));
        while (!stack.isEmpty()) {
            Oop op = stack.pop(); // op.l ... op.r
            if (op.l < op.r) {
                SortUtils.swap(arr, op.l + (int) (Math.random() * (op.r - op.l + 1)), op.r); // 这就是随机的意思
                int[] innerArea = netherLandsFlag(arr, op.l, op.r);
                int iel = innerArea[0];
                int ier = innerArea[1];
                stack.push(new Oop(op.l, iel - 1));
                stack.push(new Oop(ier + 1, op.r));
            }
        }
    }

    // 快排非递归版本需要的辅助类
    // 要处理的是什么范围上的排序
    public static class Oop {
        public int l;
        public int r;

        public Oop(int left, int right) {
            l = left;
            r = right;
        }
    }


    public static void main(String[] args) {
        int maxSize = 10000;
        int maxValue = 1000;
        int testTimes = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int[] array1 = SortUtils.generateRandomArray(maxSize, maxValue);
            int[] array2 = SortUtils.copyArray(array1);
            quickSortRecursive(array1);
            quickSortNonRecursive(array2);
            if (!SortUtils.isEqual(array1, array2)) {
                System.out.println("出错了...");
                System.out.println(Arrays.toString(array1));
                System.out.println(Arrays.toString(array2));
            }
        }
        System.out.println("测试结束");
    }


}
