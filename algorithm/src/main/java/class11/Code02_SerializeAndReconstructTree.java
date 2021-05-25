package class11;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Code02_SerializeAndReconstructTree {
    /**
     * 二叉树可以通过先序、后序或者按层遍历的方式序列化和反序列化，
     * 以下代码全部实现了。
     * 但是，二叉树无法通过中序遍历的方式实现序列化和反序列化
     * 因为不同的两棵树，可能得到同样的中序序列，即便补了空位置也可能一样。
     * 比如如下两棵树
     * __2
     * /
     * 1
     * 和
     * 1__
     * \
     * 2
     * 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
     */
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    // 先序序列化
    public static Queue<String> preSerial(Node node) {
        Queue<String> queue = new LinkedList<>();
        pres(node, queue);
        return queue;
    }

    public static void pres(Node node, Queue<String> queue) {
        if (node == null) {
            queue.add(null);
        }else {
            queue.add(String.valueOf(node.value));
            pres(node.left, queue);
            pres(node.right, queue);
        }
    }
    // 中序序列化
  public static Queue<String> inSerial(Node node) {
        Queue<String> queue = new LinkedList<>();
        ins(node, queue);
        return queue;
    }

    private static void ins(Node node, Queue<String> queue) {
        if (node == null) {
            queue.add(null);
        }else {
            ins(node.left, queue);
            queue.add(String.valueOf(node.value));
            ins(node.right, queue);
        }

    }
    // 后序序列化
   public static Queue<String> posSerial(Node node) {
        Queue<String> queue = new LinkedList<>();
        pos(node, queue);
        return queue;
    }

    private static void pos(Node node, Queue<String> queue) {
        if (node == null) {
            queue.add(null);
        }else {
            pos(node.left, queue);
            pos(node.right, queue);
            queue.add(String.valueOf(node.value));
        }
    }
    // 先序反序列化
     public static Node buildByPreQueue(Queue<String> prelist) {
        if (prelist == null || prelist.size() == 0) {
            return null;
        }
        return preb(prelist);
    }

    private static Node preb(Queue<String> prelist) {
        String value = prelist.poll();
        if (value == null) {
            return null;
        }
        Node node = new Node(Integer.parseInt(value));
        node.left = preb(prelist);
        node.right = preb(prelist);
        return node;
    }


    // 反序反序列化
    public static Node buildByPosQueue(Queue<String> poslist) {
        if (poslist == null || poslist.size() == 0) {
            return null;
        }
        // 左右中  ->  stack(中右左)
        Stack<String> stack = new Stack<>();
        while (!poslist.isEmpty()) {
            stack.push(poslist.poll());
        }
        return posb(stack);
    }

    private static Node posb(Stack<String> posstack) {
        String value = posstack.pop();
        if (value == null) {
            return null;
        }
        Node node = new Node(Integer.parseInt(value));
        node.right = posb(posstack);
        node.left = posb(posstack);
        return node;
    }


    // 层级序列化
    public static Queue<String> levelSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        if(head == null){
            ans.add(null);
        }else {
            ans.add(String.valueOf(head.value));
            Queue<Node> queue = new LinkedList<>();
            queue.add(head);
            while (!queue.isEmpty()){
                head = queue.poll();
                // 先左后右
                if(head.left != null){
                    ans.add(String.valueOf(head.left.value));
                    queue.add(head.left);
                }else {
                    ans.add(null);
                }
                if(head.right != null){
                    ans.add(String.valueOf(head.right.value));
                    queue.add(head.right);
                }else {
                    ans.add(null);
                }
            }
        }
        return ans;
    }
    // 层级反序列化
    public static Node buildByLevelQueue(Queue<String> levelList) {
        if (levelList == null || levelList.size() == 0) {
            return null;
        }
        Node head = generateNode(levelList.poll());
        Queue<Node> queue = new LinkedList<>();
        if(head != null){
            queue.add(head);
        }
        Node node = null;
        while (! queue.isEmpty()){
            node = queue.poll();
            node.left = generateNode(levelList.poll());
            node.right = generateNode(levelList.poll());
            if (node.left != null) {
                queue.add(node.left);
            }
            if(node.right != null){
                queue.add(node.right);
            }
        }
        return head;
    }


    private static Node generateNode(String value) {
        if(value ==null || "".equals(value)){
            return null;
        }
        return new Node(Integer.parseInt(value));
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
    // for test
    public static boolean isSameValueStructure(Node head1, Node head2) {
        if (head1 == null && head2 != null) {
            return false;
        }
        if (head1 != null && head2 == null) {
            return false;
        }
        if (head1 == null && head2 == null) {
            return true;
        }
        if (head1.value != head2.value) {
            return false;
        }
        return isSameValueStructure(head1.left, head2.left) && isSameValueStructure(head1.right, head2.right);
    }

    // for test
    public static void printTree(Node head) {
        System.out.println("Binary Tree:");
        printInOrder(head, 0, "H", 17);
        System.out.println();
    }
    public static void printInOrder(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printInOrder(head.right, height + 1, "v", len);
        String val = to + head.value + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printInOrder(head.left, height + 1, "^", len);
    }
    public static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }
    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 10000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            Queue<String> pre = preSerial(head);
            Queue<String> pos = posSerial(head);
            Queue<String> level = levelSerial(head);
            Node preBuild = buildByPreQueue(pre);
            Node posBuild = buildByPosQueue(pos);
            Node levelBuild = buildByLevelQueue(level);
            if (!isSameValueStructure(preBuild, posBuild) || !isSameValueStructure(posBuild, levelBuild)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");

    }



}
