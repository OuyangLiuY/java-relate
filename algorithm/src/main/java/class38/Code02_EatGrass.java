package class38;

public class Code02_EatGrass {
    // 如果n份草，最终先手赢，返回"先手"
    // 如果n份草，最终后手赢，返回"后手"
    public static String whoWin(int n) {
        if (n == 0)
            return "后手";
        if (n <= 5) {
            return n == 2 || n == 5 ? "后手" : "先手";
        }
        int want = 1;
        while (want <= n){
            if(whoWin(n - want).equals("后手")){
                return "先手";
            }
            want *= 4;
        }
        return "后手";
    }

    public static String whoWin2(int n) {
        if(n % 5 == 0 || n % 5 == 2){
            return "后手";
        }else {
            return "先手";
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            System.out.println(i + " : " + whoWin(i));
            System.out.println(i + " : " + whoWin2(i));
        }
    }
}
