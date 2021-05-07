package class08;

import class03.Code02_DeleteGivenValue;
import utils.SortUtils;

import java.util.HashMap;

/**
 * 前缀树：
 * 1）单个字符串中，字符从前到后的加到一棵多叉树上
 * 2）字符放在路上，节点上有专属的数据项（常见的是pass和end值）
 * 3）所有样本都这样添加，如果没有路就新建，如有路就复用
 * 4）沿途节点的pass值增加1，每个字符串结束时来到的节点end值增加1
 */
public class Code01_TrieTree {

    /**
     * 使用数组实现
     */
    public static class Node1 {
        public int pass;
        public int end;
        public Node1[] nexts;

        public Node1() {
            pass = 0;
            end = 0;
            // 这里只考虑26个小写字母
            nexts = new Node1[26];
        }

    }

    public static class Trie1 {
        public Node1 root;

        public Trie1() {
            root = new Node1();
        }

        // 添加某个字符串，可以重复添加，每次算1个
        public void insert(String word) {
            if (word == null) {
                return;
            }
            Node1 node = root;
            node.pass++;
            char[] chars = word.toCharArray();
            int index = 0;
            // 从左往右遍历 a , b , c
            for (int i = 0; i < chars.length; i++) {
                index = chars[i] - 'a';
                // 路径不存在，则新建
                if (node.nexts[index] == null) {
                    node.nexts[index] = new Node1();
                }
                // 拿到下个节点node，并指向当前node
                node = node.nexts[index];
                node.pass++;
            }
            node.end++;
        }

        // 查询某个字符串在结构中还有几个
        public int search(String word) {
            if (word == null) {
                return 0;
            }
            Node1 node = root;
            char[] chars = word.toCharArray();
            for (char chr : chars) {
                int index = chr - 'a';
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.end;
        }

        // 删掉某个字符串，可以重复删除，每次算1个
        public void delete(String word) {
            // 有这个word则删除，否则不处理
            if (search(word) != 0) {
                Node1 node = root;
                node.pass--;
                char[] chars = word.toCharArray();
                for (char chr : chars) {
                    int index = chr - 'a';
                    // pass 为0,那么下个节点数据就不存在了...
                    if (--node.nexts[index].pass == 0) {
                        node.nexts[index] = null;
                        return;
                    }
                    node = node.nexts[index];
                }
                node.end--;
            }
        }

        // 查询有多少个字符串，是以str做前缀的
        public int prefixNumber(String word) {
            Node1 node = root;
            char[] chars = word.toCharArray();
            for (char chr : chars) {
                int index = chr - 'a';
                // 看下节点是否还在呢
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.pass;
        }
    }

    /**
     * 哈系表实现
     */
    public static class Node2 {
        public int pass;
        public int end;
        public HashMap<Integer, Node2> next;

        public Node2() {
            pass = 0;
            end = 0;
            next = new HashMap<>();
        }
    }

    public static class Trie2 {
        public Node2 root;

        public Trie2() {
            root = new Node2();
        }

        // 添加某个字符串，可以重复添加，每次算1个
        public void insert(String word) {
            if (word == null) {
                return;
            }
            Node2 node = root;
            node.pass++;
            char[] chars = word.toCharArray();
            int index = 0;
            // 从左往右遍历 a , b , c
            for (char chr : chars) {
                index = chr;
                // 路径不存在，则新建
                if (!node.next.containsKey(index)) {
                    node.next.put(index, new Node2());
                }
                // 拿到下个节点node，并指向当前node
                node = node.next.get(index);
                node.pass++;
            }
            node.end++;
        }

        // 查询某个字符串在结构中还有几个
        public int search(String word) {
            if (word == null) {
                return 0;
            }
            Node2 node = root;
            char[] chars = word.toCharArray();
            for (char chr : chars) {
                if (!node.next.containsKey((int) chr)) {
                    return 0;
                }
                node = node.next.get((int) chr);
            }
            return node.end;
        }

        // 删掉某个字符串，可以重复删除，每次算1个
        public void delete(String word) {
            // 有这个word则删除，否则不处理
            if (search(word) != 0) {
                Node2 node = root;
                node.pass--;
                char[] chars = word.toCharArray();
                for (char chr : chars) {
                    // pass 为0,那么下个节点数据就不存在了...
                    if (--node.next.get((int) chr).pass == 0) {
                        node.next.remove((int) chr);
                        return;
                    }
                    node = node.next.get((int) chr);
                }
                node.end--;
            }
        }

        // 查询有多少个字符串，是以str做前缀的
        public int prefixNumber(String word) {
            Node2 node = root;
            char[] chars = word.toCharArray();
            for (char chr : chars) {
                // 看下节点是否还在呢
                if (!node.next.containsKey((int) chr)) {
                    return 0;
                }
                node = node.next.get((int) chr);
            }
            return node.pass;
        }
    }

    public static class Right {
        public HashMap<String, Integer> box;

        public Right() {
            box = new HashMap<>();
        }

        public void insert(String word) {
            if (word == null)
                return;
            if (box.containsKey(word)) {
                box.put(word, box.get(word) + 1);
            } else {
                box.put(word, 1);
            }
        }

        public int search(String word) {
            if (!box.containsKey(word)) {
                return 0;
            }
            return box.get(word);
        }

        public void delete(String word) {
            if (box.containsKey(word)) {
                if (box.get(word) == 1) {
                    box.remove(word);
                } else {
                    box.put(word, box.get(word) - 1);
                }
            }
        }

        public int prefixNumber(String word) {
            int count = 0;
            for (String cur : box.keySet()) {
                if (cur.startsWith(word)) {
                    count += box.get(cur);
                }
            }
            return count;
        }
    }

    public static void main(String[] args) {
        int arrLen = 100;
        int strLen = 20;
        int testTimes = 100000;
        for (int i = 0; i < testTimes; i++) {
            String[] arr = SortUtils.generateRandomStringArray(arrLen, strLen);
            Trie1 trie1 = new Trie1();
            Trie2 trie2 = new Trie2();
            Right right = new Right();
            for (int j = 0; j < arr.length; j++) {
                double decide = Math.random();
                trie1.insert(arr[j]);
                trie2.insert(arr[j]);
                right.insert(arr[j]);
                if (decide < 0.5) {
                    trie1.delete(arr[j]);
                    trie2.delete(arr[j]);
                    right.delete(arr[j]);
                } else if (decide < 0.75) {
                    int ans1 = trie1.search(arr[j]);
                    int ans2 = trie2.search(arr[j]);
                    int ans3 = right.search(arr[j]);
                    if (ans1 != ans2 || ans2 != ans3) {
                        System.out.println("Oops 0.75!");
                    }
                } else {
                    int ans1 = trie1.prefixNumber(arr[j]);
                    int ans2 = trie2.prefixNumber(arr[j]);
                    int ans3 = right.prefixNumber(arr[j]);
                    if (ans1 != ans2 || ans2 != ans3) {
                        System.out.println("Oops else!" + i);
                    }
                }
            }
        }
        System.out.println("finish!");
    }
}
