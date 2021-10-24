package basic_knowledge.class35;

/**
 * 1）L是滑动窗口最左位置、R是滑动窗口最右位置，一开始LR都在数组左侧
 * 2）任何一步都可能R往右动，表示某个数进了窗口
 * 3）任何一步都可能L往右动，表示某个数出了窗口
 * 想知道每一个窗口状态的中位数
 */
public class Code03_SlidingWindowMedian {


    public double[] medianSlidingWindow(int[] nums, int k) {
        SizeBalancedTreeMap<Node> treeMap = new SizeBalancedTreeMap<>();
        // 结构中先加k-1个数
        for (int i = 0; i < k - 1; i++) {
            treeMap.add(new Node(i, nums[i]));
        }
        int index = 0;
        double[] ans = new double[nums.length - k + 1];
        // 就是加一个，求中位数，然后再删掉一个，
        for (int i = k - 1; i < nums.length; i++) {
            treeMap.add(new Node(i, nums[i]));
            if (treeMap.size() % 2 == 0) {
                Node up = treeMap.getIndexKey(treeMap.size() / 2);
                Node down = treeMap.getIndexKey(treeMap.size() / 2 - 1);
                ans[index++] = ((double) up.value + (double) down.value) / 2;
            } else {
                Node mid = treeMap.getIndexKey(treeMap.size() / 2);
                ans[index++] = mid.value;
            }
            treeMap.remove(new Node(i - k + 1, nums[i - k + 1]));
        }
        return ans;
    }

    public static class Node implements Comparable<Node> {
        public int index;
        public int value;

        public Node(int index, int value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Node o) {
            return this.value != o.value ? Integer.compare(value, o.value)
                    : Integer.compare(index, o.index);
        }
    }


    public static class SBTNode<K extends Comparable<K>> {
        public K key;
        public SBTNode<K> l;
        public SBTNode<K> r;
        public int size; // 不同的key的数量

        public SBTNode(K key) {
            this.key = key;
            size = 1;
        }
    }

    public static class SizeBalancedTreeMap<K extends Comparable<K>> {
        private SBTNode<K> root;

        private SBTNode<K> rightRotate(SBTNode<K> cur) {
            SBTNode<K> leftNode = cur.l;
            cur.l = leftNode.r;
            leftNode.r = cur;
            leftNode.size = cur.size; // 因节点个数是不变的，故只需继承
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return leftNode;
        }

        private SBTNode<K> leftRotate(SBTNode<K> cur) {
            SBTNode<K> rightNode = cur.r;
            cur.r = rightNode.l;
            rightNode.l = cur;
            rightNode.size = cur.size; // 因节点个数是不变的，故只需继承
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return rightNode;
        }

        private SBTNode<K> maintain(SBTNode<K> cur) {
            if (cur == null) {
                return null;
            }
            int leftSize = cur.l != null ? cur.l.size : 0; // 左
            int leftLeftSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0; // 左左
            int leftRightSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0; // 左右
            int rightSize = cur.r != null ? cur.r.size : 0; // 右
            int rightLeftSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0; // 右左
            int rightRightSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0; // 右右
            if (leftLeftSize > rightSize) { // LL
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (leftRightSize > rightSize) { // LR
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rightRightSize > leftSize) { // RR
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rightLeftSize > leftSize) { // RL
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        // 现在，以cur为头的树上，新增，加(keyalue)这样的记录
        // 加完之后，会对cur做检查，该调整调整
        // 返回，调整完之后，整棵树的新头部
        private SBTNode<K> add(SBTNode<K> cur, K key) {
            if (cur == null) {
                return new SBTNode<>(key);
            } else {
                cur.size++;
                if (key.compareTo(cur.key) < 0) {
                    cur.l = add(cur.l, key);
                } else {
                    cur.r = add(cur.r, key);
                }
                return maintain(cur);
            }
        }

        // 在cur这棵树上，删掉key所代表的节点
        // 返回cur这棵树的新头部
        private SBTNode<K> delete(SBTNode<K> cur, K key) {
            cur.size--;
            if (key.compareTo(cur.key) < 0) {
                cur.l = delete(cur.l, key);
            } else if (key.compareTo(cur.key) > 0) {
                cur.r = delete(cur.r, key);
            } else {
                // 当前要删掉cur
                if (cur.l == null && cur.r == null) {
                    // free cur memory -> C++
                    cur = null;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else { //左右子节点都不为空情况
                    SBTNode<K> pre = null;
                    SBTNode<K> des = cur.r;
                    des.size--;
                    while (des.l != null) {
                        pre = des;
                        des = des.l;
                        des.size--;
                    }
                    if (pre != null) {// 如果最后一个节点des是有右点节点的，所以des的右节点要放到pre节点的左节点
                        pre.l = des.r;
                        des.r = cur.r;
                    }
                    des.l = cur.l;
                    des.size = des.l.size + (des.r != null ? des.r.size : 0) + 1;
                    // free cur memory -> C++
                    cur = des;
                }
            }
            // cur = maintain(cur); //可以不需要保持平衡，等到加入节点的时候去维护平衡
            return cur;
        }

        private SBTNode<K> findLastIndex(K key) {
            SBTNode<K> pre = root;
            SBTNode<K> cur = root;
            while (cur != null) {
                pre = cur;
                if (key.compareTo(cur.key) == 0) {
                    break;
                } else if (key.compareTo(cur.key) < 0) {
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return pre;
        }

        private SBTNode<K> findLastNoSmallIndex(K key) {
            SBTNode<K> ans = null;
            SBTNode<K> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.key) < 0) {
                    ans = cur;
                    cur = cur.l;
                } else if (key.compareTo(cur.key) > 0) {
                    cur = cur.r;
                } else {
                    ans = cur;
                    break;
                }
            }
            return ans;
        }

        private SBTNode<K> findLastNoBigIndex(K key) {
            SBTNode<K> ans = null;
            SBTNode<K> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.key) < 0) {
                    cur = cur.l;
                } else if (key.compareTo(cur.key) > 0) {
                    ans = cur;
                    cur = cur.r;
                } else {
                    ans = cur;
                    break;
                }
            }
            return ans;
        }

        private SBTNode<K> getIndex(SBTNode<K> cur, int kth) {
            if (kth == (cur.l != null ? cur.l.size : 0) + 1) {
                return cur;
            } else if (kth <= (cur.l != null ? cur.l.size : 0)) {
                return getIndex(cur.l, kth);
            } else {
                return getIndex(cur.r, kth - (cur.l != null ? cur.l.size : 0) - 1);
            }
        }

        public int size() {
            return root == null ? 0 : root.size;
        }

        public boolean containsKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter.");
            }
            SBTNode<K> lastIndex = findLastIndex(key);
            return lastIndex != null && key.compareTo(lastIndex.key) == 0;
        }

        // （key，value） put -> 有序表 新增、改value
        public void add(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter.");
            }
            SBTNode<K> lastIndex = findLastIndex(key);
            if (lastIndex == null || key.compareTo(lastIndex.key) != 0) {
                root = add(root, key);
            }
        }

        public void remove(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter.");
            }
            if (containsKey(key)) {
                root = delete(root, key);
            }
        }

        public K getIndexKey(int index) {
            if (index < 0 || index >= this.size()) {
                throw new RuntimeException("invalid parameter.");
            }
            return getIndex(root, index + 1).key;
        }


        public K floorKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter.");
            }
            SBTNode<K> lastNoBigNode = findLastNoBigIndex(key);
            return lastNoBigNode == null ? null : lastNoBigNode.key;
        }
    }
}
