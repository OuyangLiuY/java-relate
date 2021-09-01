package class35;

import java.util.ArrayList;
import java.util.List;

/**
 * 意淫了 跳表结构，结果发现，是不对得
 */
public class Code02_SkipListMap_2 {

    public static class SkipListNode<K extends Comparable<K>, V> {
        public K key;
        public V val;
        public SkipListNode<K, V> nextNode;
        public int level;

        public SkipListNode(K key, V val) {
            this.key = key;
            this.val = val;
            nextNode = null;
            level = 0;
        }

        // 遍历的时候，如果是往右遍历到的null(next == null), 遍历结束
        // 头(null), 头节点的null，认为最小
        // node  -> 头，node(null, "")  node.isKeyLess(!null)  true
        // node里面的key是否比otherKey小，true，不是false
        public boolean isKeyLess(K otherKey) {
            return otherKey != null && (key == null || key.compareTo(otherKey) < 0);
        }

        public boolean isKeyEquals(K otherKey) {
            return (otherKey == null && key == null) || (key != null && otherKey != null && otherKey.compareTo(key) == 0);
        }
    }

    public static class SkipListMap2<K extends Comparable<K>, V> {
        private static final double PROBABILITY = 0.5;
        private final List<SkipListNode<K, V>> root;
        private int maxLevel;
        private int size;

        public SkipListMap2() {
            root = new ArrayList<>();
            size = 0;
            maxLevel = 0;
        }

        // 从最高层开始，一路找下去，
        // 最终，找到第0层的<key的最右的节点
        private SkipListNode<K, V> mostRightLessNodeInTree(K key) {
            if (key == null) {
                return null;
            }
            int level = maxLevel;
            List<SkipListNode<K, V>> cur = root;
            SkipListNode<K, V> ans = null;
            while (level >= 0) {
                if (cur.size() > 0) {
                    ans = mostRightLessNodeInLevel(key, cur.get(level));
                }
                level--;
            }
            return ans;
        }

        // 在level层里，如何往右移动
        // 来到level层第一个节点
        private SkipListNode<K, V> mostRightLessNodeInLevel(K key, SkipListNode<K, V> cur) {
            while (cur != null && cur.isKeyLess(key)) {
                cur = cur.nextNode;
            }
            return cur;
        }

        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            SkipListNode<K, V> less = mostRightLessNodeInTree(key);
            if (size > 1) {
                if (less == null) {
                    return false;
                } else {
                    SkipListNode<K, V> next = less.nextNode;
                    return next != null && next.isKeyEquals(key);
                }
            } else {
                return less != null && less.isKeyEquals(key);
            }
        }

        // 新增、改value
        public void put(K key, V value) {
            if (key == null) {
                return;
            }
            // 0层上，最右一个，< key 的Node -> >key
            SkipListNode<K, V> less = mostRightLessNodeInTree(key);
            if (less != null && less.nextNode != null && less.nextNode.isKeyEquals(key)) { // 存在更新
                less.nextNode.val = value;
            } else { //新增
                size++;
                int newNodeLevel = 0;
                if (Math.random() < PROBABILITY) {
                    newNodeLevel++;
                }
                while (newNodeLevel > maxLevel) {
                    root.add(null);
                    maxLevel++;
                }
                SkipListNode<K, V> newNode = new SkipListNode<>(key, value);
               /* for (int i = 0; i <= newNodeLevel; i++) {
                    newNode.nextNode = new SkipListNode<>(null, null);
                }*/
                int level = maxLevel;
                SkipListNode<K, V> curLevel;
                while (level >= 0) {
                    // level 层中，找到最右的 < key 的节点
                    if (root.size() == 0 || root.size() <= maxLevel) {
                        root.add(newNode);
                    } else {
                        SkipListNode<K, V> node = root.get(level);
                        curLevel = mostRightLessNodeInLevel(key, node);
                        if (level <= newNodeLevel) {
                            if (curLevel == null) {
                                newNode.nextNode = node;
                                root.set(level, newNode);
                            } else {
                                if (curLevel.nextNode != null) {
                                    newNode.nextNode = curLevel.nextNode;
                                }
                                curLevel.nextNode = newNode;
                            }
                        }
                    }
                    level--;
                }
            }
        }

        public void remove(K key) {
            if (containsKey(key)) {
                size--;
                int level = maxLevel;
                while (level >= 0) {
                    SkipListNode<K, V> cur = mostRightLessNodeInLevel(key, root.get(level));
                    SkipListNode<K, V> next = cur.nextNode;
                    // 1）在这一层中，pre下一个就是key
                    // 2）在这一层中，pre的下一个key是>要删除key
                    if (size <= 0) {
                        root.remove(level);
                    } else {
                        if (next != null && next.isKeyEquals(key)) {
                            cur.nextNode = next.nextNode;
                        }
                    }
                    // 看一下当前层数据是否已经不存在了
                    if (level > 0 && root.size() > maxLevel) {
                        root.remove(level);
                        maxLevel--;
                    }
                    level--;
                }
            }
        }

        public K firstKey() {
            return root.get(0) != null ? root.get(0).key : null;
        }

        public K lastKey() {
            int level = maxLevel;
            SkipListNode<K, V> cur = null;
            while (level >= 0) {
                cur = root.get(level);
                SkipListNode<K, V> next = cur.nextNode;
                while (next != null) {
                    cur = next;
                    next = cur.nextNode;
                }
                level--;
            }
            return cur.key;
        }

        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            SkipListNode<K, V> less = mostRightLessNodeInTree(key);
            SkipListNode<K, V> next = less.nextNode;
            return next != null ? next.key : null;
        }

        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            SkipListNode<K, V> less = mostRightLessNodeInTree(key);
            SkipListNode<K, V> more = less.nextNode;
            return (more != null && more.isKeyEquals(key)) ? more.key : less.key;
        }

        public int size() {
            return size;
        }
    }

    // for test
    public static void printAll(SkipListMap2<String, String> obj) {
        for (int i = obj.maxLevel; i >= 0; i--) {
            if (obj.root.size() > 0) {
                System.out.print("Level " + i + " : ");
                SkipListNode<String, String> cur = obj.root.get(i);
                while (cur != null) {
                    SkipListNode<String, String> next = cur;
                    System.out.print("(" + next.key + " , " + next.val + ") ");
                    cur = next.nextNode;
                }
                System.out.println();
            }

        }
    }

    public static void main(String[] args) {
        SkipListMap2<String, String> test = new SkipListMap2<>();
        printAll(test);
        System.out.println("======================");
        test.put("A", "10");
        printAll(test);
        System.out.println("======================");
        test.remove("A");
        printAll(test);
        System.out.println("======================");
        test.put("E", "E");
        test.put("B", "B");
        test.put("A", "A");
        test.put("F", "F");
        test.put("C", "C");
        test.put("D", "D");
        printAll(test);
        System.out.println("======================");
        System.out.println(test.containsKey("B"));
        System.out.println(test.containsKey("Z"));
        System.out.println(test.firstKey());
        System.out.println(test.lastKey());
        System.out.println(test.floorKey("D"));
        System.out.println(test.ceilingKey("D"));
        System.out.println("======================");
        test.remove("D");
        printAll(test);
        System.out.println("======================");
        System.out.println(test.floorKey("D"));
        System.out.println(test.ceilingKey("D"));/**/
    }
}
