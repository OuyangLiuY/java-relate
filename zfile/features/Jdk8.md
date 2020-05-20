# JDK 8 新特性

## Lambda表达式

### 函数式接口

函数式接口是新增的一种接口定义。

用@FunctionalInterface修饰的接口叫做函数式接口,或者,函数式接口就是一个只具有一个抽象方法的普
通接口,@FunctionalInterface可以起到校验的作用。

```java
@FunctionalInterface
public interface PersonCallback {
    void callback(Person person);
}
```

在JDK7中其实就已经有一些函数式接口了,比如Runnable、Callable、FileFilter等等。

在JDK8中也增加了很多函数式接口,比如java.tank.util.function包。

比如这四个常用的接口:

| **接口**  | **描述**                         |
| --------- | -------------------------------- |
| Supplier  | 无参数，返回一个结果             |
| Function  | 接受一个参数，返回一个结果       |
| Consumer  | 接受一个参数，无返回结果         |
| Predicate | 接受一个参数，返回一个布尔值结果 |

**Java8中加了那么多函数式结果的作用？**

一个Lambda表达式其实也可以理解为一个函数式接口的实现者，但是作为表达式，它的写法有多种多样，例如：

- () -> {return 0;} ，没有传入参数，有返回值
- (int i) -> {return 0 ;} ，传入一个参数，有返回值
- (int i) -> {System.out.println(i)}，传入一个int类型参数，但是没有返回值
- (int i, int j) -> {System.out.println(i)},传入两个int类型的参数,但是没有返回值
- (int i, int j) -> {return i+j;},传入两个int类型的参数,返回一个int值
- (int i, int j) -> {return i>j;},传入两个int类型的参数,返回一个boolean值

等等，还有许多种情况，那么每种表达式的写法其实都应该是某个函数式接口的实现类，需要特定函数式接口进行对应，比如撒谎嗯面的四种情况分别对应：

`Supplier<T>` ,` Function<T,R> `, `Consumer<T> `, `BiConsumer<T, U> `,` BiFunction<T,U, R> `,` BiPredicate<T, U>` 。

### 接口的默认方法与静态方法

在JDK7中，如果相对接口Collection增加一个方法，那么你需要修改它所有的实现类源码（这非常恐怖），那么在Java8之前是怎么设计这个问题的呢？用的是抽象类，比如：

现在有一个接口PersonInterface 接口，里面有一个抽象方法：

```java
public 	interface	PersonInterface{
	void getName();
}
```

有三个实现类：

```java
public class  YellowPerson implements PersonInterface{
    	@Override
    	public volid getName(){
            	System.out.println("yellow");
        }
}
```

```java
public class  BlackPerson implements PersonInterface{
    	@Override
    	public volid getName(){
            	System.out.println("black");
        }
}
```

```java
public class  WhitePerson implements PersonInterface{
    	@Override
    	public volid getName(){
            	System.out.println("white");
        }
}
```

现在需要在PersonInterface接口中新增一个方法，那么势必它的三个实现类都需要做相应改动才能编译通过，那么此时我们在最开始设计的时候，其实就可以增加一个抽象类PerosnAbstract，三个实现类改为继承这个抽象类，按照这种设计方法，对PersonInterface接口中新增一个方法是，其实只需要改动PersonAbstract类去实现新增的方法，其他类就不需要改动了。

```java
public interface PersonInterface {
void getName();
void walk();
}
public abstract class PersonAbstract implements PersonInterface {
@Override
public void walk() {
System.out.println("walk");
}
}
public class BlackPerson extends PersonAbstract {
@Override
public void getName() {
System.out.println("black");
}
}
public class WhitePerson extends PersonAbstract {
@Override
public void getName() {
System.out.println("white");
}
}
public class YellowPerson extends PersonAbstract {
@Override
public void getName() {
System.out.println("yellow");
}
}
```

那么在Java8中支持直接在接口中添加已经实现了的方法，一种是Default放（默认方法），一种是Static方法（静态方法）

#### 接口默认方法

在接口中用default修饰的方法称为默认方法
接口中的默认方法一定要有默认实现(方法体),接口实现者可以继承它,也可以覆盖它。

```java
default void testDefault(){
System.out.println("default");
};
```



#### 静态方法

在接口中用static修饰的方法称为静态方法。

```java
static void testStatic(){
System.out.println("static");
};
```

因为有了默认方法和静态方法,所以你不用去修改它的实现类了,可以进行直接调用。

#### 方法引用

有个函数式接口Consumer,里面有个抽象方法accept能够接收一个参数但是没有返回值,这个时候我想实现
accept方法,让它的功能为打印接收到的那个参数,那么我可以使用Lambda表达式这么做:

```java
Consumer<String> consumer = s -> System.out.println(s);
consumer.accept("诸葛亮");
```

但是其实我想要的这个功能PrintStream类(也就是System.out的类型)的println方法已经实现了,这一步还可以
再简单点,如:

```java
Consumer<String> consumer = System.out::println;
consumer.accept("诸葛孔明");
```

