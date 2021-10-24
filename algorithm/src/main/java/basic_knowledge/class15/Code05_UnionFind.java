package basic_knowledge.class15;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * 使用集合方式实现并查集的数据结构
 */
public class Code05_UnionFind {
    public static class Node<V> {
        V value;
        public Node(V v) {
            value = v;
        }
    }
    public static class UnionFind<V> {
        public HashMap<V,Node<V>>nodes; // 当前 V 对应的Node
        public HashMap<Node<V>,Node<V>> parents; // 当前Node 他的父节点是谁
        public HashMap<Node<V>,Integer> sizeMap; // 当前Node 下有多少个节点

        public UnionFind(List<V> values) {
            nodes = new HashMap<>();
            parents = new HashMap<>();
            sizeMap = new HashMap<>();
            for (V cur : values){
                Node<V> node = new Node<>(cur);
                nodes.put(cur,node);
                parents.put(node,node);
                sizeMap.put(node,1);
            }
        }

        // 给你一个节点，请你往上到不能再往上，把代表返回
        public Node<V> findFather(Node<V> cur) {
            Stack<Node<V>> path = new Stack<>();
            while (cur != parents.get(cur)){
                path.push(cur);
                cur = parents.get(cur);
            }
            while (!path.isEmpty()){
                parents.put(path.pop(),cur);
            }
            return cur;
        }
        // 是否是同一个集合的节点
        public boolean isSameSet(V a, V b) {
            return findFather(nodes.get(a)) == findFather(nodes.get(b));
        }
        // 把a和b各自所在集合的所有样本合并成一个集合
        public void union(V a, V b) {
            Node<V> aHead = findFather( nodes.get(a));
            Node<V> bHead =  findFather(nodes.get(b));
            if(aHead != bHead){
                Integer aSize = sizeMap.get(aHead);
                Integer bSize = sizeMap.get(bHead);
                Node<V> big = aSize >= bSize ? aHead : bHead;
                Node<V> small = big == aHead ? bHead : aHead;
                parents.put(small,big);
                sizeMap.put(big,aSize + bSize);
                sizeMap.remove(small);
            }
        }
        public int sets() {
            return sizeMap.size();
        }
    }
}
