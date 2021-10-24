package basic_knowledge.class04;

//  数组中求最小和

/**
 * 在一个数组中，一个数左边比它小的数的总和，叫数的小和，所有数的小和累加起来，叫数组小和。求数组小和。
 */
public class Code02_MinSum {
    //
    public static int minSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    // arr[L..R]既要排好序，也要求小和返回
    // 所有merge时，产生的小和，累加
    // 左 排序   merge
    // 右 排序  merge
    // merge
    private static int process(int[] arr, int L, int R) {
        if (L == R) {
            return 0;
        }
        int mid = (R + L) / 2;
        return process(arr, L, mid) +
                process(arr, mid + 1, R) +
                merge(arr, L, mid, R);
    }

    private static int merge(int[] arr, int L, int M, int R) {
        int[] help = new int[R - L + 1];
        int index = 0;
        int p1 = L;
        int p2 = M + 1;
        int sum = 0;
        while (p1 <= M && p2 <= R) {
            //先求值，再排序
            sum += arr[p1] < arr[p2]? (R- p2 +1) * arr[p1] : 0;
            help[index++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M){
            help[index ++] = arr[p1++];
        }
        while (p2 <= R){
            help[index ++] = arr[p2++];
        }
        System.arraycopy(help,0,arr,L,help.length);
        return sum;
    }
    public static int minSumForComp(int[]arr){
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                sum += arr[j] < arr[i] ? arr[j] : 0;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] arr1 = {1,3,4,2,5};
        System.out.println(minSum(arr1));
        int[] arr2 = {1,3,4,2,5};
        System.out.println(minSumForComp(arr2));
    }
}
