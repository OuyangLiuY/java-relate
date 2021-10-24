package basic_knowledge.class13;

import java.util.*;

/**
 * 求两个任意节点，得最低公共祖先
 */
public class Code03_lowestAncestor {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    // 使用集合方式实现，(a,b)
    public static Node lowestAncestor1(Node head, Node a, Node b) {
        if (head == null) {
            return null;
        }
        // key得value为父节点
        Map<Node, Node> parentMap = new HashMap<>();
        parentMap.put(head, null);
        fillParentMap(head, parentMap);
        // aSet 中所有经过a得父节点
        Set<Node> aSet = new HashSet<>();
        Node cur = a;
        aSet.add(cur);
        while (parentMap.get(cur) != null) {
            cur = parentMap.get(cur);
            aSet.add(cur);
        }
        cur = b;
        while (!aSet.contains(cur)) {
            cur = parentMap.get(cur);
        }
        return cur;
    }

    private static void fillParentMap(Node head, Map<Node, Node> parentMap) {
        if (head.left != null) {
            parentMap.put(head.left, head);
            fillParentMap(head.left, parentMap);
        }
        if (head.right != null) {
            parentMap.put(head.right, head);
            fillParentMap(head.right, parentMap);
        }
    }

    public static Node lowestAncestor2(Node head, Node a, Node b) {
        return process(head, a, b).ans;
    }

    public static class Info {
        public boolean findA;
        public boolean findB;
        public Node ans;

        public Info(boolean findA, boolean findB, Node ans) {
            this.findA = findA;
            this.findB = findB;
            this.ans = ans;
        }
    }

    // 1.跟x无关，ab在左树上汇聚，左有答案
    // 2.跟x无关，ab在右树上汇聚，右有答案
    // 3.跟x无关，ab在整颗树上不全，都没有答案
    // 4.跟x有关，a在左树/或右树，x就是汇聚点
    // 5.跟x有关，a在左树/或右树，x就是汇聚点
    // 6.跟x有关，x本身就是a/b节点，左树或者右树发现b/a
    public static Info process(Node head, Node a, Node b) {
        if (head == null)
            return new Info(false, false, null);
        Info leftInfo = process(head.left, a, b);
        Info rightInfo = process(head.right, a, b);
        boolean findA = a == head || leftInfo.findA || rightInfo.findA;
        boolean findB = b == head || leftInfo.findB || rightInfo.findB;
        Node ans = null;
        if (leftInfo.ans != null) {
            ans = leftInfo.ans;
        } else if (rightInfo.ans != null) {
            ans = rightInfo.ans;
        } else {
            if (findA && findB) {
                ans = head;
            }
        }
        return new Info(findA, findB, ans);
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
    public static Node pickRandomOne(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        fillPrelist(head, arr);
        int randomIndex = (int) (Math.random() * arr.size());
        return arr.get(randomIndex);
    }

    // for test
    public static void fillPrelist(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPrelist(head.left, arr);
        fillPrelist(head.right, arr);
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            Node o1 = pickRandomOne(head);
            Node o2 = pickRandomOne(head);
            if (lowestAncestor1(head, o1, o2) != lowestAncestor2(head, o1, o2)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }
}