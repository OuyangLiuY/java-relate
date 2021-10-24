package basic_knowledge.class03;

import java.util.LinkedList;
import java.util.Queue;

public class Code06_TwoQueuesImplementStack {
    public static class TwoQueueStack<T>{
        private Queue<T> queue;
        private Queue<T> help;

        public TwoQueueStack() {
            this.queue = new LinkedList<>();
            this.help = new LinkedList<>();
        }

        private void push(T v){
            queue.offer(v);
        }
        private T poll(){
            while (queue.size() > 1){
                help.offer(queue.poll());
            }
            T ans = queue.poll();
            //help.offer(ans);
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }
        private T peek(){
            while (queue.size() > 1){
                help.offer(queue.poll());
            }
            T ans = queue.poll();
            help.offer(ans);
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }
    }
}
