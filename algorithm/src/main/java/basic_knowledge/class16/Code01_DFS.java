package basic_knowledge.class16;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 图得深度遍历
 */
public class Code01_DFS {

    public static void dfs(Node node) {
        if (node == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Set<Node> set = new HashSet<>();
        stack.push(node);
        set.add(node);
        while (!stack.isEmpty()){
            Node cur = stack.pop();
            for (Node next : cur.nexts){
                if(!set.contains(cur)){
                    stack.push(cur);
                    stack.push(next);
                    set.add(cur);
                    System.out.println(cur.value);
                    break;
                }
            }
        }
    }
}
