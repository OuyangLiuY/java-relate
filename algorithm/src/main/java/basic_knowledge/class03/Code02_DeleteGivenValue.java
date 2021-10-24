package basic_knowledge.class03;

public class Code02_DeleteGivenValue {

    // 链表结构
    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public static Node removeValue(Node head,int value ){
        //在链表中删除这个值，临界条件，需找到第一个值不为value的节点
        while (head != null){
            if(head.value != value){
                break;
            }
            head = head.next;
        }
        //pre是返回的新的node数据
        Node pre = head;
        Node cur = head;
        while (cur != null){
            if(cur.value == value){
                //改变链表的指向位置
                pre.next = cur.next;
            }else {
                pre = cur;
            }
            cur = cur.next;
        }
        return pre;
    }
}
