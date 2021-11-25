package basic_knowledge.class04;

/**
 * 在一个数组中，
 * 对于每个数num，求有多少个后面的数 * 2 依然<num，求总个数
 * 比如：[3,1,7,0,2]
 * 3的后面有：1，0
 * 1的后面有：0
 * 7的后面有：0，2
 * 0的后面没有
 * 2的后面没有
 * 所以总共有5个
 */
public class Code04_BiggerThanTwiceRight {

    public static int twiceRight(int[]arr){
        if(arr == null || arr.length < 2){
            return 0;
        }
        return process(arr,0,arr.length-1);
    }

    private static int process(int[] arr, int L, int R) {
        if(L == R){ //base case not forget
            return 0;
        }
        int mid = (R + L ) /2;
        int left = process(arr, L, mid);
        int right = process(arr, mid + 1, R);
        return left+right + merge(arr,L,mid,R);
    }

    private static int merge(int[] arr, int L, int M, int R) {
        //[L...M] [M+1,R]
        int res = 0;
        //是从 [M + 1 , windowR]
        int windowR = M + 1;
        for (int i = L; i <= M; i++) {
            while (windowR <= R && arr[i] > arr[windowR] * 2){
                windowR ++;
            }
            res += windowR -M -1;
        }

        // 标准的归并排序
        int[] help = new int[R -L +1];
        int index = 0;
        int p1 = L;
        int p2 = M + 1;
        while (p1 <= M && p2 <= R){
            help[index++] = arr[p1] < arr[p2] ? arr[p1 ++] : arr[p2 ++];
        }
        while (p1 <= M){
            help[index++] = arr[p1 ++];
        }
        while (p2 <= R){
            help[index++] = arr[p2 ++];
        }
        System.arraycopy(help,0,arr,L,help.length);
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {3,1,7,0,2};
        System.out.println(twiceRight(arr));
    }
}
