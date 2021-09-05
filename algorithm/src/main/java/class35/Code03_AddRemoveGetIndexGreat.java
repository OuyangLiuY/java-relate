package class35;

import java.util.ArrayList;

/**
 * 使用有序表实现 数组 结构
 */
public class Code03_AddRemoveGetIndexGreat {
    public static class SBTNode<V> {
        public V value;
        public SBTNode<V> l;
        public SBTNode<V> r;
        public int size;

        public SBTNode(V value) {
            this.value = value;
            this.size = 1;
        }
    }

    public static class SbtList<V> {
        public SBTNode<V> root;

        private SBTNode<V> leftRotate(SBTNode<V> cur) {
            SBTNode<V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            //size
            right.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return right;
        }

        private SBTNode<V> rightRotate(SBTNode<V> cur) {
            SBTNode<V> left = cur.l;
            cur.l = left.r;
            left.r = cur;
            // size
            left.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return left;
        }

        private SBTNode<V> maintain(SBTNode<V> cur) {
            if (cur == null) {
                return null;
            }
            int leftSize = cur.l != null ? cur.l.size : 0;
            int leftLeftSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            int leftRightSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rightSize = cur.r != null ? cur.r.size : 0;
            int rightLeftSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rightRightSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
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
            } else if (rightRightSize > leftSize) { //RR
                cur = leftRotate(cur);
                cur.r = maintain(cur.r);
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

        private SBTNode<V> add(SBTNode<V> root, int index, SBTNode<V> cur) {
            if (root == null) {
                return cur;
            }
            root.size++;
            int leftAndHeadSize = (root.l != null ? root.l.size : 0) + 1;
            if (leftAndHeadSize > index) {
                root.l = add(root.l, index, cur);
            } else {
                root.r = add(root.r, index - leftAndHeadSize, cur);
            }
            root = maintain(root);
            return root;
        }

        private SBTNode<V> remove(SBTNode<V> root, int index) {
            root.size--;
            int leftIndex = root.l != null ? root.l.size : 0;
            if (index != leftIndex) {
                if (index < leftIndex) {
                    root.l = remove(root.l, index);
                } else {
                    root.r = remove(root.r, index - leftIndex - 1);
                }
                return root;
            }
            if (root.r == null && root.l == null) {
                return null;
            }
            if (root.r == null) {
                return root.l;
            }
            if (root.l == null) {
                return root.r;
            }
            // 已经找到当前需要删除得节点 root
            // 那么需要找到当前节点右节点得最左节点将当前节点替换
            SBTNode<V> pre = null;
            SBTNode<V> suc = root.r;
            suc.size--;
            while (suc.l != null) {
                pre = suc;
                suc = suc.l;
                suc.size--;
            }
            if (pre != null) {
                pre.l = suc.r;
                suc.r = root.r;
            }
            suc.l = root.l;
            suc.size = suc.l.size + (suc.r == null ? 0 : suc.r.size) + 1;
            return suc;
        }

        private SBTNode<V> get(SBTNode<V> cur, int index) {
            int leftSize = cur.l != null ? cur.l.size : 0;
            if (leftSize < index) {
                return get(cur.r, index - leftSize - 1);
            } else if (leftSize == index) {
                return cur;
            } else {
                return get(cur.l, index);
            }
        }

        public void add(int index, V value) {
            if (root == null) {
                root = new SBTNode<>(value);
            } else {
                if (index <= root.size)
                    root = add(root, index, new SBTNode<>(value));
            }
        }

        public V get(int index) {
            return get(root, index).value;
        }

        public void remove(int index) {
            if (index >= 0 && index <= size()) {
                root = remove(root, index);
            }
        }

        public int size() {
            return root != null ? root.size : 0;
        }
    }

    // 通过以下这个测试，
    // 可以很明显的看到LinkedList的插入、删除、get效率不如SbtList
    // LinkedList需要找到index所在的位置之后才能插入或者读取，时间复杂度O(N)
    // SbtList是平衡搜索二叉树，所以插入或者读取时间复杂度都是O(logN)
    public static void main(String[] args) {
        // 功能测试
        int test = 50000;
        int max = 1000000;
        boolean pass = true;
        ArrayList<Integer> list = new ArrayList<>();
        SbtList<Integer> sbtList = new SbtList<>();
        for (int i = 0; i < test; i++) {
            if (list.size() != sbtList.size()) {
                pass = false;
                break;
            }
            if (list.size() > 1 && Math.random() < 0.5) {
                int removeIndex = (int) (Math.random() * list.size());
                list.remove(removeIndex);
                sbtList.remove(removeIndex);
            } else {
                int randomIndex = (int) (Math.random() * (list.size() + 1));
                int randomValue = (int) (Math.random() * (max + 1));
                list.add(randomIndex, randomValue);
                sbtList.add(randomIndex, randomValue);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(sbtList.get(i))) {
                pass = false;
                break;
            }
        }
        System.out.println("功能测试是否通过 : " + pass);

        // 性能测试
        test = 500000;
        list = new ArrayList<>();
        sbtList = new SbtList<>();
        long start = 0;
        long end = 0;

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (list.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            list.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList插入总时长(毫秒) ： " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList读取总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * list.size());
            list.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList删除总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (sbtList.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            sbtList.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList插入总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbtList.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList读取总时长(毫秒) :  " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * sbtList.size());
            sbtList.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList删除总时长(毫秒) :  " + (end - start));

    }
}
