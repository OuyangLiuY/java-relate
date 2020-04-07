package sort;

/**
 * 冒泡排序
 *
 * @author chopsticks
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] arrs = new int[]{4, 3, 10, 2, 6, 20, 11, 30, 15};

        int[] result = sortMethod(arrs);

        for (int aa : result) {

            System.out.print(aa + "\t");
        }
        System.out.println();
    }

    private static int[] sortMethod(int[] arrs) {

        for (int i = 1; i < arrs.length; i++) {

            boolean flag = true;

            for (int j = 0; j < arrs.length - i; j++) {
                if (arrs[j] > arrs[j + 1]) {
                    int temp = arrs[j];
                    arrs[j] = arrs[j + 1];
                    arrs[j + 1] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }

        return arrs;
    }


}
