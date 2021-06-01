package class13;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 使用二叉树得递归套路实现是否是完全二叉树
 */
public class Code01_IsCBT {


    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }
    public static boolean isCBT1(Node head){
        if(head == null){
            return true;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        Node left = null;
        Node right = null;
        boolean leaf = false;
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            left = cur.left;
            right = cur.right;
            if(
                    (leaf) && (left != null || right != null)  // 如果遇到了不双全的节点之后，又发现当前节点不是叶节点
                            || (left == null && right != null) // 左节点没有，右节点存在，则必不是完全二叉树
            ){
                return false;
            }
            if(left != null){
                queue.add(left);
            }
            if(right != null){
                queue.add(right);
            }
            if(left == null || right == null){
                leaf = true;
            }
        }
        return true;
    }

    public static boolean isCBT2(Node head){
        return process(head).isCBT;
    }

    public static class Info {
        public int height;
        public boolean isFull;
        public boolean isCBT;

        public Info(int height, boolean isFull, boolean isCBT) {
            this.height = height;
            this.isFull = isFull;
            this.isCBT = isCBT;
        }
    }

    //1.种，左右都是满树，高度一样
    //2.左完全（不一定满树），右满  此时，左高度要比右高度大一
    //3，左满，右满，左树比右树高度大一
    //4. 左满，右完全，高度一样
    public static Info process(Node head){
        if(head == null){
            return new Info(0,true,true);
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        int height = Math.max(leftInfo.height,rightInfo.height) + 1;
        boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
        boolean isCBT = false;
        // 情况1
        if(leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height){
            isCBT = true;
        }
        //情况2
        else if(leftInfo.isCBT && rightInfo.isFull && leftInfo.height == (rightInfo.height + 1)){
            isCBT = true;
        }
        //情况3
        else if(leftInfo.isFull && rightInfo.isFull && leftInfo.height == (rightInfo.height + 1)){
            isCBT = true;
        }
        //情况4
        else if(leftInfo.isFull && rightInfo.isCBT && leftInfo.height == rightInfo.height ){
            isCBT = true;
        }
        return  new Info(height,isFull,isCBT);
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
            if (isCBT1(head) != isCBT2(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");

    }
}
