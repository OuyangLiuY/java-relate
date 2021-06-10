package class15;

/**
 * leetcode 547题
 * https://leetcode.com/problems/number-of-provinces/
 * A province is a group of directly or indirectly connected cities and no other cities outside of the group.
 * <p>
 * You are given an n x n matrix isConnected where isConnected[i][j] = 1 if the ith city and the jth city are directly connected, and isConnected[i][j] = 0 otherwise.
 * 题意：
 * 1  0  0  1  0
 * 0  1  0  0  0
 * 0  0  1  0  1
 * 1  0  0  1  0
 * 0  0  1  0  1
 * <p>
 * [i][j] = [j][i]
 * [i][j] = 1
 */
public class Code01_FriendCircles {

    public int findCircleNum(int[][] M) {
        int N = M.length;
        UnionFind unionFind = new UnionFind(N);
        // 只针er对上半
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (M[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        return unionFind.sets();
    }

    public static class UnionFind {
        // parent[i] = k : i的父亲是k
        public int[] parent;
        // size[i] = k :  如果i是代表节点，size[i]才有意义，否则无意义
        // i所在的集合大小是多少
        public int[] size;
        private final int[] help;
        // 一共有多少个集合
        private int sets;

        public UnionFind(int N) {
            parent = new int[N];
            size = new int[N];
            help = new int[N];
            sets = N;
            for (int i = 0; i < N; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // 从i开始一直往上，往上到不能再往上，代表节点，返回
        // 这个过程要做路径压缩
        private int find(int i) {
            int hi = 0;
            while (i != parent[i]) {
                help[hi++] = i;
                i = parent[i];
            }
            for (hi--; hi >= 0; hi--) {
                parent[help[hi]] = i;
            }
            return i;
        }

        public void union(int i, int j) {
            int f1 = find(i);
            int f2 = find(j);
            if(f1 != f2){
                if(size[f1] >= size[f2]){
                    size[f1] += size[f2];
                    parent[f2] = f1;
                }else {
                    size[f2] += size[f1];
                    parent[f1] = f2;
                }
                sets --;
            }
        }

        public int sets() {
            return sets;
        }
    }
}
