package sort;

import java.util.Arrays;
import java.util.Collections;

public class BinSearch {

    public static void main(String[] args) {

        int [] arr = {0,1,2,3,4,5,6,7,8,9};

        //int binSearch = binSearch(arr, 0, arr.length, 6);
        int binSearch = binSearch(arr, 6);
        System.out.println(binSearch);
        System.out.println(Arrays.toString(arr));
        //Arrays.toString(arr);
        Byte a = 127;
        byte b = 126;
        int res = a+b;
        b = (byte) (a + b);
        //System.out.println(b);
        Integer parseInt1 = Integer.parseInt("127");
        Integer parseInt2 = Integer.parseInt("127");
        System.out.println(parseInt1 == parseInt2);
       // System.out.println(parseInt1 == parseInt2);
        Collections.reverse(Arrays.asList(arr));


    }

    public static int binSearch(int[] srcArray, int key) {

        int mid;
        int start = 0;
        int end = srcArray.length - 1;
        while (start <= end) {
            mid = (start + end) / 2 ;
            if (key < srcArray[mid])
                end = mid - 1;
            else if (key > srcArray[mid])
                start = mid + 1;
            else
                return mid;
        }
        return -1;
    }
    //递归
    public static int binSearch(int[] srcArray,int start,int end, int key) {

        int mid = (end + start)/2;

        if(srcArray[mid]>key)
            return binSearch(srcArray,start,mid-1,key);
        if(srcArray[mid]<key)
            return binSearch(srcArray,mid+1,end,key);
        else
            return mid;

    }
    }
