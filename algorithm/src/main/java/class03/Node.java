package class03;

/**
 * 链表数据结构
 *
 *
 *   单链表
 */
public class Node {
    public int value;
    public Node next;

    public Node(int value) {
        this.value = value;
    }
}

/**
 * 双链表
 */
class DoubleNode{
    public int value;
    public DoubleNode last;
    public DoubleNode next;

    public DoubleNode(int value) {
        this.value = value;
    }
}

class Main{
    // 反转单链表
    // a -> b -> c -> null
    // c -> b -> a -> null
    public static Node converseNodeList(Node head){

        println(head);
        //head = a
        Node pre = null;
        Node next = null;
        while (head != null){
            //1. 获取head.next给到next节点
            next = head.next;

            //2. 反转之后head.next节点应该指向前置节点pre
            head.next = pre;
            //3. pre节点为当前的head节点
            pre = head;

            //4. 将head 指向 head.next，以此来将整个链表循环
            head = next;
        }
        println(pre);
        return pre;
    }

    // 反转双向链表
    // a -> b -> c -> null
    // c -> b -> a -> null
    public static DoubleNode converseDoubleNodeList(DoubleNode head){
        printlnDoubleNode(head);
        // 反转之后节点对象
        DoubleNode pre = null;
        DoubleNode next = null;
        while (head != null){
            next = head.next;
            head.next = pre;
            head.last = next;
            pre = head;
            head = next;
        }
        printlnDoubleNode(pre);
        return pre;
    }

    private static void printlnDoubleNode(DoubleNode head) {
        // 打印当前双链表节点数据
        StringBuilder sb = new StringBuilder();
        while (head != null){
            int value = head.value;
            sb.append(value);
            if(head.next != null){
                sb.append(" <=> ");
            }
            head = head.next;
        }
        System.out.println(sb);
    }

    private static void println(Node head) {
        // 打印当前单链表节点数据
        Node next = null;
        StringBuilder sb = new StringBuilder();
        while (head != null){
            int value = head.value;
            sb.append(value);
            if(head.next != null){
                sb.append(" -> ");
            }
            //next =
            head = head.next;
        }
        System.out.println(sb);
    }

    // 生成单链表
    public static Node generateRandomLinkedList(int len, int value) {
        int size = (int) (Math.random() * (len + 1));
        if (size == 0) {
            return null;
        }
        size--;
        Node head = new Node((int) (Math.random() * (value + 1)));
        Node pre = head;
        while (size != 0) {
            Node cur = new Node((int) (Math.random() * (value + 1)));
            pre.next = cur;
            // 指针改变
            pre = cur;
            size--;
        }
        return head;
    }

    // 生成双向链表
    public static DoubleNode generateRandomDoubleLinkedList(int len, int value) {
        int size = (int) (Math.random() * (len + 1));
        if (size == 0) {
            return null;
        }
        size--;
        DoubleNode head = new DoubleNode((int) (Math.random() * (value + 1)));
        // 相当于是一个指针去操作链表 假定是头节点
        DoubleNode pre = head;
        while (size != 0) {
            DoubleNode cur = new DoubleNode((int) (Math.random() * (value + 1)));
            pre.next = cur;
            cur.last = pre;
            // 指针改变
            pre = cur;
            size--;
        }
        return head;
    }

    public static void main(String[] args) {
        int len = 50;
        int value = 100;
        Node node1 = generateRandomLinkedList(len, value);
        node1 = converseNodeList(node1);

        DoubleNode node2 = generateRandomDoubleLinkedList(len,value);
        node2 = converseDoubleNodeList(node2);
    }
}



