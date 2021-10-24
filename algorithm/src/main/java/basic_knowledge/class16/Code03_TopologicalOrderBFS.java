package basic_knowledge.class16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 拓扑排序
 * https://www.lintcode.com/problem/127/
 */
public class Code03_TopologicalOrderBFS {
    // 不要提交这个类
    public static class DirectedGraphNode {
        public int label;
        public ArrayList<DirectedGraphNode> neighbors;

        public DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<DirectedGraphNode>();
        }
    }
    // 提交下面的
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode,Integer> map = new HashMap<>();
        for (DirectedGraphNode cur : graph){
            map.put(cur,0);
        }
        // 第二次遍历之后map得结果
        // 0 -> 0
        // 1 -> 1
        // 2 -> 1
        // 3 -> 1
        // 4 -> 3
        // 5 -> 2
        for (DirectedGraphNode cur : graph){
            for (DirectedGraphNode neighbor : cur.neighbors){
                map.put(neighbor,map.get(neighbor) + 1);
            }
        }

        Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
        for (DirectedGraphNode cur : map.keySet()){
            System.out.println(cur.label + ":" + map.get(cur) + " ");
            if(map.get(cur) == 0){
                zeroQueue.add(cur);
            }
        }
        System.out.println();
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        while (!zeroQueue.isEmpty()){
            DirectedGraphNode cur = zeroQueue.poll();
            ans.add(cur);
            for (DirectedGraphNode next : cur.neighbors){
                map.put(next,map.get(next) - 1);
                if(map.get(next) == 0){
                    zeroQueue.offer(next);
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        DirectedGraphNode node0 = new DirectedGraphNode(0);
        DirectedGraphNode node1 = new DirectedGraphNode(1);
        DirectedGraphNode node2 = new DirectedGraphNode(2);
        DirectedGraphNode node3 = new DirectedGraphNode(3);
        DirectedGraphNode node4 = new DirectedGraphNode(4);
        DirectedGraphNode node5 = new DirectedGraphNode(5);
        node0.neighbors.add(node2);
        node0.neighbors.add(node3);
        node0.neighbors.add(node1);

        node1.neighbors.add(node4);

        node2.neighbors.add(node4);
        node2.neighbors.add(node5);

        node3.neighbors.add(node4);
        node4.neighbors.add(node5);
        ArrayList<DirectedGraphNode> graphNodes = new ArrayList<>();
        graphNodes.add(node0);
        graphNodes.add(node1);
        graphNodes.add(node2);
        graphNodes.add(node3);
        graphNodes.add(node4);
        graphNodes.add(node5);
        ArrayList<DirectedGraphNode> nodes = topSort(graphNodes);
        for (DirectedGraphNode node : nodes){
            System.out.print(node.label + " ");
        }
        System.out.println();
    }
}