这就是方法引用,方法引用方法的参数列表必须与函数式接口的抽象方法的参数列表保持一致,返回值不作要
求。

####  使用方法

- 引用方法
- 引用构造方法
- 引用数组

引用方法

- 实例对象::实例方法名
- 类名::静态方法名
- 类名::实例方法名

#### 实例对象::实例方法名

```java
//Consumer<String> consumer = s -> System.out.println(s);
Consumer<String> consumer = System.out::println;
consumer.accept("xxx");
```

System.out代表的就是PrintStream类型的一个实例,println是这个实例的一个方法。

#### 类名::静态方法名

```java
  Function<Long,Long> f = Math::abs;
        Long result = f.apply(-10L);
        System.out.println(result);
```

#### 类名::实例方法名

若Lambda表达式的参数列表的第一个参数,是实例方法的调用者,第二个参数(或无参)是实例方法的参数时,就
可以使用这种方法

```java
   BiPredicate<String,String> b = String::contains;
        System.out.println(b.test("a", "a"));
```

String是一个类而equals为该类的定义的实例方法。BiPredicate中的唯一抽象方法test方法参数列表与equals方法
的参数列表相同,都是接收两个String类型参数。

#### 引用构造器

在引用构造器的时候,构造器参数列表要与接口中抽象方法的参数列表一致,格式为 类名::new。如:

```java
       Function<Integer,StringBuffer> ss = StringBuffer::new;
        StringBuffer apply = ss.apply(10);
```

Function接口的apply方法接收一个参数,并且有返回值。在这里接收的参数是Integer类型,与StringBuffer类的
一个构造方法StringBuffer(int capacity)对应，而返回值就是StringBuffer类型，上面这段代码的功能就是创建一个Fucntion实例，并把他apply方法实现为创建一个初始大小的StringBuffer对象。

#### 引用数组

引用数组和引用构造器很像，格式为类型[]::new，其中类型可以为基本类型也可以是类。如：

```java
// Function<Integer, int[]> fun = n -> new int[n];
Function<Integer, int[]> fun = int[]::new;
int[] arr = fun.apply(10);
Function<Integer, Integer[]> fun2 = Integer[]::new;
Integer[] arr2 = fun2.apply(10);
```

### Optional

空指针异常是导致Java应用程序失败的最常见原因,以前,为了解决空指针异常,Google公司著名的Guava项目引入了Optional类,Guava通过使用检查空值的方式来防止代码污染,它鼓励程序员写更干净的代码。受到Google Guava的启发,Optional类已经成为Java 8类库的一部分。

Optional实际上是个容器：它可以保存类型T的值，或者仅仅保存null。Optional提供很多有用的方法，这样我们就不用显示的进行空置检测。

创建Optional对象的几个方法：

- 1、创建Optional.of(T value)，返回一个Optional对象，value不能为空，否则会出空指针异常
- 2、Optional.ofNullable(T value)，返回一个Optional对象，value可以为空
- 3、Optional.empty()，代表为空

其他API：

```java
optional.isPresent() 		//是否存在值(不为空)
```

```java
optional.ifPresent(Consumer<? super T> consumer) 		//如果存在值则执行consumer
```

```java
optional.get() 				///获取value
```

```java
optional.orElse(T other) //如果没值则返回other
```

```java
optional.orElseGet(Supplier<? extends T> other)   		///如果没值则执行other并返回
```

```java
optional.orElseThrow(Supplier<? extends X> exceptionSupplier)
    //如果没值则执行exceptionSupplier,并抛出异常
```

经常使用的方式：

```java
public class Order {
String name;
public String getOrderName(Order order ) {
// if (order == null) {
// return null;
//return order.name;
//    Optional<Order> orderOptional = Optional.ofNullable(order);
//if (!orderOptional.isPresent()) {
//return null;
//}
//return orderOptional.get().name;
//} 
return Optional.ofNullable(order).map(order1 -> order1.name).orElse(null);
}
}
```

这个优化过程中map()起了很大作用。

**高级API:**

- 1、optional.map(Function<? super T, ? extends U> mapper),映射,映射规则由function指定,返回映射值
  的Optional,所以可以继续使用Optional的API。
- 2、optional.flatMap(Function<? super T, Optional< U > > mapper),同map类似,区别在于map中获取的返
  回值自动被Optional包装,flatMap中返回值保持不变,但入参必须是Optional类型。
- 3、optional.filter(Predicate<? super T> predicate),过滤,按predicate指定的规则进行过滤,不符合规则则
  返回empty,也可以继续使用Optional的API。

#### Optional总结

使用 Optional 时尽量不直接调用 Optional.get() 方法, Optional.isPresent() 更应该被视为一个私有方法, 应依赖于
其他像 Optional.orElse(), Optional.orElseGet(), Optional.map() 等这样的方法.

## Stream

