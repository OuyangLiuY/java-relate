package com.collections;

import com.google.common.base.*;
import com.google.common.collect.*;

import java.io.Serializable;
import java.util.*;

public class CollectionTest {


    public static void main(String[] args) {

        //创建普通的collection
        ArrayList<Object> list = Lists.newArrayList();
        HashSet<Object> set = Sets.newHashSet();
        HashMap<Object, Object> map = Maps.newHashMap();
        //不变的Collection
        ImmutableList<? extends Serializable> immutableList = ImmutableList.of("1", 2, 3, 4, 5);
        System.out.println(immutableList.toString());
        ImmutableSet<? extends Serializable> ofSet = ImmutableSet.of("1", 3, 5, 7, 9);

        ImmutableMap<String, String> ofMap = ImmutableMap.of("k1", "v1", "k2", "v2");


        /**
         * 当我们需要一个map中包含key为String类型，value为List类型的时候，以前我们是这样写的
         */
      /*  Map<String, List<Integer>> map = new HashMap<String,List<Integer>>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        map.put("aa", list);
        System.out.println(map.get("aa"));//[1, 2]*/

        //可以使用这种方式
        Multimap<Object, Object> multimap = ArrayListMultimap.create();
        multimap.put("aa",1);
        multimap.put("aa",2);
        System.out.println(multimap.get("aa"));

        //MultiSet: 无序+可重复   count()方法获取单词的次数  增强了可读性+操作简单
        //Multiset<String> set = HashMultiset.create();

        //Multimap: key-value  key可以重复
        //创建方式: Multimap<String, String> teachers = ArrayListMultimap.create();

        //BiMap: 双向Map(Bidirectional Map) 键与值都不能重复
        //创建方式:  BiMap<String, String> biMap = HashBiMap.create();

        //Table: 双键的Map Map--> Table-->rowKey+columnKey+value  //和sql中的联合主键有点像
        //创建方式: Table<String, String, Integer> tables = HashBasedTable.create();

        collectionsTransferToString();
        stringTransferToCollection();
        collectionFilter();
        setCollection();
        mapCollection();
    }

    /**
     * 将集合转换为特定规则的字符串
     */
    private static void collectionsTransferToString(){
        //use guava
        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        String join = Joiner.on("-").join(list);
        System.out.println(join);
        //把map集合转换为特定规则的字符串
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("str1",12);
        map.put("str2",13);

        String result = Joiner.on(",").withKeyValueSeparator("=").join(map);
        System.out.println(result);

    }

    /**
     * 将String转换为特定的集合
     */
    static void stringTransferToCollection(){
        //转换成list
        String str = "1-2-3-4-  5-    6   ";
        List<String> toList = Splitter.on("-").splitToList(str);
        System.out.println(toList);
        //guava还可以使用 omitEmptyStrings().trimResults() 去除空串与空格
        List<String> strings = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(str);
        System.out.println(strings);

        //转换成map
        String mStr = "x1=11,x2=23";
        Map<String, String> stringMap = Splitter.on(",").withKeyValueSeparator("=").split(mStr);
        System.out.println(stringMap.get("x1"));
        System.out.println(stringMap.get("x2"));

        /**guava还支持多个字符切割，或者特定的正则分隔*/
        String input = "aa.dd,,ff,,.";
        List<String> result = Splitter.onPattern("[.|,]").omitEmptyStrings().splitToList(input);
        System.out.println(result);
    }

    /**
     * 关于字符串的操作 都是在Splitter这个类上进行的
     */
    void stringOption(){

        // 判断匹配结果
        boolean matches = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).matches('K');
        System.out.println(matches); //true
        // 保留数字文本  CharMatcher.digit() 已过时   retain 保留
        String str = CharMatcher.digit().retainFrom("abc 123 efg "); //123

        String s1 = CharMatcher.inRange('0', '9').retainFrom("abc 123 efg"); // 123

        // 删除数字文本  remove 删除
        // String s2 = CharMatcher.digit().removeFrom("abc 123 efg");    //abc  efg
        String s2 = CharMatcher.inRange('0', '9').removeFrom("abc 123 efg"); // abc  efg

    }

    /**
     * 集合的过滤
     *
     */
   static void collectionFilter(){
        ImmutableList<String> names = ImmutableList.of("begin", "code", "Guava", "Java");
        Iterable<String> filter = Iterables.filter(names, Predicates.or(Predicates.equalTo("Guava"), Predicates.equalTo("Java")));
        System.out.println(filter);

        //自定义过滤条件， 使用自定义回调方法对Map的每个Value进行操作
       ImmutableMap<String, Integer> map = ImmutableMap.of("begin", 12, "code", 15);
       // Function<F, T> F表示apply()方法input的类型，T表示apply()方法返回类型
       Map<String, Integer> values = Maps.transformValues(map, input -> {
           if(input == null)
               return -1;
           if (input > 12)
               return input;
           else
               return input + 1;
       });
       System.out.println(values);
   }
    /**
     * set的交集，并集，差集
     */
    static void setCollection(){
        HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
        HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);
        //并集
        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println("union: ");
        //for (Integer integer : union)
            System.out.println(union.toString());

        //差集
        Sets.SetView<Integer> diff = Sets.difference(setA, setB);
        System.out.println("diff :");
        //for (Integer integer : diff)
            System.out.println(diff.toString());

        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        // 交集
        System.out.println("intersection : ");
        //for (Integer integer : intersection)
            System.out.println(intersection.toString());
    }
    /**
     * map的交集，并集，差集
     */
    static void mapCollection(){
        HashMap<String, Integer> mapA = Maps.newHashMap();
        mapA.put("a",1);mapA.put("b",2);mapA.put("c",3);

        HashMap<String, Integer> mapB = Maps.newHashMap();
        mapB.put("b",20);mapB.put("c",3);mapB.put("d",4);

        MapDifference mapDifference = Maps.difference(mapA, mapB);
        mapDifference.areEqual();
        Map entriesDiffering = mapDifference.entriesDiffering();
        Map entriesOnlyOnLeft = mapDifference.entriesOnlyOnLeft();
        Map entriesOnlyOnRight = mapDifference.entriesOnlyOnRight();
        Map entriesInCommon = mapDifference.entriesInCommon();

        System.out.println(entriesDiffering);
        System.out.println(entriesOnlyOnLeft);
        System.out.println(entriesOnlyOnRight);
        System.out.println(entriesInCommon);

    }
    static void checkElement(List list , String str,int count){
       //use java
        if(list!=null && list.size()>0)

        if(str!=null && str.length()>0)

        if(str !=null && !str.isEmpty())

        //use guava
        if(!Strings.isNullOrEmpty(str));

        //use java
        if(count <= 0)
            throw new IllegalArgumentException("must be positive : " + count);
        //use guava
        Preconditions.checkArgument(count > 0,"must be positive : %s",count);

        /**
         * checkArgument(boolean)  检查boolean是否为true，用来检查传递给方法的参数。 IllegalArgumentException
         * checkNotNull(T)      检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull。   NullPointerException
         * checkState(boolean)      用来检查对象的某些状态。	IllegalStateException
         * checkElementIndex(int index, int size)    检查index作为索引值对某个列表、字符串或数组是否有效。 index > 0 && index < size    IndexOutOfBoundsException
         * checkPositionIndexes(int start, int end, int size)   检查[start,end]表示的位置范围对某个列表、字符串或数组是否有效        IndexOutOfBoundsException
         */


    }
}
