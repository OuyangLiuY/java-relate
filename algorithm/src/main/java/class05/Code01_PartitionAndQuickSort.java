package class05;


public class Code01_PartitionAndQuickSort {
    public static void swap(int[] arr,int i,int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] =tmp;
    }
    // arr[L...R],以arr[R]位置的数做划分
    // <= X > X
    // <= X X
    public static int partition(int[] arr,int L ,int R){
        if(L > R){
            return -1;
        }
        if(L == R){
            return arr[L];
        }
        int lessEqual = L - 1;
        int index = L;
        while (index < R){
            // 以数组中R位置的数比较
            if(arr[index] <= arr[R]){
                swap(arr,index, ++lessEqual);
            }
            index++;
        }
        // 最后将arr[R]这个数据跟arr[L...R-1] 的中点位置的数据替换
        swap(arr,++lessEqual,arr[R]);
        return lessEqual;
    }

    public static int[] netherLandsFlag(int[] arr,int L, int R){
        if(L>R){
            return new int[] { -1, -1 };
        }
        if(L ==R){
            return new int[]{L,R};
        }
        int less = L -1; // < 区间 右边
        int more = R; // >区间，左边界
        int index = L;
        while (index < more){ // 当前位置，不能和 >区的左边界撞上
            if(arr[index] == arr[R]){
                index++;
            }else if(arr[index] < arr[R]){ // <
//                swap(arr,less + 1, index);
//                less ++;
//                index++; //等同于下面表达式
                swap(arr,++less,index++);
            }else { // >
                swap(arr,index,--more);
            }
        }
        swap(arr,more,R); //最后跟R位置上数据交换
        return new int[]{less+1,more};
    }
}
