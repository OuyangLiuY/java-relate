package class27;

public class Code01_KMP {

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

    // for test
    public static String getRandomString(int possibilities, int size) {
        char[] ans = new char[(int) (Math.random() * size) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
        }
        return String.valueOf(ans);
    }

    public static void main(String[] args) {
        int possibilities = 5;
        int strSize = 100000000;
        int matchSize = 20;
        int testTimes = 1;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            String str = getRandomString(possibilities, strSize);
            String match = getRandomString(possibilities, matchSize);
            long s1 = System.currentTimeMillis();
            int res1 = getIndexOf(str, match);
            long e2 = System.currentTimeMillis();
            int res2 = str.indexOf(match);
            long e3 = System.currentTimeMillis();
            System.out.println("res1 = " + res1 + ",time=" + (e2 - s1));
            System.out.println("res2 = " + res2 + ",time=" + (e3 - e2));
            if (res1 != res2) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }
}
