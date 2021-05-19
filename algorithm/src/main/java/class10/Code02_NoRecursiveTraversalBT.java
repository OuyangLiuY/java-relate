package class10;

import java.util.Stack;

/**
 * 非递归实现树结构遍历
 */
public class Code02_NoRecursiveTraversalBT {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int v) {
            value = v;
        }
    }

    // 先序遍历
    // 左 中 右
    public static void pre(Node head) {
        if (head == null)
            return;
        System.out.print("mid - tree : ");
        Stack<Node> stack = new Stack<>();
        stack.push(head);
        while (!stack.isEmpty()) {
            head = stack.pop();
            System.out.print(head.value + " ");
            // 右
            if (head.right != null) {
                stack.push(head.right);
            }
            // 左
            if (head.left != null) {
                stack.push(head.left);
            }
        }
        System.out.println();
    }

    //中序遍历 左 头 右
    public static void mid(Node head) {
        if (head == null)
            return;
        Node cur = head;
        Stack<Node> help = new Stack<>();
        System.out.print("mid - tree : ");
        while (!help.isEmpty() || cur != null) {
            // 先一直看左边
            if (cur != null) {
                help.push(cur);
                cur = cur.left;
            } else {
                cur = help.pop();
                System.out.print(cur.value + " ");
                cur = cur.right;
            }
        }
        System.out.println();
    }

    //后序遍历 简单版
    public static void postSimple(Node head) {
        if (head == null)
            return;
        Stack<Node> help = new Stack<>();
        Stack<Node> res = new Stack<>();
        help.push(head);
        System.out.print("post simple - tree : ");
        while (!help.isEmpty()) {
            head = help.pop(); // help 头 右 左
            res.push(head);
            // 左
            if (head.left != null) {
                help.push(head.left);
            }
            // 右
            if (head.right != null) {
                help.push(head.right);
            }
        }
        // res （左 右 头）
        while (!res.isEmpty()) {
            System.out.print(res.pop().value + " ");
        }
        System.out.println();
    }

    //后序遍历 高级版
    public static void postSupper(Node head) {
        if (head == null)
            return;
        Stack<Node> help = new Stack<>();
        System.out.print("post supper - tree : ");
        help.push(head);
        Node c = null;
        while (!help.isEmpty()) {
            c = help.peek();
            if (c.left != null && head != c.left && head != c.right) {
                help.push(c.left);
            } else if (c.right != null && head != c.right) {
                help.push(c.right);
            } else {
                System.out.print(help.pop().value + " ");
                head = c;
            }
        }
        System.out.println();
    }


    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        pre(head);
        System.out.println("========");
        mid(head);
        System.out.println("========");
        postSimple(head);
        System.out.println("========");
        postSupper(head);
        /*   System.out.println("========");*/
    }
}