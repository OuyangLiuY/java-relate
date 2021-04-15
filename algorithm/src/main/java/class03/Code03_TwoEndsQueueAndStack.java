package class03;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 使用双向链表实现栈和队列
 */
public class Code03_TwoEndsQueueAndStack {

    public static class Node<T> {
        public T value;
        public Node<T> last;
        public Node<T> next;

        public Node(T data) {
            value = data;
        }
    }
    // 中间队列（双端队列）
    public static class DoubleEndsQueue<T>{
        public Node<T> head;
        public Node<T> tail;
        // 从头添加数据
        public void  addFromHead(T value){
            Node<T> cur = new Node<>(value);
            if(head == null){
                head = cur;
                tail = cur;
            }else {
                // 从头添加
                cur.next = head;
                head.last = cur;
                // 指针改变
                head = cur;
            }
        }
        // 从尾部添加数据
        public void addFromTail(T value){
            Node<T> cur = new Node<>(value);
            if(tail == null){
                head = cur;
                tail = cur;
            }else {
                // 从尾添加
                cur.last = tail;
                tail.next = cur;
                // 指针改变
                tail = cur;
            }
        }
        // 从头拿取数据
        public T popFromHead(){
            if(head == null){
                return null;
            }
            Node<T> cur = head;
           if(tail == head){
               tail = null;
               head = null;
           }else {
               // 指针改变 head指向下一个节点
               head = head.next;
               //断链只返回这个cur的值，而不是整个head
               cur.next = null;
               head.last = null;
           }
           return cur.value;
        }
        // 从尾部拿取数据
        public T popFromTail(){
            if(tail == null){
                return null;
            }
            Node<T> cur = tail;
            if(tail == head){
                tail = null;
                head = null;
            }else {
                // 指针改变 tail 指向上一个节点
                tail = tail.last;
                //断链只返回这个cur的值，而不是整个head
                cur.last = null;
                head.next = null;
            }
            return cur.value;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }
    public static class MyStack<T>{
        public DoubleEndsQueue<T> queue;
        public MyStack (){
            this.queue = new DoubleEndsQueue<>();
        }
        public void push(T v){
            // 从头加
            queue.addFromHead(v);
        }
        public T pop(){
            // 从头弹
            return queue.popFromHead();
        }
        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }
    public static class MyQueue<T>{
        public DoubleEndsQueue<T> queue;
        public MyQueue (){
            this.queue = new DoubleEndsQueue<>();
        }
        public void push(T v){
            queue.addFromHead(v);
        }
        public T pop(){
            return queue.popFromTail();
        }
        public boolean isEmpty(){
            return queue.isEmpty();
        }
    }
    public static boolean isEqual(Integer o1, Integer o2) {
        if (o1 == null && o2 != null) {
            return false;
        }
        if (o1 != null && o2 == null) {
            return false;
        }
        if (o1 == null && o2 == null) {
            return true;
        }
        return o1.equals(o2);
    }

    public static void main(String[] args) {
        int oneTestDataNum = 100;
        int value = 10000;
        int testTimes = 100000;
        for (int i = 0; i < testTimes; i++) {
            MyStack<Integer> myStack = new MyStack<>();
            MyQueue<Integer> myQueue = new MyQueue<>();
            Stack<Integer> stack = new Stack<>();
            Queue<Integer> queue = new LinkedList<>();
            for (int j = 0; j < oneTestDataNum; j++) {
                int nums = (int) (Math.random() * value);
                if (stack.isEmpty()) {
                    myStack.push(nums);
                    stack.push(nums);
                } else {
                    if (Math.random() < 0.5) {
                        myStack.push(nums);
                        stack.push(nums);
                    } else {
                        if (!isEqual(myStack.pop(), stack.pop())) {
                            System.out.println("oops!");
                        }
                    }
                }
                int numq = (int) (Math.random() * value);
                if (stack.isEmpty()) {
                    myQueue.push(numq);
                    queue.offer(numq);
                } else {
                    if (Math.random() < 0.5) {
                        myQueue.push(numq);
                        queue.offer(numq);
                    } else {
                        if (!isEqual(myQueue.pop(), queue.poll())) {
                            System.out.println("oops!");
                        }
                    }
                }
            }
        }
        System.out.println("finish!");
    }
}