Java8中的Stream是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种非常便利、高效的聚合操作（aggregate operation），或者大批量数据操作（bulk data operation）。Stream API借助 于同样新出现的Lamdba表达式，极大的提高编程效率和程序可读性。同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用fork/join并行方式来拆分和加速处理过程，通常编写并行代码很难而且容易出错，但使用Stream API无需编写一行多线程的代码，就可以很方便地写出高性能地并发程序。所以说，Java8中首次出现地java.tank.util.stream是一个函数语言+多核时代综合影响地产物。

在传统地J2EE应用中，Java代码经常不得不依赖于关系型数据库地操作如：取平均值、取最大最小值、取汇总值、或者进行分组等等类似地这些操作。

但在当今这个数据大爆炸地时代，在数据来源多样化、数据海量化地今天，很多时候不得不脱离RDBMS，或者以底层返回地数据为基础进行更上层地数据统计。而Java地集合API中，仅仅有极少量地辅助型方法，更多地时候是程序员需要用Iterator来遍历集合，完成相关地聚合应用逻辑。这是一种远不够高效、笨拙地方法。

所以此时可以在Java8中使用Stream，代码更加简单易读，而且使用并发模式，程序执行速度更快。

```java
public static  public static void main(String[] args) {
        Collection<Student> students = Arrays.asList(
                new Student(1, "李梅", Grade.FIRST,60),
                new Student(2, "李斯", Grade.SECOND,80),
                new Student(3, "嬴政", Grade.THIRD,70),
                new Student(4, "吕不韦", Grade.THIRD,50));
        List<Integer> ageList = students.stream().filter(student -> student.getGrade().equals(Grade.THIRD)).sorted(Comparator.comparingInt(Student::getScore)).map(Student::getAge).collect(Collectors.toList());
        System.out.println(ageList.toString());
    }
```



### 什么是Stream：

Stream不是集合元素，它不是数据结构并且不保存数据，它是有关算法和计算的，它更像一个高级版本的Iterator。

### Stream的特点：

- 1、Iterator，用户只能显示地一个一个遍历元素并对其执行某些操作；Stream，用户只要给出需要对其包含地元素执行什么操作，比如“过滤掉长度大于10地字符串”、“获取每个字符串地首字母”等，Stream会隐士地在内部进行遍历，做出相应地数据转换。
- 2、Stream就如同一个Iterator，单向，不可往复，数据只能遍历一次，遍历一次后即用尽了，就好比流水流过，一去不复返。
- 3、Stream可以并行化操作，Iterator只能命令式的、串行化操作。顾名思义，当使用串行方式去遍历时，每个item读完再读下一个item。而使用并行去遍历，数据会被分成多个段，其中每一个都再不同地线程中处理，然后将结果一起输出，Stream的并行操作依赖于Java7中引入的Fork/Join框架来拆分任务和加速处理过程。

### Stream构成：

当我们使用一个流的时候，通常包括三个基本步骤：

获取一个数据源 ————>数据转换————>执行操作获取想要的结果，每次转换原来Stream对象不改变，返回一个新的Stream对象（可以有多次转换），这就允许对其操作可以像链条一样排列，变成一个管道，如下图所示：



### 生成Stream Source的方式：

- 从Collection和数据生成:
  - Collection.stream()
  - Arrays.stream(T array)
  - Stream.of(T t)
- 从 BufffferedReader
  - java.io.BufffferedReader.lines()
- 静态工厂
  - java.tank.util.stream.IntStream.range()
  - java.nio.fifile.Files.walk()
- 自己构建
  - java.tank.util.Spliterator
- 其他
  - Random.ints()
  - BitSet.stream()
  - Pattern.splitAsStream(java.lang.CharSequence)
  - JarFile.stream()

### Stream的操作类型：

- **中间操作(Intermediate Operation)：**一个流可以后面跟随零个或多个 intermediate 操作。其目的主要时打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用，这类操作都是惰性化的，就是说，仅仅调用到这类方法，并没有真正开始流的遍历。

- **终止操作（Terminal Operation）:**一个流只能有一个 terminal 操作，当这个操作执行后，流就被使

  用“光”了，无法再被操作。所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始

  流的遍历，并且会生成一个结果。

Intermediate Operation又可以分为两种类型：

- 无状态操作：操作时无状态的，不需要知道集合中其他元素的状态，每个元素之间时相互独立的，比如map()、filter()等操作。
- 有状态操作：有状态操作，操作时需要知道集合中其他元素的状态才能进行的，比如sort()、distinct()。

Terminal Operation从逻辑上可以分为两种：

- 短路操作（short-circuiting)：短路操作时指不需要处理完所有元素即可结束整个过程。
- 非短路操作（non-short-circuiting）：非短路操作时需要处理完所有元素之后才能结束整个过程。

### Stream的使用：

简单说，对 Stream 的使用就是实现一个 fifilter-map-reduce 过程，产生一个最终结果，或者导致一个副作用。

### 构造流的几种常见方式：

```java
		//Individual value
        Stream<String> stream = Stream.of("a", "b", "c");
        //Arrays
        String[] ab = new String[]{"a","b","c"};
        Stream<String> stream1 = Stream.of(ab);
        Stream<String> stream2 = Arrays.stream(ab);
        //Collections
        ArrayList<String> list = new ArrayList<>();
        Stream<String> stream3 = list.stream();
```

