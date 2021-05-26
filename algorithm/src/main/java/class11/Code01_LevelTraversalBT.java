package class11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 树的层级遍历
 */
public class Code01_LevelTraversalBT {


    public static class Node{
        public int value;
        public Node right;
        public Node left;

        public Node(int value) {
            this.value = value;
        }
    }

    public static void level(Node node){
        if(node == null)
            return;
        Queue<Node> queue = new LinkedList<>();
        // 先放入头节点
        queue.add(node);
        while (!queue.isEmpty()){
            Node curt = queue.poll();
            System.out.println(curt.value);
            if(curt.left != null){
                queue.add(curt.left);
            }
            if(curt.right != null){
                queue.add(curt.right);
            }

        }
    }

    /**
     *      1
     *    2   3
     *  4  5 6  7
     * @param args
     */

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        level(head);
        System.out.println("========");
    }

}
