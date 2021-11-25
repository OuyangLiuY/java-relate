package basic_knowledge.class03;

import java.util.Stack;

public class Code07_TwoStacksImplementQueue {
    public static class TwoStacksQueue<T>{
        private Stack<T> stackPush;
        private Stack<T> stackPop;

        public TwoStacksQueue() {
            this.stackPush = new Stack<>();
            this.stackPop = new Stack<>();
        }

        private void add(T v){
            stackPush.push(v);
            pushToPop();
        }
        private T poll(){
            pushToPop();
            return stackPop.pop();
        }
        // push stack : 1 , 2 , 3
        // push pop: 1
        public T peek() {
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Queue is empty!");
            }
            pushToPop();
            return stackPop.peek();
        }
        private void pushToPop() {
            if(stackPop.empty()){
                while (!stackPush.empty()){
                    stackPop.push(stackPush.pop());
                }
            }
        }
    }

    public static void main(String[] args) {
        TwoStacksQueue test = new TwoStacksQueue();
        test.add(1);
        test.add(2);
        test.add(3);
        System.out.println(test.peek());
        System.out.println(test.poll());
        System.out.println(test.peek());
        System.out.println(test.poll());
        System.out.println(test.peek());
        System.out.println(test.poll());
    }
}
