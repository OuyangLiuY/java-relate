package class11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *  求一颗树的最大宽度
 */
public class Code05_TreeMaxWidth {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }
    // 使用辅助hash实现
    public static int maxWidthUseMap(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        // key 在 哪一层，value
        HashMap<Node, Integer> levelMap = new HashMap<>();
        levelMap.put(head, 1);
        int curLevel = 1; // 当前你正在统计哪一层的宽度
        int curLevelNodes = 0; // 当前层curLevel层，宽度目前是多少
        int max = 0;
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            int curNodeLevel = levelMap.get(cur);
            if(cur.left != null){
                levelMap.put(cur.left,curNodeLevel + 1);
                queue.add(cur.left);
            }
            if(cur.right != null){
                levelMap.put(cur.right,curNodeLevel + 1);
                queue.add(cur.right);
            }
            if(curNodeLevel == curLevel){
                curLevelNodes++;
            }else {
                max = Math.max(max,curLevelNodes);
                curLevel ++;
                curLevelNodes = 1;
            }
        }
        max = Math.max(max,curLevelNodes);
        return max;
    }
}
