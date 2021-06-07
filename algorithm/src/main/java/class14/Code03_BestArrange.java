package class14;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 一些项目要占用一个会议室宣讲，会议室不能同时容纳两个项目的宣讲。
 * 给你每一个项目开始的时间和结束的时间（时间以整数表示）
 * 你来安排宣讲的日程，要求会议室进行的宣讲的场次最多。返回最多的宣讲场次。
 */
public class Code03_BestArrange {
    // 包装会议
    public static class Program {
        public int start;
        public int end;

        public Program(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static int bestArrange1(Program[] programs) {
        if (programs == null || programs.length == 0) {
            return 0;
        }
        return process(programs,0,0);
    }

    // 还剩下的会议都放在programs里
    // done之前已经安排了多少会议的数量
    // timeLine目前来到的时间点是什么
    // 目前来到timeLine的时间点，已经安排了done多的会议，剩下的会议programs可以自由安排
    // 返回能安排的最多会议数量
    public static int process(Program[] programs, int done, int timeLine) {
        if (programs.length == 0) {
            return done;
        }
        // 还剩下会议
        int max = done;
        for (int i = 0; i < programs.length; i++) {
            if (programs[i].start >= timeLine) {
                Program[] next = copyAndExcept(programs, i);
                max = Math.max(max, process(next, done + 1, programs[i].end));
            }
        }
        return max;
    }

    private static Program[] copyAndExcept(Program[] programs, int index) {
        Program[] ans = new Program[programs.length - 1];
        int in = 0;
        for (int i = 0; i < programs.length; i++) {
            if (i != index) {
                ans[in++] = programs[i];
            }
        }
        return ans;
    }

    // 会议的开始时间和结束时间，都是数值，不会 < 0
    public static int bestArrange2(Program[] programs) {
        Arrays.sort(programs, new MyComparator());
        int count = 0;
        int timeLine = 0;
        // 依次遍历每一个会议，结束时间早的会议先遍历
        for (Program program : programs) {
            if (timeLine <= program.start) {
                count++;
                timeLine = program.end;
            }
        }
        return count;
    }

    public static class MyComparator implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            return o1.end - o2.end; // 哪个结束时间最小得排前面
        }
    }

    public static Program[] generatePrograms(int programSize, int timeMax) {
        Program[] programs = new Program[(int) (Math.random() * (programSize + 1))];
        for (int i = 0; i < programs.length; i++) {
            int s1 = (int) (Math.random() * timeMax + 1);
            int s2 = (int) (Math.random() * timeMax + 1);
            if (s1 == s2) {
                programs[i] = new Program(s1, s1 + 10);
            } else {
                programs[i] = new Program(Math.min(s1, s2), Math.max(s1, s2));
            }
        }
        return programs;
    }

    public static void main(String[] args) {
        int programSize = 12;
        int timeMax = 20;
        int timeTimes = 1000000;
        for (int i = 0; i < timeTimes; i++) {
            Program[] programs = generatePrograms(programSize, timeMax);
            if (bestArrange1(programs) != bestArrange2(programs)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }
}
