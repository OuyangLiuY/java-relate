package basic_knowledge.class29;

import utils.SortUtils;

import java.util.Arrays;

/**
 * 给定一个无序数组arr中，长度为N，给定一个正数k，返回top k个最大的数
 * 不同时间复杂度三个方法：
 * 1）O(N*logN)
 * 2）O(N + K*logN)
 * 3）O(n + k*logk)
 */
public class Code02_MaxTopK {

    //1 时间复杂度O(N*logN)
    // 排序+收集
    public static int[] maxTopK1(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int N = arr.length;
        k = Math.min(N, k);
        int[] res = new int[k];
        Arrays.sort(arr);
        for (int i = N - 1, j = 0; j < k; i--, j++) {
            res[j] = arr[i];
        }
        return res;
    }

    //1 时间复杂度O(N + K*logN)
    // 堆排序 + 收集
/*    public static int[] maxTopK2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int N = arr.length;
        k = Math.min(N, k);
        int[] ans = new int[k];
        // 放入前k个数，O(K)
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i = 0; i < k; i++) {
            queue.add(arr[i]);
        }
        // O(N-K)*logK
        for (int i = k; i < N; i++) {
            if (queue.peek() == null)
                continue;
            if (queue.peek() <= arr[i]) {
                queue.poll();
                queue.add(arr[i]);
            }
        }
        while (!queue.isEmpty()) {
            int index = queue.size();
            ans[(index - 1)] = queue.poll();
        }
        return ans;
    }*/
    //1 时间复杂度O(N + K*logN)
    // 堆排序 + 收集
    public static int[] maxTopK2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int N = arr.length;
        for (int i = N - 1; i >= 0; i--) {
            heapify(arr, i, N);
        }
        k = Math.min(N, k);
        // 只把前K个数放在arr末尾，然后收集，O(K*logN)
        int heapSize = N;
        int count = 1;
        swap(arr, 0, --heapSize);
        while (heapSize > 0 && count < k) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
            count++;
        }
        int[] ans = new int[k];
        for (int i = N - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }

    public static void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }


    // 方法三，时间复杂度O(n + k * logk)
    // 使用快排思想，求出最小 得第K小得数，那么剩余只要大于得就是要得答案
    public static int[] maxTopK3(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int N = arr.length;
        k = Math.min(N, k);
        // O(N)
        int num = minKth(arr, N - k); //获取第 （N-K）小得数
        int[] ans = new int[k];
        int index = 0;
        for (int i = 0; i < N; i++) {
            // 获取大于num得数
            if (arr[i] > num) {
                ans[index++] = arr[i];
            }
        }
        // 说明这里得数是等于得数
        for (; index < k; index++) {
            ans[index] = num;
        }
        // O(k*logk)
        Arrays.sort(ans);
        for (int L = 0, R = k - 1; L < R; L++, R--) {
            swap(ans, L, R);
        }
        return ans;
    }

    private static int minKth(int[] arr, int index) {
        int L = 0;
        int R = arr.length - 1;
        int pivot = 0;
        int[] range = null;
        while (L < R) {
            pivot = arr[L + (int) (Math.random() * (R - L + 1))];
            range = partition(arr, L, R, pivot);
            if (index < range[0]) {
                R = range[0] - 1;
            } else if (index > range[1]) {
                L = range[1] + 1;
            } else {
                return pivot;
            }
        }
        return arr[L];
    }

    private static int[] partition(int[] arr, int L, int R, int pivot) {
        int less = L - 1;
        int more = R + 1;
        int cur = L;
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, cur++, ++less);
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }
        return new int[]{less + 1, more - 1};
    }


    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            // [-? , +?]
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    // for test
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    // 生成随机数组测试
    public static void main(String[] args) {
        int testTime = 10000;
        int maxSize = 100;
        int maxValue = 100;
        boolean pass = true;
        System.out.println("测试开始，没有打印出错信息说明测试通过");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            int k = (int) (Math.random() * maxSize) + 1;
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int[] ans1 = maxTopK1(arr1, k);
            int[] ans2 = maxTopK2(arr2, k);
            int[] ans3 = maxTopK3(arr3, k);
            if (!SortUtils.isEqual(ans1, ans2) || !SortUtils.isEqual(ans1, ans3)) {
                pass = false;
                System.out.println("出错了！ 第几次 = " + i);
                System.out.println(Arrays.toString(ans1));
                System.out.println(Arrays.toString(ans2));
                System.out.println(Arrays.toString(ans3));
                break;
            }
        }
        System.out.println("测试结束了，测试了" + testTime + "组，是否所有测试用例都通过？" + (pass ? "是" : "否"));
    }
}
