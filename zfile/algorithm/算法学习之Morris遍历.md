## Morris遍历

一种遍历二叉树的方式，并且时间复杂度O(N)，额外空间复杂度O(1)

通过利用原树中大量空闲指针的方式，达到节省空间的目的

## Morris遍历细节

假设来到当前节点cur，开始时cur来到头节点位置

1）如果cur没有左孩子，cur向右移动(cur = cur.right)

2）如果cur有左孩子，找到左子树上最右的节点mostRight：

- a.如果mostRight的右指针指向空，让其指向cur，然后cur向左移动(cur = cur.left)
- 如果mostRight的右指针指向cur，让其指向null，然后cur向右移动(cur = cur.right)(第二次到达)

3）cur为空时遍历停止

```java
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
```



## Morris遍历实质

建立一种机制：

对于没有左子树的节点只到达一次，

对于有左子树的节点会到达两次

morris遍历时间复杂度依然是O(N)，准确来说是2倍O(N)

## Morris 先序遍历

```java
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
    System.out.println("");
}
```

## Morris 中序遍历

```java
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
```

## Morris 后序遍历

**备注**：较为复杂

```java
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
```

## 题目：

给定一棵二叉树的头节点head

求以head为头的树中，最小深度是多少？

**方法1：二叉树递归套路**

```java
public static int minHeight1(Node head) {
    if (head == null) {
        return 0;
    }
    return process(head);
}

private static int process(Node head) {
    // 叶子节点
    if (head.right == null && head.left == null) {
        return 1;
    }
    // 左右节点至少有一个不为空
    int leftH = Integer.MAX_VALUE;
    if (head.left != null) {
        leftH = process(head.left);
    }
    int rightH = Integer.MAX_VALUE;
    if (head.right != null) {
        rightH = process(head.right);
    }
    return 1 + Math.min(leftH, rightH);
}
```

**方法2：根据morris遍历改写**

```java
public static int minHeight2(Node head) {
    if (head == null) {
        return 0;
    }
    Node cur = head;
    Node mostRight = null;
    int curLevel = 0;
    int minHeight = Integer.MAX_VALUE;
    while (cur != null) {
        mostRight = cur.left;
        if (mostRight != null) {
            int rightBoardSize = 1;
            while (mostRight.right != null && mostRight.right != cur) {
                rightBoardSize++;
                mostRight = mostRight.right;
            }
            if (mostRight.right == null) { // 第一次到达
                curLevel++;
                mostRight.right = cur;
                cur = cur.left;
                continue;
            } else { // 第二次到达
                if (mostRight.left == null) {
                    minHeight = Math.min(minHeight, curLevel);
                }
                curLevel -= rightBoardSize;
                mostRight.right = null;
            }
        } else { // 只有一次到达
            curLevel++;
        }
        cur = cur.right;
    }
    int finalRight = 1;
    cur = head;
    while (cur.right != null) {
        finalRight++;
        cur = cur.right;
    }
    if (cur.left == null && cur.right == null) {
        minHeight = Math.min(minHeight, finalRight);
    }
    return minHeight;
}
```