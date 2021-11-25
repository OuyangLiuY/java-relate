package basic_knowledge.class06;

import java.util.Arrays;
import java.util.Comparator;

public class Code01_Comparator {

    public static class Student {
        public String name;
        public int id;
        public int age;

        public Student(String name, int id, int age) {
            this.name = name;
            this.id = id;
            this.age = age;
        }
    }

    // 任何比较器：
    // compare方法里，遵循一个统一的规范：
    // 返回负数的时候，认为第一个参数应该排在前面
    // 返回正数的时候，认为第二个参数应该排在前面
    // 返回0的时候，认为无所谓谁放前面
    public static class IdShengAgeJiangOrder implements Comparator<Student>{
        @Override
        public int compare(Student o1, Student o2) {

            return o1.id != o2.id ? o1.id - o2.id : o2.age -  o1.age;
        }
    }

    public static class AgeJiangOrder implements Comparator<Student>{
        // 返回负数的时候，第一个参数排在前面
        // 返回正数的时候，第二个参数排在前面
        // 返回0的时候，谁在前面无所谓
        @Override
        public int compare(Student o1, Student o2) {
//            if(o1.age < o2.age){
//                return 1;
//            }else if(o1.age > o2.age){
//                return -1;
//            }else {
//                return 0;
//            }
            // 以上 判断等同于下面这行代码
            return o2.age - o1.age;
        }
    }

    public static class IntegerComp implements Comparator<Integer> {

        // 如果返回负数，认为第一个参数应该拍在前面
        // 如果返回正数，认为第二个参数应该拍在前面
        // 如果返回0，认为谁放前面都行

        // 第一參數減去 第二參數 該數組排序之後就是升序
        // 第二參數減去 第一參數 該數組排序之後就是降序
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg1 - arg0;
//            return arg0 - arg1;
        }

    }

    public static void main(String[] args) {
        Integer[] arr = { 5, 4, 3, 2, 7, 9, 1, 0 };

        Arrays.sort(arr, new IntegerComp());

        System.out.println(Arrays.toString(arr));

        System.out.println("===========================");

        Student student1 = new Student("A", 4, 40);
        Student student2 = new Student("B", 4, 21);
        Student student3 = new Student("C", 3, 12);
        Student student4 = new Student("D", 3, 62);
        Student student5 = new Student("E", 3, 42);
        // D E C A B

        Student[] students = new Student[] { student1, student2, student3, student4, student5 };
        System.out.println("第一条打印");

        Arrays.sort(students, new IdShengAgeJiangOrder());
        for (int i = 0; i < students.length; i++) {
            Student s = students[i];
            System.out.println(s.name + "," + s.id + "," + s.age);
        }
    }
}
