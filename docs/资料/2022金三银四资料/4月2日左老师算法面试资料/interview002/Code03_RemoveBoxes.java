package interview002;

// 来自小红书
// 小红书第三题：
// 薯队长最近在玩一个游戏，这个游戏桌上会有一排不同颜色的方块，
// 每次薯队长可以选择一个方块，便可以消除这个方块以及和他左右相临的
// 若干的颜色相同的方块，而每次消除的方块越多，得分越高。
// 具体来说，桌上有以个方块排成一排 (1 <= N <= 200），
// 每个方块有一个颜色，用1~N之间的一个整数表示，相同的数宇代表相同的颜色，
// 每次消除的时候，会把连续的K个相同颜色的方块消除，并得到K*K的分数，
// 直到所有方块都消除。显然，不同的消除顺序得分不同，薯队长希望您能告诉他，这个游戏最多能得到多少分
// 体系学习班，代码46节，视频在47节，消箱子原题
// 本题测试链接 : https://leetcode.com/problems/remove-boxes/
public class Code03_RemoveBoxes {

	// arr[L...R]消除，而且前面跟着K个arr[L]这个数
	// 返回：所有东西都消掉，最大得分
	public static int func1(int[] arr, int L, int R, int K) {
		if (L > R) {
			return 0;
		}
		int ans = func1(arr, L + 1, R, 0) + (K + 1) * (K + 1);

		// 前面的K个X，和arr[L]数，合在一起了，现在有K+1个arr[L]位置的数
		for (int i = L + 1; i <= R; i++) {
			if (arr[i] == arr[L]) {
				ans = Math.max(ans, func1(arr, L + 1, i - 1, 0) + func1(arr, i, R, K + 1));
			}
		}
		return ans;
	}

	public static int removeBoxes1(int[] boxes) {
		int N = boxes.length;
		int[][][] dp = new int[N][N][N];
		int ans = process1(boxes, 0, N - 1, 0, dp);
		return ans;
	}

	public static int process1(int[] boxes, int L, int R, int K, int[][][] dp) {
		if (L > R) {
			return 0;
		}
		if (dp[L][R][K] > 0) {
			return dp[L][R][K];
		}
		int ans = process1(boxes, L + 1, R, 0, dp) + (K + 1) * (K + 1);
		for (int i = L + 1; i <= R; i++) {
			if (boxes[i] == boxes[L]) {
				ans = Math.max(ans, process1(boxes, L + 1, i - 1, 0, dp) + process1(boxes, i, R, K + 1, dp));
			}
		}
		dp[L][R][K] = ans;
		return ans;
	}

	public static int removeBoxes2(int[] boxes) {
		int N = boxes.length;
		int[][][] dp = new int[N][N][N];
		int ans = process2(boxes, 0, N - 1, 0, dp);
		return ans;
	}

	public static int process2(int[] boxes, int L, int R, int K, int[][][] dp) {
		if (L > R) {
			return 0;
		}
		if (dp[L][R][K] > 0) {
			return dp[L][R][K];
		}
		// 找到开头，
		// 1,1,1,1,1,5
		// 3 4 5 6 7 8
		// !
		int last = L;
		while (last + 1 <= R && boxes[last + 1] == boxes[L]) {
			last++;
		}
		// K个1 (K + last - L) last
		int pre = K + last - L;
		int ans = (pre + 1) * (pre + 1) + process2(boxes, last + 1, R, 0, dp);
		for (int i = last + 2; i <= R; i++) {
			if (boxes[i] == boxes[L] && boxes[i - 1] != boxes[L]) {
				ans = Math.max(ans, process2(boxes, last + 1, i - 1, 0, dp) + process2(boxes, i, R, pre + 1, dp));
			}
		}
		dp[L][R][K] = ans;
		return ans;
	}

}
