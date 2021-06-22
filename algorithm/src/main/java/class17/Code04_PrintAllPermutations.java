package class17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 打印一个字符串的全部排列
 */
public class Code04_PrintAllPermutations {

    public static List<String> permutation1(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] chars = s.toCharArray();
        ArrayList<Character> characters = new ArrayList<>();
        for (char ch : chars) {
            characters.add(ch);
        }
        String path = "";
        func1(characters, ans, path);
        return ans;
    }

    // res : abc
    private static void func1(ArrayList<Character> rest, List<String> ans, String path) {
        if (rest.isEmpty()) {
            ans.add(path);
        } else {
            for (int i = 0; i < rest.size(); i++) {
                char cur = rest.get(i);
                // 拿出 a, 看 b，c情况， 到 b，了，拿出 b，然后看 c出情况， -》情况1， a b c 情况2: a, c , b
                rest.remove(i);
                func1(rest, ans, path + cur);
                rest.add(i, cur);
            }
        }
    }

    public static List<String> permutation2(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        func2(str, 0, ans);
        return ans;
    }

    private static void func2(char[] str, int index, List<String> ans) {
        if (index == str.length) {
            ans.add(String.valueOf(str));
        } else {
            for (int i = index; i < str.length; i++) {
                swap(str, index, i);
                func2(str, index + 1, ans);
                swap(str, index, i);
            }
        }
    }

    public static void swap(char[] chs, int i, int j) {
        char tmp = chs[i];
        chs[i] = chs[j];
        chs[j] = tmp;
    }

    public static List<String> permutation3(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        func3(str, 0, ans);
        return ans;
    }

    private static void func3(char[] str, int index, List<String> ans) {
        if (index == str.length) {
            ans.add(String.valueOf(str));
        } else {
            boolean[] visited = new boolean[256];
            for (int i = index; i < str.length; i++) {
                // 当前这个位置是否被访问过
                if(!visited[str[i]]){
                    visited[str[i]] = true;
                    swap(str, index, i);
                    func3(str, index + 1, ans);
                    swap(str, index, i);
                }
            }
        }
    }

    public static void main(String[] args) {
        String s = "acc";
        List<String> ans1 = permutation1(s);
        for (String str : ans1) {
            System.out.println(str);
        }
        System.out.println("=======");
        List<String> ans2 = permutation2(s);
        for (String str : ans2) {
            System.out.println(str);
        }
        System.out.println("=======");
       List<String> ans3 = permutation3(s);
        for (String str : ans3) {
            System.out.println(str);
        }
    }




}
