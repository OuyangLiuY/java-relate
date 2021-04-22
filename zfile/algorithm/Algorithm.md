



# 开始学习算法



## 异或运算

#### 与：&  

**两个位都为1时，结果才为1**

#### 同或：|

**同或运算：相同以1，不同为0**： （两个位都为0时，结果才为0）

#### 异或：^

**异或运算：相同为0，不同为1**

**解释：** 异或运算就记成**无进位相加**！

列子：

```java
int a = 7; //  => 0111
int b = 13; // => 1101
a ^ b = 12; // => 1010   无进位相加
```

性质：

1. 0 ^ N = N
2. N ^ N = 0
3. a ^ b = b ^ a ; a^b^c^d = b^c^d^a

```
1.
2.
3. a & (-a) =  获取到的是a最右边的一个 1
```

**题目一：**如何不用额外变量交换两个数

```java
// 前提：i 和 j 不能是同一个位置
a[i] = a[i] ^ a[j];
a[j] = a[i] ^ a[j];
a[i] = a[i] ^ a[j];
```

**题目二：** 一个数组中有一个数出现了奇数次，其他数都出现了偶数次，怎么找到并打印这种数

```java
 // arr中，只有一种数，出现奇数次
    public static void printOddTimesNum1(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }
```

**题目三：**怎么把一个int类型的数，提取出来最右侧的1来

```javascript
// 当前这个数去反 +1   = 当前这个数的负数
int rightOne = n & ((~n) + 1); // 也等于 n & (-n)
```

**题目四：**一个数组中有两种数出现了奇数次，其他数都出现了偶数次，怎么找到并打印这两种数 

```java
// arr中，有两种数，出现奇数次
    public static void printOddTimesNum2(int[] arr){
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        // eor 的值是这个两个奇数 异或值
        //提取出最右的1
        int rightOne = eor & (-eor);
        int arther = 0;
        for (int i = 0; i < arr.length; i++) {
            // arr[1] = 111100011110000
            // rightOne=000000000010000
            if ((arr[i] & rightOne) != 0){
                arther ^= arr[i];
            }
        }
        System.out.println(arther + " " + (eor ^ arther));
    }
```

**题目五：**

一个数组中有一种数出现K次，其他数都出现了M次，
M > 1,  K < M
找到，出现了K次的数，
要求，额外空间复杂度O(1)，时间复杂度O(N)

```java
// 难道比较高,顺便完成了对数器的模式
public static int hashKTimes(int[] arr, int k, int m) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        int ans = 0;
        for (Integer curr : map.keySet()) {
            if (k == map.get(curr)) {
                ans = curr;
                break;
            }
        }
        return ans;
    }

    // 请保证arr中，只有一种数出现了K次，其他数都出现了M次
    public static int onlyKTimes(int[] arr, int k, int m) {
        int[] t = new int[32];
        // t [0] 位置1出现了几个
        for (int value : arr) {
            for (int i = 0; i < 32; i++) {
                // (value >> i & 1) != 0 依次提取位置上为1
                // 表示第 i 位上为1
                if ((value >> i & 1) != 0) {
                    t[i]++;
                }
            }
        }
        // 将所有的arr中的数，依次按照二进制的位数累加到t这个数组中
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            // t[i] % m != 0 说明这个出现k次的数在第i位上有 1
            if (t[i] % m != 0) {
                ans |= (1 << i);
            }
        }
        return ans;
    }

    public static int[] randomArray(int maxKinds, int range, int k, int m) {
        //数组中有多少种类型的数据
        int numKinds = (int) (Math.random() * maxKinds + 2);
        // 至少需要两种数据
        // 数组长度 = ( k + (numKinds-1) * m)
        int[] arr = new int[k + (numKinds - 1) * m];
        int index = 0;
        int kTimesValue = (int) (Math.random() * range) + 1;
        //填充k次数据
        for (; index < k; index++) {
            arr[index] = kTimesValue;
        }
        numKinds--;
        // 填充M次数据
        HashSet<Integer> set = new HashSet<>();
        set.add(kTimesValue);
        while (numKinds > 0) {
            int currNam;
            do {
                currNam = (int) (Math.random() * range + 1);
            } while (set.contains(currNam));
            set.add(currNam);
            numKinds--;
            for (int j = 0; j < m; j++) {
                arr[index++] = currNam;
            }
        }
        //填充m次数据完成
        //arr中数据太过整齐，需要随机打乱
        for (int i = 0; i < arr.length; i++) {
            // 将arr[i] 上的数据随机跟数组上另外一个位置的数据交换
            int a = (int) (Math.random() * arr.length);
            int tmp = arr[i];
            arr[i] = arr[a];
            arr[a] = tmp;
        }
        return arr;
    }

    public static void main(String[] args) {
        int maxKinds = 10;
        int range = 200;
        int testTimes = 10000;
        int max = 9;
        //System.out.println(Arrays.toString(randomArray(maxKinds, range, 2, 3)));
        System.out.println("测试开始...");
        for (int i = 0; i < testTimes; i++) {
            int a = (int) (Math.random() * max + 1);// a 1 ~ 9
            int b = (int) (Math.random() * max + 1);// b 1 ~ 9
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            if (k == m) {
                m++;
            }
            int[] arr = randomArray(maxKinds, range, k, m);
            int res1 = hashKTimes(arr, k, m);
            int res2 = onlyKTimes(arr, k, m);
            if (res1 != res2) {
                System.out.println("出错了....");
                System.out.println("res1 = " + res1);
                System.out.println("res2 = " + res2);
            }
        }
        System.out.println("测试结束...");
    }
```

