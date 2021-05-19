package class10;

/**
 * 递归实现树结构遍历
 */
public class Code01_RecursiveTraversalBT {

    public static class  Node{
        public int value;
        public Node left;
        public Node right;
        public Node(int v){
            value = v;
        }
    }
    public static void  f(Node head){
        if(head == null)
            return ;
        // 1 此处打印输出，就是先序遍历
        f(head.left);
        // 2 此处打印输出，就是中序遍历
        f(head.right);
        // 3 此处打印输出，就是后序遍历
    }
    // 先序打印所有节点
    // 先序
    public static void  pre(Node head){
        if(head == null)
            return ;
        // 1 此处打印输出，就是先序遍历
        System.out.println(head.value);
        pre(head.left);
        pre(head.right);
    }
    // 中序
    public static void  mid(Node head){
        if(head == null)
            return ;
        mid(head.left);
        System.out.println(head.value);
        mid(head.right);
    }

    // 后序
    public static void  pos(Node head){
        if(head == null)
            return ;
        pos(head.left);
        pos(head.right);
        System.out.println(head.value);
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
        pos(head);
        System.out.println("========");

    }
}
