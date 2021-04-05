



# 开始学习算法



## 异或运算

#### 同或：|

**同或运算：相同以1，不同为0**

#### 异或：^

**异或运算：相同为0，不同为1**

**解释：** 异或运算就记成无进位相加！

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

题目二：怎么把一个int类型的数，提取出来最右侧的1来

```java
// 当前这个数去反 +1   = 当前这个数的负数
int rightOne = n & ((~n) + 1); // 也等于 n & (-n)
```

题目四：一个数组中有两种数出现了奇数次，其他数都出现了偶数次，怎么找到并打印这两种数 

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

题目5：

一个数组中有一种数出现K次，其他数都出现了M次，
M > 1,  K < M
找到，出现了K次的数，
要求，额外空间复杂度O(1)，时间复杂度O(N)

```java
// 难道比较高
```