## 数据结构

### 链表

```java
/**
 * 链表数据结构
 *
 *   单链表
 */
class Node {
    private int value;
    private Node next;

    public Node(int value) {
        this.value = value;
    }
}

/**
 * 双链表
 */
class DoubleNode{
    private int value;
    private DoubleNode last;
    private DoubleNode next;

    public DoubleNode(int value) {
        this.value = value;
    }
}
```

### 单向链表和双向链表的练习：

链表相关的问题几乎都是coding问题

1. 单链表和双链表如何反转
2. 把给定值都删除

```java
/**
 * 链表数据结构
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
```



```java
// 把给定值都删除
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
```

## 栈和队列

逻辑概念

栈：数据先进后出，犹如弹匣

队列：数据先进先出，好似排队

#### 栈和队列的实际实现

**双向链表实现**

```java
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
```

**使用数组实现队列：**

```java
// 数组方式实现队列
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
```





#### 栈和队列的常见面试题

**题1：**怎么用数组实现不超过固定大小的队列和栈？

栈：正常使用

队列：环形数组



**题2：**实现一个特殊的栈，在基本功能的基础上，再实现返回栈中最小元素的功能

1）pop、push、getMin操作的时间复杂度都是 O(1)。 

2）设计的栈类型可以使用现成的栈结构。 

## 递归

**例子：**

求数组arr[L..R]中的最大值，怎么用递归方法实现。

1）将[L..R]范围分成左右两半。左：[L..Mid]  右[Mid+1..R]
2）左部分求最大值，右部分求最大值
3） [L..R]范围上的最大值，是max{左部分最大值，右部分最大值}

注意：2）是个递归过程，当范围上只有一个数，就可以不用再递归了

```java
 // 求arr中的最大值
    public static int getMax(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }
// 一个最简单的递归调用
    public static int process(int[] arr, int L, int R) {
        // base case , arr[L...R] 范围上只有一个数，直接返回，必须要有的
        if (L == R) {
            return arr[L];
        }
        //(L+R) / 2 == L + ((R - L) >> 1)
        int mid = L + ((R - L) >> 1); // 中点   	1
        int letMax = process(arr, L, mid);
        int rightMax = process(arr, mid + 1, R);
        return Math.max(letMax, rightMax);
    }
```

**递归的脑图和实际实现：**

对于新手来说，把调用的过程画出结构图是必须的，这有利于分析递归
递归并不是玄学，递归底层是利用系统栈来实现的
注意：**任何递归函数都一定可以改成非递归**

**Master公式：**

形如
T(N) = a * T(N/b) + O(N^d)(其中的a、b、d都是常数)
的递归函数，可以直接通过Master公式来确定时间复杂度
如果 log(b,a) < d，复杂度为O(N^d)
如果 log(b,a) > d，复杂度为O(N^log(b,a))
如果 log(b,a) == d，复杂度为O(N^d  * logN)

