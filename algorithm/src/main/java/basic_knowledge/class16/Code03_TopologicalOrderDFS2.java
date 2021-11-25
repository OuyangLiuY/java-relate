package basic_knowledge.class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 *  根据 点次是大小进行排序
 *  点此：就是自己节点 + 自己得所有子节点
 */
public class Code03_TopologicalOrderDFS2 {

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
        public long nodes;

        public Record(DirectedGraphNode node, long nodes) {
            this.node = node;
            this.nodes = nodes;
        }
    }

    public static  class  MyComparator implements Comparator<Record> {
        @Override
        public int compare(Record o1, Record o2) {
            return o1.nodes == o2.nodes ? 0: (o1.nodes > o2.nodes ? -1 : 1);
        }
    }

    public static Record fill(DirectedGraphNode node , HashMap<DirectedGraphNode, Record> order){
        if(order.containsKey(node)){
            return order.get(node);
        }
        long nodes = 0;
        for (DirectedGraphNode cur : node.neighbors){
            nodes +=fill(cur,order).nodes;
        }
        Record record = new Record(node,nodes + 1);
        order.put(node,record);
        return record;
    }

    // 当前来到cur点，请返回cur点所到之处，所有的点次！
    // 返回（cur，点次）
    // 缓存！！！！！order
    //  key : 某一个点的点次，之前算过了！
    //  value : 点次是多少
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
