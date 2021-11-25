package basic_knowledge.class39;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

//本文件是Code02_SnacksWays问题的牛客题目解答
//但是用的分治的方法
//这是牛客的测试链接：
//https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
//把如下的全部代码拷贝进编辑器（java）
//可以直接通过
public class Code02_SnacksWaysMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int bag = sc.nextInt();
        int[] arr = new int[N];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        long ways = ways(arr, bag);
        System.out.println(ways);
        sc.close();
    }

    public static long ways(int[] arr, int bag) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] <= bag ? 2 : 1;
        }
        int mid = (arr.length - 1) / 2;
        TreeMap<Long, Long> lMap = new TreeMap<>();
        int ways = process(arr, 0, mid, 0, bag, lMap);
        TreeMap<Long, Long> rMap = new TreeMap<>();
        ways += process(arr, mid + 1, arr.length - 1, 0, bag, rMap);
        // 右半边前缀和
        TreeMap<Long, Long> rpre = new TreeMap<>();
        long pre = 0;
        for (Map.Entry<Long, Long> entry : rMap.entrySet()) {
            pre += entry.getValue();
            rpre.put(entry.getKey(), pre);
        }
        for (Map.Entry<Long, Long> entry : lMap.entrySet()) {
            Long lWeight = entry.getKey();
            Long lWays = entry.getValue();
            Long floor = rpre.floorKey(bag - lWeight);
            if (floor != null) {
                ways += lWays * rpre.get(floor);
            }
        }
        return ways + 1; // +1 是sum等于0情况
    }

    // 从index出发，到end结束
    // 之前的选择，已经形成的累加和sum
    // 零食[index....end]自由选择，出来的所有累加和sum，不能超过bag，每一种累加和对应的方法数，填在map里
    // 最后不能什么货都没选
    private static int process(int[] arr, int start, int end, long sum, long bag, TreeMap<Long, Long> treeMap) {
        if (sum > bag) {
            return 0;
        }
        if (start > end) {
            if (sum != 0) {
                if (!treeMap.containsKey(sum)) {
                    treeMap.put(sum, 1L);
                } else {
                    treeMap.put(sum, treeMap.get(sum) + 1);
                }
                return 1;
            } else {
                return 0; //一种都不选情况
            }
        }
        // 不要当前位置
        int ways1 = process(arr, start + 1, end, sum, bag, treeMap);
        // 要当前位置得数
        int ways2 = process(arr, start + 1, end, sum + arr[start], bag, treeMap);
        return ways1 + ways2;
    }

}
