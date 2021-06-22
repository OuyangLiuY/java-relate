package class17;

/**
 * 打印n层汉诺塔从最左边移动到最右边的全部过程
 */
public class Code01_Hanoi {
    public static void hanoi1(int n) {
        leftToRight(n);
    }

    // 把 1~N 层圆盘 从左 -> 右
    public static void leftToRight(int N) {
        if (N == 1) {
            System.out.println(" Move  1 from left to right!");
            return;
        }
        leftToMid(N - 1);
        System.out.println("Move " + N + " from left to right");
        midToRight(N - 1);
    }

    // 把 1~N 层圆盘 从中 -> 右
    private static void midToRight(int N) {
        if (N == 1) {
            System.out.println(" Move  1 from mid to right!");
            return;
        }
        midToLeft(N - 1);
        System.out.println("Move " + N + " from mid to right");
        leftToRight(N - 1);
    }

    private static void midToLeft(int N) {
        if (N == 1) {
            System.out.println(" Move  1 from mid to left!");
            return;
        }
        midToRight(N - 1);
        System.out.println("Move " + N + " from mid to left");
        rightToLeft(N - 1);
    }

    private static void rightToLeft(int N) {
        if (N == 1) {
            System.out.println(" Move  1 from right to left!");
            return;
        }
        rightToMid(N - 1);
        System.out.println("Move " + N + " from right to left");
        rightToLeft(N - 1);
    }

    private static void leftToMid(int N) {
        if (N == 1) {
            System.out.println(" Move  1 from left to mid!");
            return;
        }
        leftToRight(N - 1);
        System.out.println("Move " + N + " from left to mid");
        rightToMid(N - 1);
    }

    private static void rightToMid(int N) {
        if (N == 1) {
            System.out.println(" Move  1 from right to mid!");
            return;
        }
        rightToLeft(N - 1);
        System.out.println("Move " + N + " from right to mid");
        leftToMid(N - 1);
    }


    public static void hanoi2(int n) {
        if (n > 0) {
            func(n, "left", "right", "mid");
        }
    }

    private static void func(int n, String from, String to, String other) {
        if (n == 1) {
            System.out.println("Move 1 from " + from + " to " + to);
        } else {
            func(n - 1, from, other, to);
            System.out.println(" Move " + n + " from " + from + " to " + to);
            func(n - 1, other, to, from);
        }
    }

    public static void main(String[] args) {
        int n = 3;
        System.out.println("hanoi 1-------");
        hanoi1(n);
        System.out.println("hanoi 2 -------");
        hanoi2(n);
    }

}
