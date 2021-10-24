package basic_knowledge.class02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 一个数组中有一种数出现K次，其他数都出现了M次，
 * M > 1,  K < M
 * 找到，出现了K次的数，
 * 要求，额外空间复杂度O(1)，时间复杂度O(N)
 * <p>
 * 解题思路：将这个数组中所有的值按照二进制位数依次累加到一个长度为32数组的位数中，
 * 遍历该数组，某个位置上的数，去模 M，如果结果不为0,则，这个位数上的值，代表的是包括K这个数据的值，反之，则不包括
 * 则可以得到这个出现k此的数在某个位置上的值，然后一次累加到相应的位数，则可得到该值
 * </p>
 */
public class Code02_KM {
    public static int hashKTimes(int[] arr, int k, int m) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        int ans = 0;
        for (Integer curr : map.keySet()) {
            if (k == map.get(curr)) {
                ans = curr;
                break;
            }
        }
        return ans;
    }

    // 请保证arr中，只有一种数出现了K次，其他数都出现了M次
    public static int onlyKTimes(int[] arr, int k, int m) {
        int[] t = new int[32];
        // t [0] 位置1出现了几个
        for (int value : arr) {
            for (int i = 0; i < 32; i++) {
                // (value >> i & 1) != 0 依次提取位置上为1
                // 表示第 i 位上为1
                if ((value >> i & 1) != 0) {
                    t[i]++;
                }
            }
        }
        // 将所有的arr中的数，依次按照二进制的位数累加到t这个数组中
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            // t[i] % m != 0 说明这个出现k次的数在第i位上有 1
            if (t[i] % m != 0) {
                ans |= (1 << i);
            }
        }
        return ans;
    }

    public static int[] randomArray(int maxKinds, int range, int k, int m) {
        //数组中有多少种类型的数据
        int numKinds = (int) (Math.random() * maxKinds + 2);
        // 至少需要两种数据
        // 数组长度 = ( k + (numKinds-1) * m)
        int[] arr = new int[k + (numKinds - 1) * m];
        int index = 0;
        int kTimesValue = (int) (Math.random() * range) + 1;
        //填充k次数据
        for (; index < k; index++) {
            arr[index] = kTimesValue;
        }
        numKinds--;
        // 填充M次数据
        HashSet<Integer> set = new HashSet<>();
        set.add(kTimesValue);
        while (numKinds > 0) {
            int currNam;
            do {
                currNam = (int) (Math.random() * range + 1);
            } while (set.contains(currNam));
            set.add(currNam);
            numKinds--;
            for (int j = 0; j < m; j++) {
                arr[index++] = currNam;
            }
        }
        //填充m次数据完成
        //arr中数据太过整齐，需要随机打乱
        for (int i = 0; i < arr.length; i++) {
            // 将arr[i] 上的数据随机跟数组上另外一个位置的数据交换
            int a = (int) (Math.random() * arr.length);
            int tmp = arr[i];
            arr[i] = arr[a];
            arr[a] = tmp;
        }
        return arr;
    }

    public static void main(String[] args) {
        int maxKinds = 10;
        int range = 200;
        int testTimes = 10000;
        int max = 9;
        //System.out.println(Arrays.toString(randomArray(maxKinds, range, 2, 3)));
        System.out.println("测试开始...");
        for (int i = 0; i < testTimes; i++) {
            int a = (int) (Math.random() * max + 1);// a 1 ~ 9
            int b = (int) (Math.random() * max + 1);// b 1 ~ 9
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            if (k == m) {
                m++;
            }
            int[] arr = randomArray(maxKinds, range, k, m);
            int res1 = hashKTimes(arr, k, m);
            int res2 = onlyKTimes(arr, k, m);
            if (res1 != res2) {
                System.out.println("出错了....");
                System.out.println("res1 = " + res1);
                System.out.println("res2 = " + res2);
            }
        }
        System.out.println("测试结束...");
    }
}
