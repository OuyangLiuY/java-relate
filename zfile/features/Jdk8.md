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

在JDK8中也增加了很多函数式接口,比如java.util.function包。

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

### 什么是Stream

### Stream的特点

### Stream构成

### 生成Stream Source的方式：

### Stream的操作类型

### Stream的使用

### 构造流的几种常见方式：

### 数值流的构造

### 流转换为其他数据结构

### 流的典型用法

### map/flatMap

### filter

### forEach

### reduce

### limit/skip

### sorted

### min/max/distinct

### Match

### 用Collectors来进行reduction操作

### groupingBy/PartitioningBy

### parallelStream

### parallelStream使用

### parallelStream要注意的问题

### Stream总结

## Date/Time  API

Java 8通过发布新的Date-Time API (JSR 310)来进一步加强对日期与时间的处理。对日期和时间的操作一直都是Java程序员最痛苦的地方之一，标准的 java.util.Date以及后来的java.util.Calendar一点没有改善这种情况(可以这么说,它们一定程度上更加复杂)。

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

### 扩展注解

### 更好的类型推测机制

### 参数名字保留在字节码中

### StampedLock

### ReentrantLock

### ReentrantReadWriteLock

### 并行数组

### 获取方法参数的名字

### CompletableFuture

### Java虚拟机（JVM）的新特性 