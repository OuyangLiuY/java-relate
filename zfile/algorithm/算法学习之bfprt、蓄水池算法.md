# 在无序数组中求第K小的数

## 1) 堆实现方式

```java
static class MaxHeapComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
        return o2 - o1;
    }
}

// 利用大根堆，时间复杂度O(N*logK)
public static int minKth1(int[] arr, int k) {
    PriorityQueue<Integer> queue = new PriorityQueue<>(new MaxHeapComparator());
    // 先放入前k个数
    for (int i = 0; i < k; i++) {
        queue.add(arr[i]);
    }
    for (int i = k; i < arr.length; i++) {
        if (queue.peek() > arr[i]) {
            queue.poll();
            queue.add(arr[i]);
        }
    }
    return queue.peek();
}
```

## 2）改写快排的方法

```java
// 改写快排，时间复杂度O(N)
// k >= 1
public static int minKth2(int[] array, int k) {
    int[] arr = Arrays.copyOfRange(array, 0, array.length);
    return process2(arr, 0, arr.length - 1, k - 1);
}


private static int process2(int[] arr, int L, int R, int index) {
    if (L == R) {
        return arr[L];
    }
    // 不止一个数  L +  [0, R -L]
    //int pivot = arr[L + (int) (Math.random() * (R - L + 1))];
    int pivot = arr[(R + L) / 2];
    int[] range = partition(arr, L, R, pivot);
    if (index >= range[0] && index <= range[1]) {
        return arr[index];
    } else if (index < range[0]) {
        return process2(arr, L, range[0] - 1, index);
    } else {
        return process2(arr, range[1] + 1, R, index);
    }
}


private static int[] partition(int[] arr, int L, int R, int pivot) {
    int less = L - 1;
    int cur = L;
    int more = R + 1;
    while (cur < more) {
        if (arr[cur] < pivot) {
            SortUtils.swap(arr, ++less, cur++);
        } else if (arr[cur] > pivot) {
            SortUtils.swap(arr, cur, --more);
        } else {
            cur++;
        }
    }
    return new int[]{less + 1, more - 1};
}
```

## 3）bfprt算法：

思想：

1. 求出特殊值 P

```shell
求P步骤：
1. 脑子中想象无序数组中，每5个数为一组
2.将这每5个数中自己排序，整体是无序得，需要时间复杂O(N)
3.拿出每小组中得中位数，组成一个新小组,复杂度T(N/5)
4.拿出新得数组这个中位数就是P
```

2. 快排第二步骤， 小于P得放左边，等于P得放中间，大于P得放右边(<P  =P >P)  
3. 求剩余小于P得部分数据  ，时间复杂度T(7/10N)

```
T(7/10N) 来源：
据分析：
因为求得大于P得至少数是(0.3N) = (1/10 N *3)
所以，小于P得最多是7/10
```

**时间复杂度：** T(N) = T(N/5) + T(7/10N) + O(N)

**备注：**算法导论9.3节证明该复杂的为O(N)

```java
public static int minKth3(int[] array, int k) {
    int[] arr = Arrays.copyOfRange(array, 0, array.length);
    return process3(arr, 0, arr.length - 1, k - 1);
}

// arr 第k小的数
// process2(arr, 0, N-1, k-1)
// arr[L..R]  范围上，如果排序的话(不是真的去排序)，找位于index的数
// index [L..R]
public static int process3(int[] arr, int L, int R, int index) {
    if (L == R) { // L = =R ==INDEX
        return arr[L];
    }
    // 不止一个数  L +  [0, R -L]
    int pivot = arr[L + (int) (Math.random() * (R - L + 1))];
    int[] range = partition3(arr, L, R, pivot);
    if (index >= range[0] && index <= range[1]) {
        return arr[index];
    } else if (index < range[0]) {
        return process3(arr, L, range[0] - 1, index);
    } else {
        return process3(arr, range[1] + 1, R, index);
    }
}

public static int[] partition3(int[] arr, int L, int R, int pivot) {
    int less = L - 1;
    int more = R + 1;
    int cur = L;
    while (cur < more) {
        if (arr[cur] < pivot) {
            swap(arr, ++less, cur++);
        } else if (arr[cur] > pivot) {  /// more index位置先改变再交换，
            swap(arr, cur, --more);
        } else {
            cur++;
        }
    }
    return new int[]{less + 1, more - 1};
}

public static void swap(int[] arr, int i1, int i2) {
    int tmp = arr[i1];
    arr[i1] = arr[i2];
    arr[i2] = tmp;
}
```

## 题目：

给定一个无序数组arr中，长度为N，给定一个正数k，返回top k个最大的数
不同时间复杂度三个方法：
1）O(N*logN)*

```java
//1 时间复杂度O(N*logN)
// 排序+收集
public static int[] maxTopK1(int[] arr, int k) {
    if (arr == null || arr.length == 0) {
        return new int[0];
    }
    int N = arr.length;
    k = Math.min(N, k);
    int[] res = new int[k];
    Arrays.sort(arr);//系统排序
    for (int i = N - 1, j = 0; j < k; i--, j++) {
        res[j] = arr[i];
    }
    return res;
}
```

2）O(N + K*logN)*

