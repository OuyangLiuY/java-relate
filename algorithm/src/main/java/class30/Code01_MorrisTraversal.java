package class30;

/**
 * morris 二叉树遍历
 */
public class Code01_MorrisTraversal {

    static class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;


        public TreeNode(int data) {
            this.value = data;
        }
    }

    // morris 二叉树遍历
    public static void morris(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode mostRight;
        TreeNode cur = head;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 第一次来到该node
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 将改变得节点恢复
                    mostRight.right = null;
                }
            }
            cur = cur.right;
        }
    }

    public static void treePre(TreeNode head) {
        if (head == null) {
            return;
        }
        process(head);
        System.out.println("X");
    }

    private static void process(TreeNode head) {
        if (head == null) {
            return;
        }
        System.out.print(head.value + " ");
        process(head.left);
        process(head.right);
    }

    public static void treeIn(TreeNode head) {
        if (head == null) {
            return;
        }
        processIn(head);
        System.out.println("X");
    }

    private static void processIn(TreeNode head) {
        if (head == null) {
            return;
        }
        processIn(head.left);
        System.out.print(head.value + " ");
        processIn(head.right);
    }

    public static void treePos(TreeNode head) {
        if (head == null) {
            return;
        }
        processPos(head);
        System.out.println("X");
    }

    private static void processPos(TreeNode head) {
        if (head == null) {
            return;
        }
        processPos(head.left);
        processPos(head.right);
        System.out.print(head.value + " ");
    }

    // morris 二叉树先序遍历
    public static void morrisPre(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode mostRight;
        TreeNode cur = head;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 第一次来到该node
                if (mostRight.right == null) {
                    System.out.print(cur.value + " ");
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 将改变得节点恢复
                    mostRight.right = null;
                }
            } else {
                System.out.print(cur.value + " ");
            }
            cur = cur.right;
        }
        System.out.println("xx");
    }

    // morris 二叉树中序遍历
    public static void morrisIn(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode mostRight;
        TreeNode cur = head;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 第一次来到该node
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 将改变得节点恢复
                    mostRight.right = null;
                }
            }
            System.out.print(cur.value + " ");
            cur = cur.right;
        }
        System.out.println("xx");
    }

    // morris 二叉树中序遍历
    // 后序遍历，需要通过链表反转来后续打印
    public static void morrisPos(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode mostRight;
        TreeNode cur = head;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 第一次来到该node
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {

                    // 将改变得节点恢复
                    mostRight.right = null;
                    printEdge(cur.left);
                }
            }
            cur = cur.right;
        }
        printEdge(head);
        System.out.println("xx");
    }

    public static void printEdge(TreeNode head) {
        TreeNode tail = reverseEdge(head);
        TreeNode cur = tail;
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.right;
        }
        reverseEdge(tail);
    }

    // 链表反转
    private static TreeNode reverseEdge(TreeNode from) {
        TreeNode pre = null;
        TreeNode next = null;
        while (from != null) {
            next = from.right;
            from.right = pre;
            pre = from;
            from = next;
        }
        return pre;
    }

    // for test -- print tree
    public static void printTree(TreeNode head) {
        System.out.println("Binary Tree:");
        printInOrder(head, 0, "H", 17);
        System.out.println();
    }

    public static void printInOrder(TreeNode head, int height, String to, int len) {
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

    // 是否是搜索二叉树
    public static boolean isBST(TreeNode head) {
        if (head == null) {
            return false;
        }
        int pre = 0;
        boolean res = true;
        TreeNode mostRight;
        TreeNode cur = head;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 第一次来到该node
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 将改变得节点恢复
                    mostRight.right = null;
                }
            }
            // 根据中序遍历数组大小是递增来判断
            if (pre >= cur.value) {
                res = false;
            }
            pre = cur.value;
            cur = cur.right;
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode head = new TreeNode(4);
        head.left = new TreeNode(2);
        head.right = new TreeNode(6);
        head.left.left = new TreeNode(1);
        head.left.right = new TreeNode(3);
        head.right.left = new TreeNode(5);
        head.right.right = new TreeNode(7);
        printTree(head);
        treePre(head);
        morrisPre(head);
        treeIn(head);
        morrisIn(head);
        treePos(head);
        morrisPos(head);
        System.out.println("是否是搜索二叉树 = " + isBST(head));
        printTree(head);
    }
}
