import java.awt.desktop.SystemSleepEvent;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {
    public static void main(String[] args) {
        Collection<Student> students = Arrays.asList(
                new Student(1, "李梅", Grade.FIRST, 60),
                new Student(2, "李斯", Grade.SECOND, 80),
                new Student(3, "嬴政", Grade.THIRD, 70),
                new Student(4, "吕不韦", Grade.THIRD, 50));
        List<Integer> ageList = students.stream().filter(student -> student.getGrade().equals(Grade.THIRD))
                .sorted(Comparator.comparingInt(Student::getScore)).map(Student::getAge).collect(Collectors.toList());
        System.out.println(ageList.toString());

        //Individual value
        Stream<String> stream = Stream.of("a", "b", "c");
        //Arrays
        String[] ab = new String[]{"a", "b", "c"};
        Stream<String> stream1 = Stream.of(ab);
        Stream<String> stream2 = Arrays.stream(ab);
        //Collections
        ArrayList<String> list = new ArrayList<>();
        Stream<String> stream3 = list.stream();

        IntStream.of(1, 2, 4, 5).forEach(System.out::println);
        IntStream.range(1, 6).forEach(System.out::println);
        IntStream.rangeClosed(1, 6).forEach(System.out::println);
        //Stream

        Stream<String> streams = Stream.<String>of(new String[]{"1", "2", "3"});
        //List<String> list1 = streams.collect(Collectors.toList());
        //List<String> list2 = streams.collect(Collectors.toCollection(ArrayList::new));

        String str = streams.collect(Collectors.joining(","));
        System.out.println(str);

        stream.map(String::toUpperCase).forEach(System.out::println);
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
       // Stream<Integer> mapStream = inputStream.map(List::size);
        Stream<Integer> integerStream = inputStream.flatMap(Collection::stream);
        integerStream.forEach(System.out::println);
        Integer[] nums = new Integer[]{1, 2, 2, 3, 4, 5, 5, 6};
        Arrays.stream(nums).filter(n -> n<3).forEach(System.out::println);

    /*    Integer[] nums1 = new Integer[]{1,2,3,4,5,6};
        Stream stressam = Arrays.stream(nums);
        stressam.forEach(System.out::print);
        stressam.forEach(System.out::print);*/

        Integer[] nums1 = new Integer[]{1,2,3,4,5,6};
        Stream stream6 = Arrays.stream(nums);
        stream6.peek(System.out::print)
                .peek(System.out::print)
                .collect(Collectors.toList());
        System.out.println();
        Integer[] nums2 = new Integer[]{1,2,3,4,5,6};
        Arrays.stream(nums2).forEach(integer -> {
            System.out.println(integer);
            return;
        });


        Integer sum = Arrays.stream(nums).reduce(0, (Integer::sum));
        System.out.println(sum);
        //有初始化值
      //  Integer sum = Arrays.stream(nums).reduce(0, Integer::sum);
        //无初始化值
        Integer sum1 = Arrays.stream(nums).reduce(Integer::sum).get();

        Arrays.stream(nums).limit(3).forEach(System.out::print);
        System.out.println();
        Arrays.stream(nums).skip(2).forEach(System.out::print);

        System.out.println();
        Arrays.stream(nums).sorted((i1, i2) ->
                i2.compareTo(i1)).limit(3).forEach(System.out::print);
        System.out.println();
        Arrays.stream(nums).sorted(Comparator.naturalOrder()).skip(2).forEach(System.out::print);
        System.out.println();
        Arrays.stream(nums).sorted(Comparator.reverseOrder()).skip(2).forEach(System.out::print);
        System.out.println();
        System.out.println(Arrays.stream(nums).min(Comparator.naturalOrder()).get());
        System.out.println(Arrays.stream(nums).max(Comparator.naturalOrder()).get());
        Arrays.stream(nums).distinct().forEach(System.out::print);

        System.out.println(Arrays.stream(nums).allMatch(integer -> integer < 7));
        System.out.println(Arrays.stream(nums).anyMatch(integer -> integer< 2));
        System.out.println(Arrays.stream(nums).noneMatch(integer -> integer< 2));

        Collection<Student> studentss = Arrays.asList(
                new Student(1, "李梅", Grade.FIRST, 60),
                new Student(2, "李斯", Grade.SECOND, 80),
                new Student(3, "嬴政", Grade.THIRD, 70),
                new Student(4, "吕不韦", Grade.THIRD, 50));
        // 按年级分组
        studentss.stream().collect(Collectors.groupingBy(Student::getGrade)).forEach(((grade, students1) -> {
            System.out.println(grade);
            students1.forEach(student -> System.out.println(student.toString()));
        }));
        // 按分数段分组
        studentss.stream().collect(Collectors.partitioningBy(student -> student.getScore()>=60)).forEach(((grade, students1) -> {
            System.out.println(grade);
            students1.forEach(student -> System.out.println(student.toString()));
        }));

        Arrays.stream(nums).parallel().forEach(System.out::print);
        System.out.println(Arrays.stream(nums).parallel().reduce(Integer::sum).get());
        System.out.println();
        Arrays.stream(nums).forEach(System.out::print);
        System.out.println(Arrays.stream(nums).reduce(Integer::sum).get());

    }
}