```java
//1 时间复杂度O(N + K*logN)
// 堆排序 + 收集 (加强堆)
public static int[] maxTopK2(int[] arr, int k) {
    if (arr == null || arr.length == 0) {
        return new int[0];
    }
    int N = arr.length;
    for (int i = N - 1; i >= 0; i--) {
        heapify(arr, i, N);
    }
    k = Math.min(N, k);
    // 只把前K个数放在arr末尾，然后收集，O(K*logN)
    int heapSize = N;
    int count = 1;
    swap(arr, 0, --heapSize);
    while (heapSize > 0 && count < k) {
        heapify(arr, 0, heapSize);
        swap(arr, 0, --heapSize);
        count++;
    }
    int[] ans = new int[k];
    for (int i = N - 1, j = 0; j < k; i--, j++) {
        ans[j] = arr[i];
    }
    return ans;
}

public static void heapInsert(int[] arr, int index) {
    while (arr[index] > arr[(index - 1) / 2]) {
        swap(arr, index, (index - 1) / 2);
        index = (index - 1) / 2;
    }
}

public static void swap(int[] arr, int i, int j) {
    int tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
}

public static void heapify(int[] arr, int index, int heapSize) {
    int left = index * 2 + 1;
    while (left < heapSize) {
        int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
        largest = arr[largest] > arr[index] ? largest : index;
        if (largest == index) {
            break;
        }
        swap(arr, largest, index);
        index = largest;
        left = index * 2 + 1;
    }
}
```

3）O(n + k*logk)

```java
// 方法三，时间复杂度O(n + k * logk)
// 使用快排思想，求出最小 得第K小得数，那么剩余只要大于得就是要得答案
public static int[] maxTopK3(int[] arr, int k) {
    if (arr == null || arr.length == 0) {
        return new int[0];
    }
    int N = arr.length;
    k = Math.min(N, k);
    // O(N)
    int num = minKth(arr, N - k); //获取第 （N-K）小得数
    int[] ans = new int[k];
    int index = 0;
    for (int i = 0; i < N; i++) {
        // 获取大于num得数
        if (arr[i] > num) {
            ans[index++] = arr[i];
        }
    }
    // 说明这里得数是等于得数
    for (; index < k; index++) {
        ans[index] = num;
    }
    // O(k*logk)
    Arrays.sort(ans);
    for (int L = 0, R = k - 1; L < R; L++, R--) {
        swap(ans, L, R);
    }
    return ans;
}
// 求出第index小得数
private static int minKth(int[] arr, int index) {
    int L = 0;
    int R = arr.length - 1;
    int pivot = 0;
    int[] range = null;
    while (L < R) {
        pivot = arr[L + (int) (Math.random() * (R - L + 1))];
        range = partition(arr, L, R, pivot);
        if (index < range[0]) {
            R = range[0] - 1;
        } else if (index > range[1]) {
            L = range[1] + 1;
        } else {
            return pivot;
        }
    }
    return arr[L];
}

private static int[] partition(int[] arr, int L, int R, int pivot) {
    int less = L - 1;
    int more = R + 1;
    int cur = L;
    while (cur < more) {
        if (arr[cur] < pivot) {
            swap(arr, cur++, ++less);
        } else if (arr[cur] > pivot) {
            swap(arr, cur, --more);
        } else {
            cur++;
        }
    }
    return new int[]{less + 1, more - 1};
}
```

## 蓄水池算法：

解决的问题：

假设有一个源源吐出不同球的机器，

只有装下10个球的袋子，每一个吐出的球，要么放入袋子，要么永远扔掉

如何做到机器吐出每一个球之后，所有吐出的球都等概率被放进袋子里

```java
public static class RandomBox {
    private int[] bag;//奖池
    private int N;
    private int count;
    public RandomBox(int capacity) {
        bag = new int[capacity];
        N = capacity;
        count = 0;
    }
    // 概率返回1~max 位置数
    private int rand(int max) {
        return (int) (Math.random() * max) + 1;
    }
    public void add(int num) {
        count++;
        if(count <= N){
            bag[count-1] = num;
        }else {
            if(rand(count) <= N){ //必须再N范围内
                bag[rand(N) - 1] = num; // 再N内随机替换一个位置，用num代替
            }
        }
    }

    public int[] choices() {
        int[] ans = new int[N];
        for (int i = 0; i < N; i++) {
            ans[i] = bag[i];
        }
        return ans;
    }
}
// 等概率返回1~N 位置得数
public static int random(int N){
    return (int) (Math.random()*N + 1);
}


public static void main(String[] args) {

    System.out.println("hello");
    int all = 100;
    int choose = 10;
    int testTimes = 50000;
    int[] counts = new int[all + 1];
    for (int i = 0; i < testTimes; i++) {
        RandomBox box = new RandomBox(choose);
        for (int num = 1; num <= all; num++) {
            box.add(num);
        }
        // 选择N个
        int[] ans = box.choices();
        for (int j = 0; j < ans.length; j++) {
            counts[ans[j]]++;
        }
    }
    //选择每个位置得数是等概率得

    for (int i = 0; i < counts.length; i++) {
        System.out.println(i + " times : " + counts[i]);
    }
}
```

## 蓄水池算法场景：

解决：时时刻刻中奖得前100名用户

**思路：** 前一百名用户第一次登录就记录，然后依次等概率，等下一个N用户第一次登录之后算出中奖概率，如果中奖将这个用户放入奖池中，从原来得奖池中随机等概率剔除掉一个中奖用户。那么任何地方登录得用户中奖得概率都相同，且是等概率得。

# 经典生成UUID算法：

全球任何地方得东西，生成不同ID且等概率，并且是不能重复得

思路：只需一台机器管理，机器记录两个数据（base数据，和range），使用range去解决并发度，总机器下管理了各个机器（国家），各个机器下还管理了其他各个机器（省），依次类推。

比如：总机器：base=10000开始，range到2000结束 ，