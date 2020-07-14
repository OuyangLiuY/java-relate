import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class NewMap {

    static class BeanA{
        String name;
        int age;

        public BeanA(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "BeanA{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
    static class BeanB{
        String name;
        int age;

        public BeanB(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "BeanB{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) {
        //map : foreach
        Map<Integer,BeanA> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i,new BeanA("李斯" + i,i + 10));
        }
        map.forEach((key,value)->{
            System.out.println(key + " = "  + value.toString());
        });
        /*     @Override
             public <V> Function<V, BeanB> compose(Function<? super V, ? extends BeanA> before) {
                 return null;
             }
             @Override
             public <V> Function<BeanA, V> andThen(Function<? super BeanB, ? extends V> after) {
                 return null;
             }*/
        Stream<BeanB> bStream = map.values().stream().map(new Function<BeanA, BeanB>() {
            @Override
            public BeanB apply(BeanA beanA) {
                return new BeanB(beanA.getName(), beanA.getAge());
            }
        });
        bStream.forEach(System.out::println);
        Stream<BeanB> beanBStream = map.values().stream().map(beanA -> new BeanB(beanA.getName(), beanA.getAge()));
        beanBStream.forEach(System.out::println);


        Map<Integer,String> maps = new HashMap<>();
        for (int i = 0; i <10 ; i++) {
            maps.put(i,"val" + i);
        }

        //computeIfPresent,当key存在时，才会做相关处理
        //对于可以为3的值，内部会先判断值是否存在，存在，则做value + key 的拼接操作，如果不存在，则不做操作
        maps.computeIfPresent(3,(num,val) ->  val+ num);
        System.out.println(  maps.get(3));
        //如果存在可以为6的数据，则置空，相当于做了删除操作
        maps.computeIfPresent(6,(num,val) ->  null);
        System.out.println(  maps.containsKey(6));

        //computeIfAbsent,当key不存在时，才会做相关处理
        //先判断key为20的之元素是否存在，不存在，则添加
        maps.computeIfAbsent(20,key-> "val" + key);
        System.out.println(maps.containsKey(20));
        //先判断key为20的之元素是否存在，存在，则不做任何处理
        maps.computeIfAbsent(3,key -> "newVal");
        System.out.println(maps.get(3));

        System.out.println(maps.get(7));
        maps.remove(7,"val");
        System.out.println(maps.get(7));
        maps.remove(7,"val7");
        System.out.println(maps.containsKey(7));

        String existValue = maps.getOrDefault(8, "defaultValue");
        System.out.println(existValue);
        String defaultValue = maps.getOrDefault(12, "defaultValue");
        System.out.println(defaultValue);

        //concat是String类的拼接操作
        //merge方法，会先判断进行合并的key是否存在，不存在，怎会添加元素
        maps.merge(12,"val12",(value,newValue)->value.concat(newValue));
        System.out.println(maps.get(12));
        //merge方法，会先判断进行合并的key是否存在，存在，怎会拼接新旧元素
        maps.merge(12,"concat",(value,newValue)->value.concat(newValue));
        System.out.println(maps.get(12));

        String a = "abc";
        String concat = a.concat("-def-");
        System.out.println(concat);

    }
}
