package basic_knowledge.class09;

import java.util.ArrayList;

public class Code01_LinkedListMid_MY {

    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    // 使用快慢指针实现
    // 1.输入链表头节点，奇数长度返回中点，偶数长度返回上中点
    public static Node midOrUpMidNode(Node head) {
        // 前提是不是要三个节点以上呢
        if (head == null || head.next == null || head.next.next == null)
            return head;
        // 慢指针 一次跳跃一步
        Node slow = head;
        // 快指针 一次跳跃两步
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        // 此时快指针走到尾部，慢指针到中点
        //slow.next = null;
        return slow;
    }

    // 2.输入链表头节点，奇数长度返回中点，偶数长度返回下中点
    public static Node midOrDownMidNode(Node head) {
        // 前提是不是要三个节点以上呢
        if (head == null || head.next == null || head.next.next == null)
            return head;
        // 慢指针 一次跳跃一步
        Node slow = head;
        // 快指针 一次跳跃两步
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        // 此时快指针走到尾部，慢指针到中点
        if (fast.next == null) {
            //slow.next = null;
            return slow;
        }
        //slow.next.next = null;
        return slow.next;
    }

    // 3.输入链表头节点，奇数长度返回中点前一个，偶数长度返回上中点前一个
    public static Node beforeMidOrBeforeUpMidNode(Node head) {
        // 前提是不是要三个节点以上呢
        if (head == null || head.next == null || head.next.next == null)
            return head;
        // 慢指针 一次跳跃一步
        Node slow = head;
        // 快指针 一次跳跃两步
        Node fast = head;
        Node res;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            res = slow;
            slow = slow.next;
            if (fast.next == null) {
                return res;
            }
            if (fast.next.next == null) {
                return res;
            }
        }
        // 此时快指针走到尾部，慢指针到中点
        return slow;
    }

    // 4.输入链表头节点，奇数长度返回中点前一个，偶数长度返回下中点前一个
    public static Node afterMidOrAfterDownMidNode(Node head) {
        // 前提是不是要三个节点以上呢
        if (head == null || head.next == null || head.next.next == null)
            return head;
        // 慢指针 一次跳跃一步
        Node slow = head;
        // 快指针 一次跳跃两步
        Node fast = head;
        Node res;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            res = slow;
            slow = slow.next;
            if (fast.next == null) {
                return res;
            }
            if (fast.next.next == null) {
                return slow;
            }
        }
        // 此时快指针走到尾部，慢指针到中点
        return slow.next;
    }

    public static Node right1(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 1) / 2);
    }


    public static Node right2(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get(arr.size()  / 2);
    }

    public static Node right3(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 3)  / 2);
    }

    public static Node right4(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 2) / 2);
    }

    public static void printlnNode(Node node) {
        Node head = node;
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(+head.value);
            if (head.next != null) {
                sb.append(" -> ");
            }
            head = head.next;
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        Node test = null;
        test = new Node(0);
        test.next = new Node(1);
        test.next.next = new Node(2);
        test.next.next.next = new Node(3);
        test.next.next.next.next = new Node(4);
        test.next.next.next.next.next = new Node(5);
        test.next.next.next.next.next.next = new Node(6);
        test.next.next.next.next.next.next.next = new Node(7);
        test.next.next.next.next.next.next.next.next = new Node(8);
        test.next.next.next.next.next.next.next.next.next = new Node(9);
        printlnNode(test);
 /*          printlnNode(midOrUpMidNode(test));
        printlnNode(right1(test));
        printlnNode(midOrDownMidNode(test));*/
        System.out.println("==");
        System.out.println(right1(test).value);
        System.out.println(midOrUpMidNode(test).value);
        System.out.println("==");
        System.out.println(right2(test).value);
        System.out.println(midOrDownMidNode(test).value);
        System.out.println("==");
        System.out.println(right3(test).value);
        System.out.println(beforeMidOrBeforeUpMidNode(test).value);
        System.out.println("==");
        System.out.println(right4(test).value);
        System.out.println(afterMidOrAfterDownMidNode(test).value);
    }
}
