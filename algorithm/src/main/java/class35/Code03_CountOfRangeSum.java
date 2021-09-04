package class35;

import java.util.HashSet;

/**
 * leetcode题目：https://leetcode.com/problems/count-of-range-sum/
 */
public class Code03_CountOfRangeSum {

    public static int countRangeSum1(int[] nums, int lower, int upper) {
        int ans = 0;
        int N = nums.length;
        long[] sums = new long[N + 1];
        for (int i = 0; i < N; ++i) {
            sums[i + 1] = nums[i] + sums[i];
        }
        return processor(sums, 0, N + 1, lower, upper);
    }

    private static int processor(long[] sums, int L, int R, int lower, int upper) {
        //base case
        if (R - L <= 1)
            return 0;
        int mid = (R + L) / 2;
        int count = processor(sums, L, mid, lower, upper) + processor(sums, mid, R, lower, upper);
        long[] cache = new long[R - L];
        int j = mid, k = mid, t = mid;
        for (int i = L, r = 0; i < mid; ++i, ++r) {
            while (k < R && sums[k] - sums[i] < lower)
                k++;
            while (j < R && sums[j] - sums[i] <= upper)
                j++;
            while (t < R && sums[t] < sums[i])
                cache[r++] = sums[t++];
            cache[r] = sums[i];
            count += j - k;
        }
        System.arraycopy(cache, 0, sums, L, t - L);
        return count;
    }

    public static int countRangeSum2(int[] nums, int lower, int upper) {
        SizeBalancedTreeSet treeSet = new SizeBalancedTreeSet();
        long sum = 0;  // 类型不能为 int，容易越界
        int ans = 0;
        treeSet.add(0); // 一个数都没有的时候，就已经有一个前缀和累加和为0，
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i]; // 前缀和
            // 求[10,20]
            // 等于求 a 是< 20 ，b是 < 10
            // 减去就是结果了
            long a = treeSet.lessKeySize(sum - lower + 1);
            long b = treeSet.lessKeySize(sum - upper);
            ans += a - b;
            treeSet.add(sum);
        }
        return ans;
    }

    public static class SizeBalancedTreeSet {
        public SBTNode root;
        private HashSet<Long> set = new HashSet<>();


        public void add(long sum) {
            root = add(root, sum, set.contains(sum));
            set.add(sum);
        }

        private SBTNode add(SBTNode cur, long key, boolean contains) {
            if (cur == null) {
                return new SBTNode(key);
            } else {
                cur.all++;
                if (key == cur.key) {
                    return cur;
                } else {
                    if (!contains) {
                        cur.size++;
                    }
                    if (key < cur.key) {
                        cur.l = add(cur.l, key, contains);
                    } else {
                        cur.r = add(cur.r, key, contains);
                    }
                    return maintain(cur);
                }
            }
        }

        public long lessKeySize(long key) {
            SBTNode cur = root;
            int ans = 0;
            while (cur != null) {
                if (key == cur.key) {
                    return ans + (cur.l != null ? cur.l.all : 0);
                } else if (key < cur.key) {
                    cur = cur.l;
                } else {
                    ans += cur.all - (cur.r != null ? cur.r.all : 0);
                    cur = cur.r;
                }
            }
            return ans;
        }

        private SBTNode leftRotate(SBTNode cur) {
            long same = cur.all - (cur.l != null ? cur.l.all : 0) - (cur.r != null ? cur.r.all : 0);
            SBTNode right = cur.r;
            cur.r = right.l;
            right.l = cur;
            //size
            right.size = cur.size; // 节点数没变，故继承即可
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            //all
            right.all = cur.all; // 进入节点总数没变，故继承即可
            cur.all = (cur.l != null ? cur.l.all : 0) + (cur.r != null ? cur.r.all : 0) + same;
            return right;
        }

        private SBTNode rightRotate(SBTNode cur) {
            long same = cur.all - (cur.l != null ? cur.l.all : 0) - (cur.r != null ? cur.r.all : 0);
            SBTNode left = cur.l;
            cur.l = left.r;
            left.r = cur;
            //size
            left.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            //all
            left.all = cur.all;
            cur.all = (cur.l != null ? cur.l.all : 0) + (cur.r != null ? cur.r.all : 0) + same; //居然写成 (left.l != null ? left.l.all : 0) ...无语
            return left;
        }

        private SBTNode maintain(SBTNode cur) {
            if (cur == null) {
                return null;
            }
            long leftSize = cur.l != null ? cur.l.size : 0;
            long leftLeftSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            long leftRightSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            long rightSize = cur.r != null ? cur.r.size : 0;
            long rightLeftSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            long rightRightSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            if (leftLeftSize > rightSize) { // LL
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (leftRightSize > rightSize) { // LR
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rightRightSize > leftSize) { //RR
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rightLeftSize > leftSize) { //R L
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

    }

    public static class SBTNode {
        public long key;
        public SBTNode l;
        public SBTNode r;
        public long size; //二叉树平衡因子
        public long all; //当前节点上所有加入的数据大小

        public SBTNode(long key) {
            this.key = key;
            this.size = 1;
            this.all = 1;
        }
    }

    // for test
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // for test
    public static int[] generateArray(int len, int varible) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * varible);
        }
        return arr;
    }

    public static void main(String[] args) {
        int len = 200;
        int varible = 50;
        for (int i = 0; i < 100000; i++) {
            int[] test = generateArray(len, varible);
            int lower = (int) (Math.random() * varible) - (int) (Math.random() * varible);
            int upper = lower + (int) (Math.random() * varible);
            int ans1 = countRangeSum1(test, lower, upper);
            int ans2 = countRangeSum2(test, lower, upper);
            if (ans1 != ans2) {
                printArray(test);
                System.out.println(lower);
                System.out.println(upper);
                System.out.println(ans1);
                System.out.println(ans2);
            }
        }

    }
}
