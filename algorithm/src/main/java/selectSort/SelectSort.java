package selectSort;


import java.util.Arrays;

/**
 *  选择排序，就是从 0 ~ N-1 中找到一个最小的值，然后依次放到 0,1...N-1 位置为止
 */
public class SelectSort {

    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 10000;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            selectionSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");

        int[] arr = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        selectionSort(arr);
        printArray(arr);
    }

    private static void printArray(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    private static boolean isEqual(int[] arr1, int[] arr2) {
        if((arr1 ==null && arr2 != null) || (arr1 !=null && arr2 == null)){
            return false;
        }
        if(arr1 == null && arr2 ==null){
            return true;
        }
        if(arr1 .length != arr2.length){
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if(arr1[i] != arr2[i]){
                return false;
            }
        }
        return true;
    }

    private static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    private static int[] copyArray(int[] arr) {
        if(arr == null){
            return null;
        }
        int [] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    private static int[] generateRandomArray(int maxSize, int maxValue) {
        // Math.random()   [0,1)  是0 ~ 1 的左闭又开区间
        // Math.random() * N   [0,N)  是0 ~ 1 的左闭又开区间
        // (int)(Math.random() * N)  [0, N-1] 的左闭又开区间
        int[] arr = new int [(int) (Math.random() * (maxSize + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxValue + 1)) - (int)(Math.random() * (maxValue + 1));
        }
        return arr;
    }

    static void selectionSort(int[] arr){
        if(arr == null || arr.length < 2){
            return;
        }
        // 0 ~ N-1 位置找到最小的 放到 0 位置
        // 1 ~ N-1 位置找到最小的 放到 1 位置
        // 2 ~ N-2 位置找到最小的 放到 2 位置
        for(int i = 0;i< arr.length -1 ; i++){
            int index = i;
            for(int j = i+1 ; j < arr.length; j++){
                index = arr[j] < arr[index] ? j : index;
//                if(arr[index] > arr[j]){
//                    index = j;
//                }
            }
            swap(arr,i,index);
//            System.out.println("min = " + index);
        }
    }

    private static void swap(int[] arr, int i, int index) {
        int tmp = arr[i];
        arr[i] = arr[index];
        arr[index] =tmp;
    }
}