## 哈希表

1. 哈希表在使用层面上可以理解为一种集合结构
2. 如果只有key，没有伴随数据value，可以使用HashSet结构
3. 如果既有key，又有伴随数据value，可以使用HashMap结构
4. 有无伴随数据，是HashMap和HashSet唯一的区别，实际结构是一回事 
5. 使用哈希表增(put)、删(remove)、改(put)和查(get)的操作，可以认为时间复杂度为 O(1)，但是常数时间比较大 
6. 放入哈希表的东西，如果是基础类型，内部按值传递，内存占用是这个东西的大小 
7. 放入哈希表的东西，如果不是基础类型，内部按引用传递，内存占用是8字节

**注意：**

**哈希表在使用时，增删改查时间复杂度都是O(1)**

## 有序表

1. 有序表在使用层面上可以理解为一种集合结构
2. 如果只有key，没有伴随数据value，可以使用TreeSet结构
3. 如果既有key，又有伴随数据value，可以使用TreeMap结构
4. 有无伴随数据，是TreeSet和TreeMap唯一的区别，底层的实际结构是一回事
5. 有序表把key按照顺序组织起来，而哈希表完全不组织
6. 红黑树、AVL树、size-balance-tree和跳表等都属于有序表结构，只是底层具体实现不同
7. 放入如果是基础类型，内部按值传递，内存占用就是这个东西的大小
8. 放入如果不是基础类型，内部按引用传递，内存占用是8字节
9. 不管是什么底层具体实现，只要是有序表，都有以下固定的基本功能和固定的时间复杂度 

**基本API：**

1)void put(K key, V value)
将一个(key，value)记录加入到表中，或者将key的记录 更新成value。

2)V get(K key)
根据给定的key，查询value并返回。

3)void remove(K key)
移除key的记录。 

4)boolean containsKey(K key)
询问是否有关于key的记录。

5)K firstKey()
返回所有键值的排序结果中，最小的那个。

6)K lastKey()
返回所有键值的排序结果中，最大的那个。

7)K floorKey(K key)
返回<= key 离key最近的那个

