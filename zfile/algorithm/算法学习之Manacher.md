## Manacher算法

假设字符串str长度为N，想返回最长回文子串的长度

时间复杂度O(N)

## Manacher算法核心

1）理解回文半径数组

2）理解所有中心的回文最右边界R，和取得R时的中心点C

3）理解   L…(i`)…C…(i)…R  的结构，以及根据i’回文长度进行的状况划分

4）每一种情况划分，都可以加速求解i回文半径的过程

### 暴力实现：

```java
public static int right(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    char[] str = manacherString(s);
    int max = 0;
    for (int i = 0; i < str.length; i++) {
        int L = i - 1;
        int R = i + 1;
        while (L > 0 && R < str.length && str[L] == str[R]) {
            L--;
            R++;
        }
        max = Math.max(max, R - L + 1);
    }
    return max / 2;
}
```

### Manacher实现：

```java
public static int manacher(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    // "12132" -> "#1#2#1#3#2#"
    char[] str = manacherString(s);
    // 回文半径的大小
    int[] pArr = new int[str.length];
    int C = -1;
    // 讲述中：R代表最右的扩成功的位置
    // coding：最右的扩成功位置的，再下一个位置
    int R = -1;
    int max = Integer.MIN_VALUE;
    for (int i = 0; i < str.length; i++) {
        // R第一个违规的位置，i>= R
        // i位置扩出来的答案，i位置扩的区域，至少是多大。
        // 2 *C - i 数组种根据i对称得左边位置
        pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;
        // 不是最后一个位置，也不是第一个位置
        while (i + pArr[i] < str.length && i - pArr[i] > -1) {
            if (str[i + pArr[i]] == str[i - pArr[i]]) {
                pArr[i]++;
            } else {
                break;
            }
        }
        if (i + pArr[i] > R) {
            R = i + pArr[i];
            C = i;
        }
        max = Math.max(max, pArr[i]);
    }
    return max - 1;
}

private static char[] manacherString(String str) {
    char[] res = new char[str.length() * 2 + 1];
    char[] chars = str.toCharArray();
    int index = 0;
    for (int i = 0; i != res.length; i++) {
        res[i] = (i & 1) == 0 ? '#' : chars[index++];
    }
    return res;
}
```