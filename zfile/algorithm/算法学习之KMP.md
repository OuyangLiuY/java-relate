## KMP算法

假设字符串str长度为N，字符串match长度为M，M <= N

想确定str中是否有某个子串是等于match的。

时间复杂度O(N)

## KMP算法核心

1）如何理解next数组

2）如何利用next数组加速匹配过程，优化时的两个实质！（私货解释）

### KMP算法实现

```java
public static int getIndexOf(String s1, String s2) {
    if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
        return -1;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    //O(M)
    int[] next = getNextArray(str2);
    int x = 0;
    int y = 0;
    //O(N)
    while (x < str1.length && y < str2.length) {
        if (str1[x] == str2[y]) {
            x++;
            y++;
        } else if (next[y] == -1) { //str1开头数组没有匹配到str2开头
            x++;
        } else {
            y = next[y];
        }
    }
    return y == str2.length ? x - y : -1;
}

private static int[] getNextArray(char[] str2) {
    if (str2.length == 1) {
        return new int[]{-1};
    }
    int[] next = new int[str2.length];
    next[0] = -1;
    next[1] = 0;
    int index = 2;//从2位置开始
    int cn = 0; // 当前是哪个位置的值再和i-1位置的字符比较
    while (index < next.length) {
        if (str2[index - 1] == str2[cn]) {
            next[index++] = ++cn;
        } else if (cn > 0) {
            cn = next[cn];
        } else {
            next[index++] = 0;
        }
    }
    return next;
}
```