需要注意的是，对于基本数值型，目前有三种对应的包装类型 Stream：

IntStream、LongStream、DoubleStream。当然也可以使用 Stream、Stream 、Stream，但是boxing和unboxing会很耗时，所以特别为这三种基本数据类型提供了对应的Stream。

Java 8 中还没有提供其他数值型Stream，因为这将导致扩增的内容较多，而常规的数值型聚合运算可以通过上面三种Stream进行，

### 数值流的构造：

```java
  IntStream.of(1,2,4,5).forEach(System.out::println);
  IntStream.range(1,6).forEach(System.out::println);
  IntStream.rangeClosed(1,6).forEach(System.out::println);
```



### 流转换为其他数据结构

```java
   Stream<String> streams = Stream.<String>of(new String[]{"1", "2", "3"});
   List<String> list1 = streams.collect(Collectors.toList());
   List<String> list2 = streams.collect(Collectors.toCollection(ArrayList::new));
   String str = streams.collect(Collectors.joining(","));
   System.out.println(str);     
```

一个 Stream 只可以使用一次，上面的代码为了简洁而重复使用了数次。

### 流的典型用法:

### map/flatMap

先来看map。如果你熟悉 scala 这类函数式语言，对这个方法应该很了解，它的作用就是把 input Stream

的每一个元素，映射成 output Stream 的另外一个元素。

```java
Stream<String> stream = Stream.of("a", "b", "c");
stream.map(String::toUpperCase).forEach(System.out::println);
```

这段代码把所有的字母转换为大写。map 生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap。

```java
         Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
       // Stream<Integer> mapStream = inputStream.map(List::size);
        Stream<Integer> integerStream = inputStream.flatMap(Collection::stream);
        integerStream.forEach(System.out::println);
```

### filter

fifilter 对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};
Arrays.stream(nums).filter(n -> n<3).forEach(System.out::println);
```

将小于3的数字留下来。

### forEach

forEach 是 terminal 操作，因此它执行后，Stream 的元素就被“消费”掉了，你无法对一个 Stream 进行两次terminal 运算。下面的代码会报错。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};
Stream stream = Arrays.stream(nums);
stream.forEach(System.out::print);
stream.forEach(System.out::print);
```

相反，具有相似功能的 intermediate 操作 peek 可以达到上述目的。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};
 Stream stream = Arrays.stream(nums);
 stream
 .peek(System.out::print)
 .peek(System.out::print)
 .collect(Collectors.toList());
//打印结果
//112233445566
//得知 peek，是将操作同时执行两次
```

forEach 不能修改自己包含的本地变量值，也不能用 break/return 之类的关键字提前结束循环。下面的代码还是打印出所有元素，并不会提前返回。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};
 Arrays.stream(nums).forEach(integer -> {
 	System.out.print(integer);
	return;
 });
```

**forEach** 和常规 和常规 **for** 循环的差异不涉及到性能，它们仅仅是函数式风格与传统循环的差异不涉及到性能，它们仅仅是函数式风格与传统 **Java** 风格的差别。 

### reduce

这个方法的主要作用是把 Stream 元素组合起来。它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。例如 Stream 的 sum 就相当于：

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};
Integer sum = Arrays.stream(nums).reduce(0, (integer, integer2) ->
integer+integer2);
 System.out.println(sum);
```

也有没有起始值的情况，这时会把 Stream 的前面两个元素组合起来，返回的是 Optional。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};  	
//有初始化值
Integer sum = Arrays.stream(nums).reduce(0, Integer::sum);        
//无初始化值
Integer sum1 = Arrays.stream(nums).reduce(Integer::sum).get();
```

### limit/skip

limit 返回 Stream 的前面 n 个元素；skip 则是扔掉前 n 个元素。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};  
Arrays.stream(nums).limit(3).forEach(System.out::print);
//123
System.out.println();
Arrays.stream(nums).skip(2).forEach(System.out::print);
//3456
```

### sorted

对 Stream 的排序通过 sorted 进行，它比数组的排序更强之处在于你可以首先对 Stream 进行各类 map、fifilter、limit、skip 甚至 distinct 来减少元素数量后，再排序，这能帮助程序明显缩短执行时间。

```java
Integer[] nums = new Integer[]{1,2,3,4,5,6};  
Arrays.stream(nums).sorted((i1, i2) ->
                i2.compareTo(i1)).limit(3).forEach(System.out::print);
 // 654  
