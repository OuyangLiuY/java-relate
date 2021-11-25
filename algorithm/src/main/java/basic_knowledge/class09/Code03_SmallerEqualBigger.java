package basic_knowledge.class09;

/**
 * **题目二：**将单向链表按某值划分成左边小、中间相等、右边大的形式
 * <p>
 * 1）把链表放入数组里，在数组上做partition（笔试用）
 * 2）分成小、中、大三部分，再把各个部分之间串起来（面试用）
 */
public class Code03_SmallerEqualBigger {
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }
    // 使用数组方式实现
    public static Node listPartition1(Node head, int pivot) {
        if (head == null)
            return null;
        Node cur = head;
        int size = 0;
        while (cur != null) {
            size++;
            cur = cur.next;
        }
        Node[] nodes = new Node[size];
        cur = head;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = cur;
            cur = cur.next;
        }
        arrPartition(nodes, pivot);
        for (int i = 1; i < nodes.length; i++) {
            nodes[i - 1].next = nodes[i];
        }
        nodes[nodes.length - 1].next = null;
        return nodes[0];
    }

    private static void arrPartition(Node[] nodes, int pivot) {
        int L = 0;
        int R = nodes.length;
        int small = L - 1;
        int big = R;
        int index = 0;
        while (index != big) {
            if (nodes[index].value < pivot) {
//                swap(nodes,small+ 1,index);
//                small ++;
//                index ++; // 等同于一下代码
                swap(nodes, ++small, index++);
            } else if (nodes[index].value == pivot) {
                index++;
            } else {
                swap(nodes, --big, index);
            }
        }

    }

    public static void swap(Node[] nodeArr, int a, int b) {
        Node tmp = nodeArr[a];
        nodeArr[a] = nodeArr[b];
        nodeArr[b] = tmp;
    }

    public static Node listPartition2(Node head, int pivot) {
        //
        Node sh = null; // small head
        Node st = null; // small tail
        Node eh = null; // equal head
        Node et = null; // equal tail
        Node bh = null; // big head
        Node bt = null; // big tail
        Node next = null;
        while (head != null){
            next = head.next;
            head.next = null;
            if(head.value < pivot){
                if(sh ==null){
                    sh = head;
                }else {
                    st.next =head;
                }
                st = head;
            }else if(head.value == pivot){
                if(eh == null){
                    eh = head;
                }else {
                    et.next =head;
                }
                et = head;
            }else {
                if(bh == null){
                    bh = head;
                }else {
                    bt.next =head;
                }
                bt = head;
            }
            head = next;
        }
        // 开始将链表拼接起来
        if(st !=null){ //说明又小于区域
            st.next = eh;
            et = et ==null ? st : et; // 谁去链接大于区域的投，谁就变成了et
        }
        // 下一步，一定是需要用eT 去接 大于区域的头
        // 有等于区域，eT -> 等于区域的尾结点
        // 无等于区域，eT -> 小于区域的尾结点
        if(et != null){
            et.next = bh;
        }
        return sh != null ? sh : (eh != null ? eh : bh);
    }

    public static void printLinkedList(Node node) {
        System.out.print("Linked List: ");
        while (node != null) {
            System.out.print(node.value + " ");
            node = node.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head1 = new Node(7);
        head1.next = new Node(9);
        head1.next.next = new Node(1);
        head1.next.next.next = new Node(8);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(2);
        head1.next.next.next.next.next.next = new Node(5);
        printLinkedList(head1);
        head1 = listPartition1(head1, 5);
//        head1 = listPartition2(head1, 5);
        printLinkedList(head1);

    }
}
