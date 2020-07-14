import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Value<T> {
    public static<T> T defaultValue() {
        return null;
    }
    public T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
    public static void main(String[] args) {

        long b1 = System.currentTimeMillis();
        long [] num = new long[200000000];
        for (int i = 0; i < num.length; i++) {
            num[i] = new Random().nextLong();
        }
        long end1 = System.currentTimeMillis();
        System.out.println(end1 - b1 + "ms") ;

        long nextLong = new Random().nextInt(10000000);
        System.out.println(nextLong);

        long b = System.currentTimeMillis();
        Arrays.setAll(num,index -> ThreadLocalRandom.current().nextInt(10000000));
        Arrays.stream(num).limit(10).forEach(i -> System.out.print(i + "\t"));

        System.out.println();
        System.out.println();

        long end = System.currentTimeMillis();
        System.out.println(end - b + "ms") ;
        long end2 = System.currentTimeMillis();
        Arrays.sort(num);
//        Arrays.parallelSort(num);
        long ends = System.currentTimeMillis();
        Arrays.stream(num).limit(10).forEach(i -> System.out.print(i + "\t"));
        System.out.println();
        System.out.println((ends -end2) + "ms");
    }
}