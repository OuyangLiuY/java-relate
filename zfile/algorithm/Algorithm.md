



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



