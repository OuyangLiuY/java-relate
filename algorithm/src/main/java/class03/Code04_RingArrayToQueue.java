package class03;

/**
 * 使用数组实现队列
 */
public class Code04_RingArrayToQueue {
    public static class MyQueue{
        private final int[] arr;
        private int pushI;
        private int popI;
        private int size;
        // 当前数组长度限制
        private  int limit;
        public MyQueue(int limit){
            arr = new int[limit];
            size = 0;
            pushI = 0;
            popI = 0;
        }

        public void push(int v){
            if(size == limit){
                throw new RuntimeException("队列满了，不能在添加数据");
            }
            size ++;
            // 添值
            arr[pushI] = v;
            // beginI 下标加 1
            pushI = nextIndex(pushI);
        }
        public int pop(){
            if(size == 0){
                throw  new RuntimeException("队列空了，不能再获取数据");
            }
            size --;
            int ans = arr[popI] ;
            popI = nextIndex(popI);
            return ans;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private int nextIndex(int index) {
            return index < limit - 1 ? index + 1 : 0 ;
        }
    }
}
