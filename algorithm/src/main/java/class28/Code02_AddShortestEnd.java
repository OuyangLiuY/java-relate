package class28;

/**
 *
 */
public class Code02_AddShortestEnd {


    public static String shortestEnd(String s) {
        if (s == null || s.length() == 0) {
            return null;
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
        int maxContainsEnd = -1;
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
           if(R == str.length){
               maxContainsEnd = pArr[i];
               break;
           }
        }
        char[] res = new char[s.length() - maxContainsEnd + 1];
        for (int i = 0; i < res.length; i++) {
            // 从最后起            // str从开始位置起
            res[res.length - 1 - i] = str[i * 2 + 1];
        }
        return String.valueOf(res);
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
    public static void main(String[] args) {
        String str1 = "abcd123321";
        System.out.println(shortestEnd(str1));
    }
}
