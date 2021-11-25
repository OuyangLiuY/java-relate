package basic_knowledge.class11;

/**
 * 纸对折之后求当前纸的折痕
 * 原理：其实就是二叉树中序遍历的结果
 */
public class Code07_PaperFolding {
    // N 就是对折了几次
    public static void printAllFolds(int N) {
        process(1, N, true);
        System.out.println();
    }

    // 当前你来了一个节点，脑海中想象的！
    // 这个节点在第i层，一共有N层，N固定不变的
    // 这个节点如果是凹的话，down = T
    // 这个节点如果是凸的话，down = F
    // 函数的功能：中序打印以你想象的节点为头的整棵树！
    private static void process(int i, int N, boolean down) {
        if (i > N) {
            return;
        }
        process(i + 1, N, true);
        System.out.print(down ? " 凹 " : " 凸 ");
        process(i + 1, N, false);
    }

    public static void main(String[] args) {
        int N = 4;
        printAllFolds(4);
        /**
         *                    d
         *                d       u
         *              d   u   d   u
         *            d  u d u d u d u
         */
    }
}
