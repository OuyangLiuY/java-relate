package class04;

/**
 * 在一个数组中， 任何一个前面的数a，和任何一个后面的数b，
 * 如果(a,b)是降序的，就称为逆序对
 * 返回数组中所有的逆序对
 */
public class Code03_ReversePair {
    public static int reverseNum(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }

    private static int process(int[] arr, int L, int R) {
        if (L == R) {
            return 0;
        }
        int mid = L + ((R - L) >> 1);
        return process(arr, L, mid) + process(arr, mid + 1, R) + merge(arr, L, mid, R);
    }

    private static int merge(int[] arr, int L, int M, int R) {
        int[] help = new int[R - L + 1];
        // 反过来做
        int index = help.length - 1;
        int p1 = M;
        int p2 = R;
        int sum = 0;
        while (p1 >= L && p2 > M) {
            //先求值，再排序,有多少对？ 右边 - mid
            sum += arr[p1] > arr[p2] ? (p2 - M) : 0;
            help[index--] = arr[p1] > arr[p2] ? arr[p1--] : arr[p2--];
        }
        while (p1 >= L) {
            help[index--] = arr[p1--];
        }
        while (p2 > M) {
            help[index--] = arr[p2--];
        }
        System.arraycopy(help, 0, arr, L, help.length);
        return sum;
    }

    public static int comparator(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 3, 4, 2, 5};
        System.out.println(reverseNum(arr1));
        int[] arr2 = {1, 3, 4, 2, 5};
        System.out.println(comparator(arr2));
    }
}