System.out.println();   
Arrays.stream(nums).sorted(Comparator.naturalOrder()).skip(2).forEach(System.out::print);
//3456
System.out.println();
Arrays.stream(nums).sorted(Comparator.reverseOrder()).skip(2).forEach(System.out::print);
//4321
```



### min/max/distinct

```java
Integer[] nums = new Integer[]{1, 2, 2, 3, 4, 5, 5, 6};
System.out.println(Arrays.stream(nums).min(Comparator.naturalOrder()).get());
//  1    
System.out.println(Arrays.stream(nums).max(Comparator.naturalOrder()).get());
// 6
Arrays.stream(nums).distinct().forEach(System.out::print);
//123456
```

### Match

- allMatch：Stream 中全部元素符合传入的 predicate，返回 true
- anyMatch：Stream 中只要有一个元素符合传入的 predicate，返回 true
- noneMatch：Stream 中没有一个元素符合传入的 predicate，返回 true

它们都不是要遍历全部元素才能返回结果。例如 allMatch 只要一个元素不满足条件，就 skip 剩下的所有元素，返回 false。

```java
Integer[] nums = new Integer[]{1, 2, 2, 3, 4, 5, 5, 6};
System.out.println(Arrays.stream(nums).allMatch(integer -> integer < 7));
//true        
System.out.println(Arrays.stream(nums).anyMatch(integer -> integer< 2));
//true        
System.out.println(Arrays.stream(nums).noneMatch(integer -> integer< 2));
//false
```



### 用Collectors来进行reduction操作

java.tank.util.stream.Collectors 类的主要作用就是辅助进行各类有用的 reduction 操作,例如转变输出为 Collection，把 Stream 元素进行归组。

### groupingBy/PartitioningBy

例如对上面的Student进行按年级进行分组：

```java
  Collection<Student> students = Arrays.asList(
                new Student(1, "李梅", Grade.FIRST, 60),
                new Student(2, "李斯", Grade.SECOND, 80),
                new Student(3, "嬴政", Grade.THIRD, 70),
                new Student(4, "吕不韦", Grade.THIRD, 50));
        // 按年级分组
        students.stream().collect(Collectors.groupingBy(Student::getGrade)).forEach(((grade, students1) -> {
            System.out.println(grade);
            students1.forEach(student -> System.out.println(student.toString()));
        }));
```

打印结果：

```
SECOND
Student{age=2, name='李斯', grade=SECOND}
THIRD
Student{age=3, name='嬴政', grade=THIRD}
Student{age=4, name='吕不韦', grade=THIRD}
FIRST
Student{age=1, name='李梅', grade=FIRST}
```

例如对上面的Student进行按分数段进行分组：

```java
 // 按分数段分组
        studentss.stream().collect(Collectors.partitioningBy(student -> student.getScore()>=60)).forEach(((grade, students1) -> {
            System.out.println(grade);
            students1.forEach(student -> System.out.println(student.toString()));
        }));
```

打印结果：

```
false
Student{age=4, name='吕不韦', grade=THIRD}
true
Student{age=1, name='李梅', grade=FIRST}
Student{age=2, name='李斯', grade=SECOND}
Student{age=3, name='嬴政', grade=THIRD}
```



### parallelStream

parallelStream其实就是一个并行执行的流.它通过默认的ForkJoinPool,可以提高你的多线程任务的速度。

### parallelStream使用

```java
 Arrays.stream(nums).parallel().forEach(System.out::print);
        
System.out.println(Arrays.stream(nums).parallel().reduce(Integer::sum).get());
        
System.out.println();
        
Arrays.stream(nums).forEach(System.out::print);
        
System.out.println(Arrays.stream(nums).reduce(Integer::sum).get());

```



### parallelStream要注意的问题

parallelStream底层是使用的ForkJoin。而ForkJoin里面的线程是通过ForkJoinPool来运行的，Java 8为

ForkJoinPool添加了一个通用线程池，这个线程池用来处理那些没有被显式提交到任何线程池的任务。它是

ForkJoinPool类型上的一个静态元素。它拥有的默认线程数量等于运行计算机上的处理器数量，所以这里就出现

了这个java进程里所有使用parallelStream的地方实际上是公用的同一个ForkJoinPool。parallelStream提供了更简单的并发执行的实现，但并不意味着更高的性能，它是使用要根据具体的应用场景。如果cpu资源紧张

parallelStream不会带来性能提升；如果存在频繁的线程切换反而会降低性能。

### Stream总结

1. 不是数据结构，它没有内部存储，它只是用操作管道从 source（数据结构、数组、generator function、

   IO channel）抓取数据。

2. 它也绝不修改自己所封装的底层数据结构的数据。例如 Stream 的 fifilter 操作会产生一个不包含被过滤元素的新 Stream，而不是从 source 删除那些元素。

3. 所有 Stream 的操作必须以 lambda 表达式为参数。

4. 惰性化，很多 Stream 操作是向后延迟的，一直到它弄清楚了最后需要多少数据才会开始，Intermediate

   操作永远是惰性化的。

5. 当一个 Stream 是并行化的，就不需要再写多线程代码，所有对它的操作会自动并行进行的。

## Map集合

Map是不支持Stream流的，因为map接口没有像collection接口那样，定义了stream方法，但是我们可以对其key，value，entry使用流操作，如果：

`map.keySet().stream,map.values().stream()，map.entrySet().stream`

### forEach:

```java
Map<Object,Object> map = new HashMap<>();
for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i,new BeanA("李斯" + i,i + 10));
}
map.forEach((key,value)->{
    System.out.println(key + " = "  + value.toString());
});
```

map对象的转化输出：

```java
Stream<BeanB> bStream = map.values().stream().map(new Function<BeanA, BeanB>() {
    @Override
    public BeanB apply(BeanA beanA) {
        return new BeanB(beanA.getName(), beanA.getAge());
    }
});
bStream.forEach(System.out::println);
  Stream<BeanB> beanBStream = map.values().stream().map(beanA -> new BeanB(beanA.getName(), beanA.getAge()));
        beanBStream.forEach(System.out::println);
