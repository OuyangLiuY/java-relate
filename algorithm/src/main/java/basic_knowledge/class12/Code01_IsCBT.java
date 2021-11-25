package basic_knowledge.class12;

import java.util.*;

/**
 * 是否是一个完全二叉树
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
    // 对每一棵子树，是否是满二叉树、是否是完全二叉树、高度
    public static class  Info{
        public boolean isFull;
        public boolean isCBT;
        public int height;

        public Info(boolean isFull, boolean isCBT, int height) {
            this.isFull = isFull;
            this.isCBT = isCBT;
            this.height = height;
        }
    }

    public static Info process(Node head){
        if(head == null){
            return  new Info(true,true,0);
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);
        int height = Math.max(leftInfo.height,rightInfo.height) + 1;
        boolean isCBT = false;
        boolean isFull = leftInfo.isFull && rightInfo.isFull && rightInfo.height == leftInfo.height;
        if(isFull){
            isCBT =true;
        }else {
            // 以x为头整棵树，不满
            if (leftInfo.isCBT && rightInfo.isCBT){
                if(leftInfo.isCBT && rightInfo.isFull
                        && leftInfo.height == rightInfo.height + 1
                ){
                    isCBT =true;
                }
                if(leftInfo.isFull && rightInfo.isFull
                        && leftInfo.height == rightInfo.height + 1){
                    isCBT =true;
                }
                if(leftInfo.isFull && rightInfo.isCBT &&  leftInfo.height == rightInfo.height){
                    isCBT =true;
                }
            }
        }
         return new Info(isFull,isCBT,height);
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
