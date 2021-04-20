package class03;

import java.util.Stack;

public class Code05_GetMinStack {
    public static class MyStack<T>{
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MyStack() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }
        public void push(Integer v){
            if(stackMin.isEmpty()){
                stackMin.push(v);
            }else if(v <= this.getMin()){
                this.stackMin.push(v);
            }
            this.stackData.push(v);
        }
        public Integer getMin(){
            if(stackMin.isEmpty()){
                throw new RuntimeException("stack is empty,");
            }
            return stackMin.peek();
        }
        public int pop() {
            if (this.stackData.isEmpty()) {
                throw new RuntimeException("Your stack is empty.");
            }
            int value = this.stackData.pop();
            if (value == this.getMin()) {
                this.stackMin.pop();
            }
            return value;
        }
    }

    public static void main(String[] args) {
        MyStack stack1 = new MyStack<>();
        stack1.push(3);
        System.out.println(stack1.getMin());
        stack1.push(4);
        System.out.println(stack1.getMin());
        stack1.push(1);
        System.out.println(stack1.getMin());
        System.out.println(stack1.pop());
        System.out.println(stack1.getMin());
    }
}
