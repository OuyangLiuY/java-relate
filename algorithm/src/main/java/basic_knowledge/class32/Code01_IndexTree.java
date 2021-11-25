package basic_knowledge.class32;

/**
 * index下
 * 0011 1001 1111
 *
 * tree：[1,1+2,3,3+4+2+1,5,5+6,7,7+8+6+5+4+3+2+1]
 *  以此类推，
 *  当在index上加入某个数得时候，跟他有关系得数是 index 与(&)当前二进制位置最右边为1得数到小于N为止 += index & (-(index));
 *  当求从1到某个位置得数得时候，就是累加以index开始，index -= index & (-(index));
 */
public class Code01_IndexTree {

    // 下标从1开始！
    public static class IndexTree {
        private int[] tree;
        private int N;

        public IndexTree(int size) {
            N = size;
            tree = new int[N + 1];
        }

        // 1 ~ index 累加和多少了？
        public int sum(int index) {
            // 相当于当前位置的数， 从累加到index为0的位置
            int ret = 0;
            while (index > 0) {
                ret += tree[index];
                index -= index & (-(index));  // 提取到最右边的一个1的位置的数，然后减
            }
            return ret;
        }


        public void add(int index, int d) {
            while (index <= N) {
                tree[index] += d;
                index += index & (-(index));  // 提取到最右边的一个1的位置的数，然后减
            }
        }
    }

    public static class Right {
        private int[] nums;
        private int N;

        public Right(int size) {
            N = size + 1;
            nums = new int[N + 1];
        }

        public int sum(int index) {
            int ret = 0;
            for (int i = 1; i <= index; i++) {
                ret += nums[i];
            }
            return ret;
        }

        public void add(int index, int d) {
            nums[index] += d;
        }
    }
    public static void main(String[] args) {
        int N = 100;
        int V = 100;
        int testTime = 2000000;
        IndexTree tree = new IndexTree(N);
        Right test = new Right(N);
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int index = (int) (Math.random() * N) + 1;
            if (Math.random() <= 0.5) {
                int add = (int) (Math.random() * V);
                tree.add(index, add);
                test.add(index, add);
            } else {
                if (tree.sum(index) != test.sum(index)) {
                    System.out.println("Oops!");
                }
            }
        }
        System.out.println("test finish");
    }
}
