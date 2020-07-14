//给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。 
//
// 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。 
//
// 
//
// 示例: 
//
// 给定 nums = [2, 7, 11, 15], target = 9
//
//因为 nums[0] + nums[1] = 2 + 7 = 9
//所以返回 [0, 1]
// 
// Related Topics 数组 哈希表

package leetcode.editor.cn;
//JAVA:两数之和
public class P1{
    public static void main(String[] args) {
        Solution solution = new P1().new Solution();
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] ints = solution.twoSum(nums, target);
        System.out.println(ints[0]);
        System.out.println(ints[1]);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] twoSum(int[] nums, int target) {
            int[] temp = new int[2];
            for (int i = 0; i < nums.length-1; i++) {
                if(nums[i] + nums[i+1] == target){
                    temp[0] = i;
                    temp[1] = i+1;
                }
            }
            return temp;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}