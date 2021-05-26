import java.util.*;
import java.util.stream.Collectors;

public class CollectionsStream {

    public static void main(String[] args) {

        // map 的 stream 流测试
        Map<String, List<String>> res = new HashMap<>();
        List<String> l1 = new ArrayList<>();
        l1.add("1");
        l1.add("2");
        l1.add("3");

        List<String> l2 = new ArrayList<>();
        l2.add("1");
        l2.add("2");
        List<String> l3 = new ArrayList<>();
        l3.add("1");
        l3.add("2");
        l3.add("3");
        l3.add("4");
        res.put("1",l1);
        res.put("2",l2);
        res.put("3",l3);

        LinkedHashMap<String, List<String>> ans = res.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparingInt(List::size)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

        System.out.println(ans);
    }
}
