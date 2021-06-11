package class16;

/**
 * 图得边
 */
public class Edge {
    public int weight; // 边得权重
    public Node from; //
    public Node to;
    public Edge(int weight,Node from,Node to){
        this.weight = weight;
        this.from = from;
        this.to = to;
    }
}
