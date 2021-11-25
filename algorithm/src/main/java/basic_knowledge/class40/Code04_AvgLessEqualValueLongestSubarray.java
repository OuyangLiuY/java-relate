package basic_knowledge.class40;


import java.util.TreeMap;

/**
 * 给定一个数组arr，给定一个值v
 * 求子数组平均值小于等于v的最长子数组长度
 */
public class Code04_AvgLessEqualValueLongestSubarray {

    // 暴力解，时间复杂度O(N^3)，用于做对数器
    public static int ways1(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int ans = 0;
        for (int L = 0; L < arr.length; L++) {
            for (int R = L; R < arr.length; R++) {
                int sum = 0;
                int k = R - L + 1;
                for (int i = L; i <= R; i++) {
                    sum += arr[i];
                }
                double avg = (double) sum / (double) k;
                if (avg <= v) {
                    ans = Math.max(ans, k);
                }
            }
        }
        return ans;
    }

    // 想实现的解法2，时间复杂度O(N*logN)
    public static int ways2(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        TreeMap<Integer, Integer> origins = new TreeMap<>();
        int ans = 0;
        int modify = 0;
        for (int i = 0; i < arr.length; i++) {
            int p1 = arr[i] <= v ? 1 : 0;
            int p2 = 0;
            int query = -arr[i] - modify;
            if (origins.floorKey(query) != null) {
                p2 = i - origins.get(origins.floorKey(query)) + 1;
            }
            ans = Math.max(ans, Math.max(p1, p2));
            int curOrigin = -modify - v;
            if (origins.floorKey(curOrigin) == null) {
                origins.put(curOrigin, i);
            }
            modify += arr[i] - v;
        }
        return ans;
    }

    // 想实现的解法2，时间复杂度O(N)
    public static int ways3(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] help = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            help[i] = arr[i] - v;
        }
        return maxLengthAwesome(help, 0);
    }

    private static int maxLengthAwesome(int[] arr, int k) {
        int[] minSums = new int[arr.length];
        int[] minSumsEnd = new int[arr.length];
        int tail = arr.length - 1;
        minSums[tail] = arr[tail];
        minSumsEnd[tail] = tail;
        for (int i = tail - 1; i >= 0; i--) {
            if (minSums[i + 1] < 0) {
                minSums[i] = minSums[i + 1] + arr[i];
                minSumsEnd[i] = minSumsEnd[i + 1];
            } else {
                minSums[i] = arr[i];
                minSumsEnd[i] = i;
            }
        }
        int end = 0;
        int ans = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            while (end < arr.length && minSums[end] + sum <= k) {
                sum += minSums[end];
                end = minSumsEnd[end] + 1;
            }
            ans = Math.max(ans, end - i);
            if (end > i) {
                sum -= arr[i];
            } else {
                end = i + 1;
            }
        }
        return ans;
    }

    // 用于测试
    public static int[] randomArray(int maxLen, int maxValue) {
        int len = (int) (Math.random() * maxLen) + 1;
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) (Math.random() * maxValue);
        }
        return ans;
    }

    // 用于测试
    public static int[] copyArray(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    // 用于测试
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // 用于测试
    public static void main(String[] args) {
        System.out.println("测试开始");
        int maxLen = 20;
        int maxValue = 100;
        int testTime = 500000;
        for (int i = 0; i < testTime; i++) {
            int[] arr = randomArray(maxLen, maxValue);
            int value = (int) (Math.random() * maxValue);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int ans1 = ways1(arr1, value);
            int ans2 = ways2(arr2, value);
            int ans3 = ways3(arr3, value);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("测试出错！");
                System.out.print("测试数组：");
                System.out.println("子数组平均值不小于 ：" + value);
                System.out.println("方法1得到的最大长度：" + ans1);
                System.out.println("方法2得到的最大长度：" + ans2);
                System.out.println("方法3得到的最大长度：" + ans3);
                System.out.println("=========================");
            }
        }
        System.out.println("测试结束");
    }
}
