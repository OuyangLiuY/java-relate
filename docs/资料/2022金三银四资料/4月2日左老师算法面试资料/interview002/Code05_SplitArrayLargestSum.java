package interview002;

// leetcode原题
// 测试链接：https://leetcode.com/problems/split-array-largest-sum/
public class Code05_SplitArrayLargestSum {

	public static int splitArray(int[] nums, int M) {
		long sum = 0;
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
		}
		long l = 0;
		long r = sum;
		long ans = 0;
		while (l <= r) {
			long mid = (l + r) / 2;
			long cur = getNeedParts(nums, mid);
			if (cur <= M) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return (int) ans;
	}

	public static int getNeedParts(int[] arr, long aim) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > aim) {
				return Integer.MAX_VALUE;
			}
		}
		int parts = 1;
		int all = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (all + arr[i] > aim) {
				parts++;
				all = arr[i];
			} else {
				all += arr[i];
			}
		}
		return parts;
	}

	public static int[] randomArray(int len, int maxValue) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * maxValue);
		}
		return arr;
	}

	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

}
