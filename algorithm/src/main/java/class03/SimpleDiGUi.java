package class03;

public class SimpleDiGUi {

    // 求arr中的最大值
    public static int getMax(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }

    public static int process(int[] arr, int L, int R) {
        // base case , arr[L...R] 范围上只有一个数，直接返回，必须要有的
        if (L == R) {
            return arr[L];
        }
        //(L+R) / 2 == L + ((R - L) >> 1)
        int mid = L + ((R - L) >> 1); // 中点   	1
        int letMax = process(arr, L, mid);
        int rightMax = process(arr, mid + 1, R);
        return Math.max(letMax, rightMax);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, -1, -10, 5, 7, 10, 70, 100};
        System.out.println(getMax(arr));
    }

}
