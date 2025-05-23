package basic_knowledge.class12;

/**
 * 是否是满树
 */
public class Code04_IsFull {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static boolean isFull1(Node head) {
        if (head == null)
            return true;
        int height = h(head);
        int nodes = n(head);
        // 2^n - 1 个节点
        return (1 << height) - 1 == nodes;
    }

    private static int h(Node head) {
        if (head == null)
            return 0;
        return Math.max(h(head.left), h(head.right)) + 1;
    }

    private static int n(Node head) {
        if (head == null)
            return 0;
        return n(head.left) + n(head.right) + 1;
    }
    public static boolean isFull2(Node head) {
        Info process = process(head);
        return process.nodes == (1 << process.height) - 1;
    }

    public static class  Info{
        public int height;
        public int nodes;

        public Info(int height, int nodes) {
            this.height = height;
            this.nodes = nodes;
        }
    }

    public static Info process(Node head){
        if(head == null){
            return new  Info(0,0);
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);
        int height = Math.max(rightInfo.height ,leftInfo.height) + 1;
        int nodes = leftInfo.nodes + rightInfo.nodes + 1; //加 1 就是算了头节点
        return new Info(height,nodes);
    }

    // for test
    public static Node generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static Node generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            if (isFull1(head) != isFull2(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }

}