8)K ceilingKey(K key）
返回>= key 离key最近的那个

**注意：**

1. **Java中有序表是 TreeMap，而TreeMap是用红黑树实现的**
2. **有序表在使用时，比哈希表功能多，时间复杂度都是O(logN)**



## 归并排序

**递归实现**：

1. 整体是递归，左边排好序+右边排好序+merge让整体有序
2. 让其整体有序的过程里用了排外序方法
3. 利用master公式求解时间复杂度

```java
 // 递归实现
    public static void mergeSortOfRecursion(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int L, int R) {
        // base case 自己写的时候忘记这个临界条件
        if (L == R) {
            return;
        }
        int mid = (L + R) / 2;
        sort(arr, L, mid);
        sort(arr, mid + 1, R);
        merge(arr, mid, L, R);
    }
```



**非递归实现：**

```java
// 归并排序非递归实现
    public static void mergeSortNotRecursion(int[] arr) {
        //
        if (arr == null || arr.length < 2) {
            return;
        }
        int N = arr.length;
        // 步长
        int mergeSize = 1;
        while (mergeSize < N) { // Log N
            // 当前左组的，第一个位置
            int L = 0;
            while (L < N) {
                // 长度不够
                if (mergeSize >= N - L) {
                    break;
                }
                int mid = L + mergeSize - 1;
                int R = mid + Math.min(mergeSize, N - mid - 1);
                merge(arr, mid, L, R);
                L = R + 1;
            }
            // 步长为 2^n
            mergeSize <<= 1;
        }
    }
```

**公共merge方法：**

```java
 private static void merge(int[] arr, int mid, int L, int R) {
        int[] help = new int[R - L + 1];
        int index = 0;
        //左指针（处理左边数据）
        int l = L;
        //右指针（处理右边数据）
        int r = mid + 1;
        //条件:左指针不得大于mid，右指针不得大于R
        while (l <= mid && r <= R) {
            help[index++] = arr[l] <= arr[r] ? arr[l++] : arr[r++];
        }
        // 要么左边数据全部拷贝完毕，只剩右边
        while (r <= R) {
            help[index++] = arr[r++];
        }
        // 要么右边数据全部拷贝完毕，只剩左边
        while (l <= mid) {
            help[index++] = arr[l++];
        }
        //最后将help排好序的数组放进arr中
        System.arraycopy(help, 0, arr, L, help.length);
    }
```

**归并排序复杂度：**

T(N) = 2 * T(N/2) + O(N)

根据master公式可知时间复杂度为O(N * log N)

merge过程需要辅助数组，所以额外空间复杂度为O(N)

**归并排序的实质是把比较行为变为有序信息传递，比O(N ^ 2)要快**

### 题目1：

在一个数组中，一个数左边比它小的数的总和，叫数的小和，所有数的小和累加起来，叫数组小和。求数组小和。
例子： [1,3,4,2,5] 
1左边比1小的数：没有
3左边比3小的数：1
4左边比4小的数：1、3
2左边比2小的数：1
5左边比5小的数：1、3、4、 2		
所以数组的小和为1+1+3+1+1+3+4+2=16 

```java
public static int minSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    // arr[L..R]既要排好序，也要求小和返回
    // 所有merge时，产生的小和，累加
    // 左 排序   merge
    // 右 排序  merge
    // merge
    private static int process(int[] arr, int L, int R) {
        if (L == R) {
            return 0;
        }
        int mid = (R + L) / 2;
        return process(arr, L, mid) +
                process(arr, mid + 1, R) +
                merge(arr, L, mid, R);
    }

    private static int merge(int[] arr, int L, int M, int R) {
        int[] help = new int[R - L + 1];
        int index = 0;
        int p1 = L;
        int p2 = M + 1;
        int sum = 0;
        while (p1 <= M && p2 <= R) {
            //先求值，再排序
            sum += arr[p1] < arr[p2]? (R- p2 +1) * arr[p1] : 0;
            help[index++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M){
            help[index ++] = arr[p1++];
        }
        while (p2 <= R){
            help[index ++] = arr[p2++];
        }
        System.arraycopy(help,0,arr,L,help.length);
        return sum;
    }
    public static int minSumForComp(int[]arr){
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                sum += arr[j] < arr[i] ? arr[j] : 0;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] arr1 = {1,3,4,2,5};
        System.out.println(minSum(arr1));
        int[] arr2 = {1,3,4,2,5};
        System.out.println(minSumForComp(arr2));
    }		
```



### 题目2：

在一个数组中，
任何一个前面的数a，和任何一个后面的数b，
如果(a,b)是降序的，就称为逆序对
返回数组中所有的逆序对

```java
public static int reverseNum(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }

    private static int process(int[] arr, int L, int R) {
        if (L == R) {
            return 0;
        }
        int mid = L + ((R - L) >> 1);
        return process(arr, L, mid) + process(arr, mid + 1, R) + merge(arr, L, mid, R);
    }

    private static int merge(int[] arr, int L, int M, int R) {
        int[] help = new int[R - L + 1];
        // 反过来做
        int index = help.length - 1;
        int p1 = M;
        int p2 = R;
        int sum = 0;
        while (p1 >= L && p2 > M) {
            //先求值，再排序,有多少对？ 右边 - mid
            sum += arr[p1] > arr[p2] ? (p2 - M) : 0;
            help[index--] = arr[p1] > arr[p2] ? arr[p1--] : arr[p2--];
        }
        while (p1 >= L) {
            help[index--] = arr[p1--];
        }
        while (p2 > M) {
            help[index--] = arr[p2--];
        }
        System.arraycopy(help, 0, arr, L, help.length);
        return sum;
    }

    public static int comparator(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 3, 4, 2, 5};
        System.out.println(reverseNum(arr1));
        int[] arr2 = {1, 3, 4, 2, 5};
        System.out.println(comparator(arr2));
    }		
```



### 题目3：

在一个数组中，
对于每个数num，求有多少个后面的数 * 2 依然<num，求总个数
比如：[3,1,7,0,2]
3的后面有：1，0
1的后面有：0
7的后面有：0，2
0的后面没有
2的后面没有
所以总共有5个