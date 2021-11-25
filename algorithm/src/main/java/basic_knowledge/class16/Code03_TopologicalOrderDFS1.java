package basic_knowledge.class16;

import basic_knowledge.class14.Code03_BestArrange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 *  按照node节点深度排序
 *  https://www.lintcode.com/problem/topological-sorting
 */
public class Code03_TopologicalOrderDFS1 {
    public static class DirectedGraphNode {
        public int label;
        public ArrayList<DirectedGraphNode> neighbors;

        public DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<DirectedGraphNode>();
        }
    }


    public static class Record{
        public DirectedGraphNode node;
        public int deep;

        public Record(DirectedGraphNode node, int deep) {
            this.node = node;
            this.deep = deep;
        }
    }

    public static  class  MyComparator implements Comparator<Record>{
        @Override
        public int compare(Record o1, Record o2) {
            return o2.deep - o1.deep;
        }
    }

    public static  Record fill(DirectedGraphNode node , HashMap<DirectedGraphNode, Record> order){
        if(order.containsKey(node)){
            return order.get(node);
        }
        int follow = 0;
        for (DirectedGraphNode cur : node.neighbors){
            follow = Math.max(follow,fill(cur,order).deep);
        }
        Record record = new Record(node,follow + 1);
        order.put(node,record);
        return record;
    }
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Record> order = new HashMap<>();
        for (DirectedGraphNode node : graph){
            fill(node,order);
        }
        ArrayList<Record> recordArr = new ArrayList<>(order.values());
        recordArr.sort(new MyComparator());
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        for (Record record : recordArr){
            ans.add(record.node);
        }
        return  ans;
    }
}
