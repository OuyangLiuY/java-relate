package basic_knowledge.class11;


// 本题测试链接：https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree
import java.util.ArrayList;
import java.util.List;

/**
 * 一个多叉树，转换成二叉树反之将其可以逆向转换
 * 思想: 将所有children节点都转换成当前节点的左孩子节点的右节点，也可以转成右孩子节点左节点
 * 比如：  1
 *     2  3  4
 * 5 6 7
 *
 * 换成：
 *      1
 *    2
 *  5   3
 *    6   4
 *      7
 */
public class Code03_EncodeNaryTreeToBinaryTree {
    // 提交时不要提交这个类
    public static class Node {
        public int val;
        public List<Node> children;
        public Node() {
        }
        public Node(int _val) {
            val = _val;
        }
        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }
    // 提交时不要提交这个类
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) {
            val = x;
        }
    }
    // 只提交这个类即可
    class Codec {
        // Encodes an n-ary tree to a binary tree.
        public TreeNode encode(Node root) {
            if (root == null) {
                return null;
            }
            TreeNode head = new TreeNode(root.val);
            head.left = en(root.children);
            return head;
        }

        private TreeNode en(List<Node> children) {
            TreeNode head = null;
            TreeNode cur = null;
            for(Node child : children){
                TreeNode tn = new TreeNode(child.val);
                if(head == null){
                    head = tn;
                }else {
                    cur.right = tn;
                }
                cur = tn;
                cur.left = en(child.children);
            }
            return head;
        }

        // Decodes your binary tree to an n-ary tree.
        public Node decode(TreeNode root) {
            if (root == null) {
                return null;
            }
            // 所有左孩子节点就是当前节点的child
            return new Node(root.val,de(root.left));
        }

        private List<Node> de(TreeNode node) {
            List<Node> children = new ArrayList<>();
            while (node != null){
                Node cur = new Node(node.val);
                children.add(cur);
                node = node.left;
            }
            return children;
        }
    }

}
