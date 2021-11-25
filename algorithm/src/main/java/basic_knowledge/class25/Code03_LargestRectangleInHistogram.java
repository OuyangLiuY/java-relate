package basic_knowledge.class25;

import java.util.Stack;

/**
 * 给定一个非负数组arr，代表直方图
 * 返回直方图的最大长方形面积
 * 测试链接：https://leetcode.com/problems/largest-rectangle-in-histogram
 */
public class Code03_LargestRectangleInHistogram {
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                int j = stack.pop();
                int k = stack.isEmpty() ? -1 : stack.peek();
                int curArea = (i - k - 1) * heights[j];
                maxArea = Math.max(maxArea, curArea);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int k = stack.isEmpty() ? -1 : stack.peek();
            int curArea = (heights.length - k - 1) * heights[j];
            maxArea = Math.max(maxArea, curArea);
        }
        return maxArea;
    }

    // 常数项优化，将系统得栈，使用数组代替
    public int largestRectangleArea1(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        int maxArea = 0;
        int[] stack = new int[heights.length];
        int index = -1;
        for (int i = 0; i < heights.length; i++) {
            while (index != -1 && heights[stack[index]] >= heights[i]) {
                int j = stack[index--];
                int k = (index == -1) ? -1 : stack[index];
                int curArea = (i - k - 1) * heights[j];
                maxArea = Math.max(maxArea, curArea);
            }
            stack[++index] = i;
        }
        while (index != -1) {
            int j = stack[index--];
            int k = index == -1 ? -1 : stack[index];
            int curArea = (heights.length - k - 1) * heights[j];
            maxArea = Math.max(maxArea, curArea);
        }
        return maxArea;
    }
}
