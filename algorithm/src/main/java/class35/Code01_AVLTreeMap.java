package class35;

/**
 * AVL 树有序表的实现
 */
public class Code01_AVLTreeMap {
    /**
     * AVL 实体类
     *
     * @param <K>
     * @param <V>
     */
    public static class AVLNode<K extends Comparable<K>, V> {
        public K k;
        public V v;
        public AVLNode<K, V> r;
        public AVLNode<K, V> l;
        public int h;

        public AVLNode(K key, V value) {
            k = key;
            v = value;
            h = 1;
        }
    }

    /**
     * 实现AVL树结构增删改查的Map结构
     *
     * @param <K>
     * @param <V>
     */
    public static class AVLTreeMap<K extends Comparable<K>, V> {
        private AVLNode<K, V> root;
        private int size;

        public AVLTreeMap() {
            this.root = null;
            this.size = 0;
        }

        // 右旋
        public AVLNode<K, V> rightRotate(AVLNode<K, V> cur) {
            if (cur == null) {
                return null;
            }
            AVLNode<K, V> left = cur.l;
            cur.l = left.r;
            left.r = cur;
            cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
            left.h = Math.max(left.r.h, left.l != null ? left.l.h : 0) + 1;
            return left;
        }

        // 左旋
        public AVLNode<K, V> leftRotate(AVLNode<K, V> cur) {
            AVLNode<K, V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
            right.h = Math.max(right.l.h, right.r != null ? right.r.h : 0) + 1;
            return right;
        }

        public AVLNode<K, V> add(AVLNode<K, V> cur, K key, V value) {
            if (cur == null) {
                return new AVLNode<>(key, value);
            } else {
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key, value);
                } else {
                    cur.r = add(cur.r, key, value);
                }
                cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
                return maintain(cur);
            }
        }

        public AVLNode<K, V> delete(AVLNode<K, V> cur, K key) {
            if (key.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, key);
            } else if (key.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, key);
            } else {
                if (cur.l == null && cur.r == null) { // 左右子节点都为空，直接删除
                    cur = null;
                } else if (cur.l == null && cur.r != null) { // 左节点为空，右节点不为空
                    cur = cur.r;
                } else if (cur.l != null && cur.r == null) {// 右节点为空，左节点不为空
                    cur = cur.l;
                } else {
                    AVLNode<K, V> des = cur.r; // 右节点
                    while (des.l != null) { // 右节点最左节点
                        des = des.l;
                    }
                    cur.r = delete(cur.r, des.k); // 将des节点从 cur.r 树上删除，其实就是右节点有可能做了调整
                    // 这里开始才真正删除
                    des.r = cur.r;
                    des.l = cur.l;
                    cur = des;
                }
            }
            if (cur != null) {
                cur.h = Math.max(cur.r != null ? cur.r.h : 0, cur.l != null ? cur.l.h : 0);
            }
            return maintain(cur);
        }

        /**
         * 将数据结构保持平衡
         *
         * @param cur
         * @return
         */
        public AVLNode<K, V> maintain(AVLNode<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int rightHeight = cur.r != null ? cur.r.h : 0;
            int leftHeight = cur.l != null ? cur.l.h : 0;
            if (Math.abs(rightHeight - leftHeight) > 1) {
                if (leftHeight > rightHeight) {
                    int leftLeftHeight = cur.l != null && cur.l.l != null ? cur.l.l.h : 0;
                    int leftRightHeight = cur.l != null && cur.l.r != null ? cur.l.r.h : 0;
                    if (leftLeftHeight >= leftRightHeight) { // LL型
                        cur = rightRotate(cur);
                    } else { // LR 型，先左旋，再左旋
                        cur.l = leftRotate(cur.l);
                        cur = rightRotate(cur);
                    }
                } else {
                    int rightLeftHeight = cur.r != null && cur.r.l != null ? cur.r.l.h : 0;
                    int rightRightHeight = cur.r != null && cur.r.r != null ? cur.r.r.h : 0;
                    if (rightRightHeight >= rightLeftHeight) { // RR 型
                        cur = leftRotate(cur);
                    } else { // RL 型
                        cur.r = rightRotate(cur.r);
                        cur = leftRotate(cur);
                    }
                }
            }
            return cur;
        }

        public AVLNode<K, V> getRoot() {
            return root;
        }

        /**
         * 获取到当前节点为key的头节点数据，
         * 如果没找到则返回空
         *
         * @param key
         * @return
         */
        private AVLNode<K, V> findLastIndex(K key) {
            AVLNode<K, V> ans = null;
            AVLNode<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else if (key.compareTo(cur.k) > 0) {
                    cur = cur.r;
                } else {
                    ans = cur;
                    break;
                }
            }
            return ans;
        }

        /**
         * 找到最后一个为key或小于key的索引，
         * 如没有等于的节点，那么返回小于可以的索引，
         * 如果没有比他更小的那么就返回空
         *
         * @param key
         * @return
         */
        private AVLNode<K, V> findLastNoSmallIndex(K key) {
            AVLNode<K, V> ans = null;
            AVLNode<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) < 0) {
                    ans = cur;
                    cur = cur.l;
                } else if (key.compareTo(cur.k) > 0) {
                    cur = cur.r;
                } else {
                    ans = cur;
                    break;
                }
            }
            return ans;
        }

        /**
         * 找到最后一个为key或大于key的索引，
         * 如没有等于的节点，那么返回小于可以的索引，
         * 如果没有比他更小的那么就返回空
         *
         * @param key
         * @return
         */
        private AVLNode<K, V> findLastNoBigIndex(K key) {
            AVLNode<K, V> ans = null;
            AVLNode<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else if (key.compareTo(cur.k) > 0) {
                    ans = cur;
                    cur = cur.r;
                } else {
                    ans = cur;
                    break;
                }
            }
            return ans;
        }

        /**
         * 查询是否包含key
         *
         * @param key
         * @return
         */
        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            AVLNode<K, V> index = findLastIndex(key);
            return index != null && key.compareTo(index.k) == 0;
        }

        public void put(K key, V value) {
            if (key == null) {
                return;
            }
            AVLNode<K, V> lastNode = findLastIndex(key);
            if (lastNode != null && key.compareTo(lastNode.k) == 0) {
                lastNode.v = value;
            } else {
                size++;
                root = add(root, key, value);
            }
        }

        public void remove(K key) {
            if (key == null) {
                return;
            }
            if (containsKey(key)) {
                size--;
                root = delete(root, key);
            }
        }

        public V get(K key) {
            if (key == null) {
                return null;
            }
            AVLNode<K, V> lastNode = findLastIndex(key);
            if (lastNode != null && key.compareTo(lastNode.k) == 0) {
                return lastNode.v;
            }
            return null;
        }

        public K firstKey() {
            if (root == null) {
                return null;
            }
            AVLNode<K, V> cur = root;
            while (cur.l != null) { // 最左边的值，就是最小的值
                cur = cur.l;
            }
            return cur.k;
        }

        public K lastKey() {
            if (root == null) {
                return null;
            }
            AVLNode<K, V> cur = root;
            while (cur.r != null) { // 最右边的值，就是最大的值
                cur = cur.r;
            }
            return cur.k;
        }

        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            AVLNode<K, V> lastNoBigNode = findLastNoBigIndex(key);
            return lastNoBigNode == null ? null : lastNoBigNode.k;
        }

        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            AVLNode<K, V> lastNoSmallNode = findLastNoSmallIndex(key);
            return lastNoSmallNode == null ? null : lastNoSmallNode.k;
        }
    }
}