```

### computeIfPresen：

computeIfPresent(),先判断key存在是否存在，存在，才会做处理

```java
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
```

### computeIfAbsent：

computeIfAbsent()，先判断key存在是否存在，不存在，才会做处理

```java
//computeIfAbsent,当key不存在时，才会做相关处理
//先判断key为20的之元素是否存在，不存在，则添加
maps.computeIfAbsent(20,key-> "val" + key);
System.out.println(maps.containsKey(20));
//先判断key为20的之元素是否存在，存在，则不做任何处理
maps.computeIfAbsent(3,key -> "newVal");
System.out.println(maps.get(3));
```

### remove(key,value):

只有当key和value都存在且能匹配上时，才能删除该元素。

```java
System.out.println(maps.get(7));
maps.remove(7,"val");
System.out.println(maps.get(7));
maps.remove(7,"val7");
System.out.println(maps.containsKey(7));
//val7
//val7
//false
```

### getOrDefault(key,defaultValue):

根据key获取值，如果存在则返回集合元素的值，如果不存在则返回该defaultValue;

```java
String existValue = maps.getOrDefault(8, "defaultValue");
System.out.println(existValue);
String defaultValue = maps.getOrDefault(12, "defaultValue");
System.out.println(defaultValue);
//val8
//defaultValue
```

### merge:

```java
//concat是String类的拼接操作
//merge方法，会先判断进行合并的key是否存在，不存在，怎会添加元素
maps.merge(12,"val12",(value,newValue)->value.concat(newValue));
System.out.println(maps.get(12));
//merge方法，会先判断进行合并的key是否存在，存在，怎会拼接新旧元素
maps.merge(12,"concat",(value,newValue)->value.concat(newValue));
System.out.println(maps.get(12));
```



## Date/Time  API

Java 8通过发布新的Date-Time API (JSR 310)来进一步加强对日期与时间的处理。对日期和时间的操作一直都是Java程序员最痛苦的地方之一，标准的 java.tank.util.Date以及后来的java.tank.util.Calendar一点没有改善这种情况(可以这么说,它们一定程度上更加复杂)。

这种情况直接导致了Joda-Time——一个可替换标准日期/时间处理且功能非常强大的Java API的诞生。Java 8新
的Date-Time API (JSR 310)在很大程度上受到Joda-Time的影响,并且吸取了其精髓。

### LocalDate类

LocaleDate只支持有ISO-8601格式且无时区信息的日期部分：

```java
    //当前日期
        LocalDate date = LocalDate.now();
        //日期增加一天
        date = date.plusDays(1);
        //日期增加一个月
        date = date.plusMonths(1);
        //日期减少一天
        date = date.minusDays(1);
        //日期减少一个月
        date = date.minusMonths(1);
        //日期减少一年
        date = date.minusYears(1);
        System.out.println(date);
```

### LocalTime类

LocalTime只支持ISO-8601格式且无时区信息的时间部分

```java
      //当期时间
        LocalTime time = LocalTime.now();
        //日期增减一分钟
        time = time.plusMinutes(1);
        //时间增加一小时
        time = time.plusHours(1);
        //时间增加一秒
        time = time.plusSeconds(1);
        //时间增加一纳秒
        time = time.plusNanos(100);
        System.out.println(time);
```

### LocalDateTime类和格式化

LocalDateTime把LocaleDate与LocaleTime的功能结合起来，它持有的是ISO-8601格式无时区信息的日期与时间。

```java
   LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
```

### ZonedDateTime类

如果你需要特定时区的日期/时间,那么ZonedDateTime是你的选择。它持有ISO-8601格式具具有时区信息的日
期与时间。

```java
ZonedDateTime shanghai  = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime usa = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        System.out.println(shanghai);
        System.out.println(usa);
```

### Clock类

它通过指定一个时区,然后就可以获取到当前的时刻,日期与时间。Clock可以替换System.currentTimeMillis()与
TimeZone.getDefault()。

```java
 //协调世界时间，又称世界标准时间
        Clock utc = Clock.systemUTC();
        Clock beijing = Clock.system(ZoneId.of("Asia/Tokyo"));
        System.out.println(LocalDateTime.now(utc));
        System.out.println(LocalDateTime.now(beijing));
