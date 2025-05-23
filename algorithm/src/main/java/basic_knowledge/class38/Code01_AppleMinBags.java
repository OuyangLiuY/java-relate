package basic_knowledge.class38;

public class Code01_AppleMinBags {

    public static int minBags(int apple) {
        if (apple < 0) {
            return -1;
        }
        // 先装8个试试
        int bag8 = apple / 8;
        int rest = apple - bag8 * 8;
        while (bag8 >= 0) {
            if (rest % 6 == 0) {
                return bag8 + rest / 6;
            } else {
                bag8--;
                rest += 8;
            }
        }
        return -1;
    }

    public static int minBags2(int apple) {
        if ((apple & 1) != 0) { // 如果是奇数，返回-1
            return -1;
        }
        if (apple < 18) {
            return apple == 0 ? 0 : (apple == 6 || apple == 8) ? 1 : (apple == 12 || apple == 14 || apple == 16) ? 2 : -1;
        }
        return (apple - 18) / 8 + 3;
    }


    public static void main(String[] args) {
        for (int apple = 1; apple < 100000; apple++) {
            int ans1 = minBags(apple);
            int ans2 = minBags2(apple);
            if (ans1 != ans2) {
                System.out.println("方法1:" + apple + " : " + minBags(apple));
                System.out.println("方法2:" + apple + " : " + minBags2(apple));
            }
        }
    }
}
