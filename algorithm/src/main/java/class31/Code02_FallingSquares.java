package class31;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * https://leetcode.com/problems/falling-squares/
 */
public class Code02_FallingSquares {

    public static class SegmentTree {
        private final int[] max;
        private final int[] change;
        private final boolean[] update;

        public SegmentTree(int size) {
            int N = size + 1;
            max = new int[N << 2];
            change = new int[N << 2];
            update = new boolean[N << 2];
        }

        private void pushUp(int rt) {
            // 求最大值问题
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        private void pushDown(int rt, int ln, int rn) {
            // 先查看是是否需要更新
            if (update[rt]) {
                update[rt << 1] = true; //左边标记
                update[rt << 1 | 1] = true; //右边标记
                change[rt << 1] = change[rt]; //左边改变
                change[rt << 1 | 1] = change[rt]; //右边改变
                max[rt << 1] = change[rt];
                max[rt << 1 | 1] = change[rt];
                update[rt] = false;
            }

        }

        // 求 L~R 上累加和，在数组 l~r上 rt
        // 1~6 累加和是多少？ 1~500 rt
        public int query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return max[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            int left = 0;
            int right = 0;
            if (L <= mid) {
                left = query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                right = query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return Math.max(left, right);
        }

        // L~R  所有的值变成C
        // l~r  rt
        public void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                change[rt] = C;  // 缓存change
                update[rt] = true; // 标计当前位置需要改变，
                max[rt] = C;
                return;
            }
            // 当前任务躲不掉，无法懒更新，要往下发
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (L <= mid) {
                update(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            // 左右都更新之后，更新当前位置
            pushUp(rt);
        }
    }

    public HashMap<Integer, Integer> index(int[][] positions) {
        TreeSet<Integer> pos = new TreeSet<>();
        for (int[] arr : positions) {
            pos.add(arr[0]); // 左闭
            pos.add(arr[0] + arr[1] - 1); // 右开 区间
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        int count = 0;
        for (Integer index : pos) {
            map.put(index, ++count);
        }
        return map;
    }

    public List<Integer> fallingSquares(int[][] positions) {
        HashMap<Integer, Integer> map = index(positions);
        int N = map.size();
        int max = 0;
        List<Integer> res = new ArrayList<>();
        SegmentTree segmentTree = new SegmentTree(N);
        // 每落一个正方形，收集一下，所有东西组成的图像，最高高度是什么
        for (int[] arr : positions) {
            int L = map.get(arr[0]);
            int R = map.get(arr[0] + arr[1] - 1);
            int height = segmentTree.query(L, R, 1, N, 1) + arr[1];
            max = Math.max(max, height);
            res.add(max);
            segmentTree.update(L, R, height, 1, N, 1);
        }
        return res;
    }

}