```

### Duration类

Duration使计算两个日期间的不同变的十分简单

```java
   LocalDateTime from = LocalDateTime.parse("2020-04-29 18:50:50", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime to = LocalDateTime.parse("2020-05-29 18:50:50", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Duration duration = Duration.between(from, to);
        System.out.println("Duration in days :" + duration.toDays());
        System.out.println("Duration in hours :" + duration.toHours());
```

## 其他特性

### 重复注解

假设,现在有一个服务我们需要定时运行,就像Linux中的cron一样,假设我们需要它在每周三的12点运行一
次,那我们可能会定义一个注解,有两个代表时间的属性。

```java
public @interface RepeatAnnotation {
    int dayOfWeek() default 1; //周几？
    int hour() default 0;   //几点？
}
```

所以我们可以给对应的服务方法上使用该注解,代表运行的时间:

```java
public class ScheduleService {
    @MySchedule(dayOfWeek = 3,hour = 12)
    public void start(){
        System.out.println("定时运行任务！");
    }
}
```

那么如果我们需要这个服务在每周四的13点也需要运行一下,如果是JDK8之前,那么...尴尬了!你不能像下面的
代码,会编译错误

```java
public class ScheduleService {
    @MySchedule(dayOfWeek = 3,hour = 12)
    @MySchedule(dayOfWeek = 4,hour = 13)
    public void start(){
        System.out.println("定时运行任务！");
    }
}
```

那么如果是JDK8,你可以改一下注解的代码,在自定义注解上加上@Repeatable元注解,并且指定重复注解的存
储注解(其实就是需要需要数组来存储重复注解),这样就可以解决上面的编译报错问题。

```java
@Repeatable(value = MySchedule.MySchedules.class)
public @interface MySchedule {
    int dayOfWeek() default 1; //周几？
    int hour() default 0;   //几点？

    @interface Schedules{
        MySchedule[] value();
    }
}
```

同时,反射相关的API提供了新的函数getAnnotationsByType()来返回重复注解的类型。
添加main方法:

```java
public static void main(String[] args) {
    try {
        Method method = ScheduleService.class.getMethod("start");
        for(Annotation annotation : method.getAnnotations()){
            System.out.println(annotation);
        }
        //MySchedule[] annotationsByType = method.getAnnotationsByType(MySchedule.class);
        for(MySchedule s : method.getAnnotationsByType(MySchedule.class)){
            System.out.println(s.dayOfWeek() + "->" + s.hour());
        }
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    }
}
```

**找不到注解！！！！！！未找到原因！！！！**

### 扩展注解

注解就相当于一种标记，在程序中加了注解就等于为程序加了某种标记。

JDK 8 之前的注解只能加在：

- 1、类，接口，枚举
- 2、类型变量
- 3、方法
- 4、方法参数
- 5、构造方法
- 6、局部变量
- 7、注解类型
- 8、包

JDK 11的注解类型：

```java
public enum ElementType {
    /** Class, interface (including annotation type), or enum declaration */
    TYPE,

    /** Field declaration (includes enum constants) */
    FIELD,

    /** Method declaration */
    METHOD,

    /** Formal parameter declaration */
    PARAMETER,

    /** Constructor declaration */
    CONSTRUCTOR,

    /** Local variable declaration */
    LOCAL_VARIABLE,

    /** Annotation type declaration */
    ANNOTATION_TYPE,

    /** Package declaration */
    PACKAGE,

    /**
     * Type parameter declaration
     *
     * @since 1.8
     */
    TYPE_PARAMETER,

    /**
     * Use of a type
     *
     * @since 1.8
     */
    TYPE_USE,

    /**
     * Module declaration.
     *
     * @since 9
     */
    MODULE
}
```



JDK8中新增了两种：

1、TYPE_PARAMNETER，表示该注解能写在类型变量的声明语句中。

2、TYPE_USER，表示该注解能写在使用类型的任何语句中

JDK9中新增了一种：

- MODULE

checkerframework中的各种校验注解,比如:@Nullable, @NonNull等等。

```java
public class GetStarted {
		void sample() {
			@NonNull Object ref = null;
	}
}
```



### 更好的类型推测机制

```java
public class Value<T> {
    public static<T> T defaultValue() {
        return null;
    }
    public T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
    public static void main(String[] args) {
        Value<String> value = new Value<>();
        System.out.println(value.getOrDefault("22", Value.defaultValue()));
    }
}
```

上面的代码重点关注value.getOrDefault("22", Value.defaultValue()), 在JDK8中不会报错,那么在JDK7中
呢?

答案是会报错: Wrong 2nd argument type. Found: 'java.lang.Object', required:
'java.lang.String' 。所以Value.defaultValue()的参数类型在JDK8中可以被推测出,所以就不必明确给
出。

### 参数名字保留在字节码中

先来想一个问题:JDK8之前,怎么获取一个方法的参数名列表?

在JDK7中一个Method对象有下列方法:

- `Method.getParameterAnnotations()`获取方法参数上的注解
- `Method.getParameterTypes()` 获取方法的参数类型列表

但是没有能够获取到方法的参数名字列表！

在JDK8中增加了两个方法：

- `Method.getParameters()` 获取参数名字列表
- `Method.getParameterCount()` 获取参数名字个数

### StampedLock

是对读写锁ReentrantReadWriteLock的增强，该类提供了一些功能，优化了读锁，写锁的访问，同时使读写索之间可以相互转换，更细粒度控制并发。

### ReentrantLock

ReentrantLock类，实现了Lock接口，是一种可重入的独占锁，它具有与使用synchronized相同的一些基本行为和语义，但功能更脚矮强大，ReentrantLock内部通过内部类实现了AQS框架(AbstractQueuedSynchronizer)的API来
实现独占锁的功能。

### ReentrantReadWriteLock

ReentrantReadWriteLock和ReentrantLock不同,ReentrantReadWriteLock实现的是ReadWriteLock接口。

```
读写锁的概念:加读锁时其他线程可以进行读操作但不可进行写操作,加写锁时其他线程读写操作都不可进行。
```

但是,读写锁如果使用不当,很容易产生饥饿”问题。在ReentrantReadWriteLock中,当读锁被使用时,如
果有线程尝试获取写锁,该写线程会阻塞。在读线程非常多,写线程很少的情况下,很容易导致写线程“饥
饿”,虽然使用“公平”策略可以一定程度上缓解这个问题,但是“公平”策略是以牺牲系统吞吐量为代价的。

### 并行数组

Java 8增加了大量的新方法来对数组进行并行处理。可以说,最重要的是parallelSort()方法,因为它可以在多核
机器上极大提高数组排序的速度。下面的例子展示了新方法(parallelXxx)的使用。
下面的代码演示了先并行随机生成20000个0-1000000的数字,然后打印前10个数字,然后使用并行排序,再次
打印前10个数字。

```java
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
```

注意：只有在大数据量的情况下并行排序的执行时间是串行排序的执行时间n倍。

### 获取方法参数的名字

在Java8之前,我们如果想获取方法参数的名字是非常困难的,需要使用ASM、javassist等技术来实现,现在,
在Java8中则可以直接在Method对象中就可以获取了。

```java
public class ParameterNames
{
   public void test(String str1 ,String str2){

    }

    public static void main(String[] args) {
        try {
            Method method = ParameterNames.class.getMethod("test",String.class,String.class);
            for (Parameter parameter : method.getParameters()) {
                System.out.println(parameter.getName());
            }
            System.out.println(method.getParameterCount());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
```

输出:

```
arg0
arg1
2
```

从结果可以看出输出的参数个数正确,但是名字不正确!需要在编译时增加–parameters参数后再运行。

```maven
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-compiler-plugin</artifactId>
<version>3.1</version>
<configuration>
<compilerArgument>-parameters</compilerArgument>
<source>1.8</source>
<target>1.8</target>
</configuration>
</plugin>
```



### CompletableFuture

当我们Javer说异步调用时,我们自然会想到Future,比如:

```java
public class FutureDemo {
/**
*
* @param args
*/
public static void main(String[] args) {
ExecutorService executor = Executors.newCachedThreadPool();
Future<Integer> result = executor.submit(new Callable<Integer>() {
public Integer call() throws Exception {
int sum=0;
System.out.println("
...");
for (int i=0; i<100; i++) {
sum = sum + i;
}
Thread.sleep(TimeUnit.SECONDS.toSeconds(3));
System.out.println("
");
return sum;
}
});
System.out.println("
...");
try {
System.out.println("result:" + result.get());
} catch (Exception e) {
e.printStackTrace();
}
System.out.println("
executor.shutdown();
}
}
```



那么现在如果想实现异步计算完成之后,立马能拿到这个结果继续异步做其他事情呢?这个问题就是一个线程依
赖另外一个线程,这个时候Future就不方便,我们来看一下CompletableFuture的实现:

```java
  public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture result = CompletableFuture.supplyAsync(() -> {
            int sum=0;
            System.out.println("...");
            for (int i=0; i<100; i++) {
                sum = sum + i;
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toSeconds(3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" 计算完毕");
            return sum;
        }, executor).thenApplyAsync(sum -> {
            System.out.println(Thread.currentThread().getName()+"打印"+sum);
            return sum;
        }, executor);
        System.out.println("...");
        try {
            System.out.println("result:" + result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("...");
    }
```

只需要简单的使用thenApplyAsync就可以实现了。

CompletableFuture还有很多其他的特性，需要慢慢来学。

```
Java8的特性还有Stream和Optional,这两个也是用的特别多的,相信很多同学早有耳闻,并且已经对这两个的特性有所了解,所以本片博客就不进行讲解了,有机会再单独讲解,可讲的内容还是非常之多的对于Java8,新增的特性还是非常之多的,就是目前Java11已经出了,但是Java8中的特性肯定会一直在后续的版本中保留的,至于这篇文章的这些新特性我们估计用的比较少,所以特已此篇来进行一个普及,希望都有所收货。
```



### Java虚拟机（JVM）的新特性 

PermGen空间被移除了,取而代之的是Metaspace(JEP 122)。JVM选项-XX:PermSize与-XX:MaxPermSize分
别被-XX:MetaSpaceSize与-XX:MaxMetaspaceSize所代替。