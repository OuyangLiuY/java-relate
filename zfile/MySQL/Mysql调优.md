Mysql ->

	1. 性能监控
 	2. schema与数据类型 优化
      	3. 执行计划
         	4. 通过索引进行优化

```shell
mysql> set global slow-query-log=on  #开启慢查询功能

mysql> set global slow_query_log_file='/var/log/mysql/mysql-slow.log';  #指定慢查询日志文件位置

mysql> set global log_queries_not_using_indexes=on;   #记录没有使用索引的查询

mysql> set global long_query_time=1;   #只记录处理时间1s以上的慢查询
```





# Schema与数据类型优化

## 数据类型的优化

### 更小的通常更好

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Test {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1","root","123456");
        PreparedStatement pstmt = conn.prepareStatement("insert into psn2 values(?,?)");
        for (int i = 0; i < 20000; i++) {
            pstmt.setInt(1,i);
            pstmt.setString(2,i+"");
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        conn.close();
    }
}
```

意思就是同样的数据，如果使用数据类型较小的字符类型，所占磁盘空间更小。

### 简单就好

简单数据类型的操作通常需要更少的CPU周期，例如：

1. 整型比字符操作代价更低，因为字符集和校对规则是字符比较比整型比较更复杂，
2. 使用mysql自建类型而不是字符串来存储日期和时间
3. 用整型存储IP地址



### 尽量避免null

如果查询中包含可为NULL的列，对mysql来说很难优化，因为可为null的列使得索引、索引统计和值比较都更加复杂，坦白来说，通常情况下null的列改为not null带来的性能提升比较小，所有没有必要将所有的表的schema进行修改，但是应该尽量避免设计成可为null的列

### 实际细则

#### 整数类型：

可以使用的几种整数类型：TINYINT，SMALLINT，MEDIUMINT，INT，BIGINT分别使用8，16，24，32，64位存储空间。

尽量使用满足需求的最小数据类型。

#### 字符和字符串类型：

**varchar根据实际内容长度保存数据：**

1. 使用最小的符合需求的长度。

2. varchar(n) n小于等于255使用额外一个字节保存长度，n>255使用额外两个字节保存长度。

3. varchar(5)与varchar(255)保存同样的内容，硬盘存储空间相同，但内存空间占用不同，是指定的大小 。

4. varchar在mysql5.6之前变更长度，或者从255一下变更到255以上时时，都会导致锁表。

5. 使用场景：

   - 1、存储长度波动较大的数据，如：文章，有的会很短有的会很长

   - 2、字符串很少更新的场景，每次更新后都会重算并使用额外存储空间保存长度
   - 3、适合保存多字节字符，如：汉字，特殊字符等

mysql 4.0版本以下，varchar(50), 指的是50字节，如果存放utf8汉字时，只能存放16个（每个汉字3字节）。

mysql 5.0版本以上，varchar(50), 指的是50字符，无论存放的是数字、字母还是UTF8汉字（每个汉字3字节），都可以存放50个。

**char固定长度的字符串:**

- 1、最大长度：255
- 2、会自动删除末尾的空格
- 3、检索效率、写效率 会比varchar高，以空间换时间
- 4、使用场景：
  - 1、存储长度波动不大的数据，如：md5摘要
  - 2、存储短字符串、经常更新的字符串

#### BLOB和TEXT类型：

MySQL 把每个 BLOB 和 TEXT 值当作一个独立的对象处理。
两者都是为了存储很大数据而设计的字符串类型，分别采用二进制和字符方式存储。

#### datetime和timestamp：

**datetime:**

- 占用8个字节
- 与时区无关，数据库底层时区配置，对datetime无效
- 可保存到毫秒
- 可保存时间范围大
- 不要使用字符串存储日期类型，占用空间大，损失日期类型函数的便捷性

**timestamp:**

- 占用4个字节
- 时间范围：1970-01-01到2038-01-19
- 精确到秒
- 采用整形存储
- 依赖数据库设置的时区
- 自动更新timestamp列的值

**date:**

- 占用的字节数比使用字符串、datetime、int存储要少，使用date类型只需要3个字节
- 使用date类型还可以利用日期时间函数进行日期之间的计算
- date类型用于保存1000-01-01到9999-12-31之间的日期

#### 使用枚举代替字符串类型

有时可以使用枚举类代替常用的字符串类型，mysql存储枚举类型会非常紧凑，会根据列表值的数据压缩到一个或两个字节中，mysql在内部会将每个值在列表中的位置保存为整数，并且在表的.frm文件中保存“数字-字符串”映射关系的查找表

 ```
create table enum_test(e enum('fish','apple','dog') not null);
insert into enum_test(e) values('fish'),('dog'),('apple');
select e+0 from enum_test;
 ```

#### 特殊类型数据

人们经常使用varchar(15)来存储ip地址，然而，它的本质是32位无符号整数不是字符串，可以使用INET_ATON()和INET_NTOA函数在这两种表示方法之间转换
案例：

```
select inet_aton('1.1.1.1');
select inet_ntoa(16843009);
```

### 合理使用范式和反范式

三范式是解决数据冗余

第一范式：列不可在分

第二范式：表示不能存在传递依赖

第三范式：表中内容必须唯一的依赖于主键

- **范式**

  - 优点：
    - 范式化的更新通常比反范式要快
    - 当数据较好的范式化后，很少或者没有重复的数据
    - 范式化的数据比较小，可以放在内存中，操作比较快
  - 缺点：通常需要进行关联

- **反范式**

  - 优点：
    - 所有的数据都在同一张表中，可以避免关联
    - 可以设计有效的索引；
  - 缺点：表格内的冗余较多，删除数据时候会造成表有些有用的信息丢失

- **注意**

  - 在企业中很好能做到严格意义上的范式或者反范式，一般需要混合使用：

    - 在一个网站实例中，这个网站，允许用户发送消息，并且一些用户是付费用户。现在想查看付费用户最近的10条信息。  在user表和message表中都存储用户类型(account_type)而不用完全的反范式化。这避免了完全反范式化的插入和删除问题，因为即使没有消息的时候也绝不会丢失用户的信息。这样也不会把user_message表搞得太大，有利于高效地获取数据。
    - 另一个从父表冗余一些数据到子表的理由是排序的需要。
    - 缓存衍生值也是有用的。如果需要显示每个用户发了多少消息（类似论坛的），可以每次执行一个昂贵的自查询来计算并显示它；也可以在user表中建一个num_messages列，每当用户发新消息时更新这个值。

  - 范式设计：

      ![范式设计](../../images/mysql/范式设计.png)

  - 反范式设计：

    ![反范式设计](../../images/mysql/反范式设计.png)

## 合理使用范式和反范式

## 主键的选择

- 代理主键：

  与业务无关，无意义的数字序列

- 自然主键

  事物属性中的自然唯一标识

- 推荐使用代理主键

  - 他们不与业务耦合，因此更容易维护
  - 一个大多数，最好是全部表，通用的键策略能够减少需要编写的源码数量，减少系统的总体拥有成本



## 字符集的选择

- 1.纯拉丁字符能表示的内容，没必要选择 latin1 之外的其他字符编码，因为这会节省大量的存储空间。
- 2.如果我们可以确定不需要存放多种语言，就没必要非得使用UTF8或者其他UNICODE字符类型，这回造成大量的存储空间浪费。
- 3.MySQL的数据类型可以精确到字段，所以当我们需要大型数据库中存放多字节数据的时候，可以通过对不同表不同字段使用不同的数据类型来较大程度减小数据存储量，进而降低 IO 操作次数并提高缓存命中率。

## 存储引擎的选择

**存储引擎的对比**

![存储引擎的对比](../../images/mysql/存储引擎的对比.png)

## 适当的数据冗余

- 1.被频繁引用且只能通过 Join 2张(或者更多)大表的方式才能得到的独立小字段。
- 2.这样的场景由于每次Join仅仅只是为了取得某个小字段的值，Join到的记录又大，会造成大量不必要的 IO，完全可以通过空间换取时间的方式来优化。不过，冗余的同时需要确保数据的一致性不会遭到破坏，确保更新的同时冗余字段也被更新。

## 适当拆分

当我们的表中存在类似于 TEXT 或者是很大的 VARCHAR类型的大字段的时候，如果我们大部分访问这张表的时候都不需要这个字段，我们就该义无反顾的将其拆分到另外的独立表中，以减少常用数据所占用的存储空间。这样做的一个明显好处就是每个数据块中可以存储的数据条数可以大大增加，既减少物理 IO 次数，也能大大提高内存中的缓存命中率。

# 执行计划

在企业的应用场景中，为了知道优化SQL语句的执行，需要查看SQL语句的具体执行过程，以加快SQL语句的执行效率。

​       可以使用explain+SQL语句来模拟优化器执行SQL查询语句，从而知道mysql是如何处理sql语句的。

​	   官网地址： https://dev.mysql.com/doc/refman/5.5/en/explain-output.html 

### 1、执行计划中包含的信息

|    Column     |                    Meaning                     |
| :-----------: | :--------------------------------------------: |
|      id       |            The `SELECT` identifier             |
|  select_type  |               The `SELECT` type                |
|     table     |          The table for the output row          |
|  partitions   |            The matching partitions             |
|     type      |                 The join type                  |
| possible_keys |         The possible indexes to choose         |
|      key      |           The index actually chosen            |
|    key_len    |          The length of the chosen key          |
|      ref      |       The columns compared to the index        |
|     rows      |        Estimate of rows to be examined         |
|   filtered    | Percentage of rows filtered by table condition |
|     extra     |             Additional information             |

**id**

select查询的序列号，包含一组数字，表示查询中执行select子句或者操作表的顺序

id号分为三种情况：

​	1、如果id相同，那么执行顺序从上到下

```sql
explain select * from emp e join dept d on e.deptno = d.deptno join salgrade sg on e.sal between sg.losal and sg.hisal;
```

​	2、如果id不同，如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行

```sql
explain select * from emp e where e.deptno in (select d.deptno from dept d where d.dname = 'SALES');
```

​	3、id相同和不同的，同时存在：相同的可以认为是一组，从上往下顺序执行，在所有组中，id值越大，优先级越高，越先执行

```sql
explain select * from emp e join dept d on e.deptno = d.deptno join salgrade sg on e.sal between sg.losal and sg.hisal where e.deptno in (select d.deptno from dept d where d.dname = 'SALES');
```

**select_type**

主要用来分辨查询的类型，是普通查询还是联合查询还是子查询

| `select_type` Value  |                           Meaning                            |
| :------------------: | :----------------------------------------------------------: |
|        SIMPLE        |        Simple SELECT (not using UNION or subqueries)         |
|       PRIMARY        |                       Outermost SELECT                       |
|        UNION         |         Second or later SELECT statement in a UNION          |
|   DEPENDENT UNION    | Second or later SELECT statement in a UNION, dependent on outer query |
|     UNION RESULT     |                      Result of a UNION.                      |
|       SUBQUERY       |                   First SELECT in subquery                   |
|  DEPENDENT SUBQUERY  |      First SELECT in subquery, dependent on outer query      |
|       DERIVED        |                        Derived table                         |
| UNCACHEABLE SUBQUERY | A subquery for which the result cannot be cached and must be re-evaluated for each row of the outer query |
|  UNCACHEABLE UNION   | The second or later select in a UNION that belongs to an uncacheable subquery (see UNCACHEABLE SUBQUERY) |

```sql
--sample:简单的查询，不包含子查询和union
explain select * from emp;

--primary:查询中若包含任何复杂的子查询，最外层查询则被标记为Primary
explain select staname,ename supname from (select ename staname,mgr from emp) t join emp on t.mgr=emp.empno ;

--union:若第二个select出现在union之后，则被标记为union
explain select * from emp where deptno = 10 union select * from emp where sal >2000;

--dependent union:跟union类似，此处的depentent表示union或union all联合而成的结果会受外部表影响
explain select * from emp e where e.empno  in ( select empno from emp where deptno = 10 union select empno from emp where sal >2000)

--union result:从union表获取结果的select
explain select * from emp where deptno = 10 union select * from emp where sal >2000;

--subquery:在select或者where列表中包含子查询
explain select * from emp where sal > (select avg(sal) from emp) ;

--dependent subquery:subquery的子查询要受到外部表查询的影响
explain select * from emp e where e.deptno in (select distinct deptno from dept);

--DERIVED: from子句中出现的子查询，也叫做派生类，
explain select staname,ename supname from (select ename staname,mgr from emp) t join emp on t.mgr=emp.empno ;

--UNCACHEABLE SUBQUERY：表示使用子查询的结果不能被缓存
 explain select * from emp where empno = (select empno from emp where deptno=@@sort_buffer_size);
 
--uncacheable union:表示union的查询结果不能被缓存：sql语句未验证
```

**table**

对应行正在访问哪一个表，表名或者别名，可能是临时表或者union合并结果集
		1、如果是具体的表名，则表明从实际的物理表中获取数据，当然也可以是表的别名

​		2、表名是derivedN的形式，表示使用了id为N的查询产生的衍生表

​		3、当有union result的时候，表名是union n1,n2等的形式，n1,n2表示参与union的id

**type**

type显示的是访问类型，访问类型表示我是以何种方式去访问我们的数据，最容易想的是全表扫描，直接暴力的遍历一张表去寻找需要的数据，效率非常低下，访问的类型有很多，效率从最好到最坏依次是：

system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL 

一般情况下，得保证查询至少达到range级别，最好能达到ref

```sql
--all:全表扫描，一般情况下出现这样的sql语句而且数据量比较大的话那么就需要进行优化。
explain select * from emp;

--index：全索引扫描这个比all的效率要好，主要有两种情况，一种是当前的查询时覆盖索引，即我们需要的数据在索引中就可以索取，或者是使用了索引进行排序，这样就避免数据的重排序
explain  select empno from emp;

--range：表示利用索引查询的时候限制了范围，在指定范围内进行查询，这样避免了index的全索引扫描，适用的操作符： =, <>, >, >=, <, <=, IS NULL, BETWEEN, LIKE, or IN() 
explain select * from emp where empno between 7000 and 7500;

--index_subquery：利用索引来关联子查询，不再扫描全表
explain select * from emp where emp.job in (select job from t_job);

--unique_subquery:该连接类型类似与index_subquery,使用的是唯一索引
 explain select * from emp e where e.deptno in (select distinct deptno from dept);
 
--index_merge：在查询过程中需要多个索引组合使用，没有模拟出来

--ref_or_null：对于某个字段即需要关联条件，也需要null值的情况下，查询优化器会选择这种访问方式
explain select * from emp e where  e.mgr is null or e.mgr=7369;

--ref：使用了非唯一性索引进行数据的查找
 create index idx_3 on emp(deptno);
 explain select * from emp e,dept d where e.deptno =d.deptno;

--eq_ref ：使用唯一性索引进行数据查找
explain select * from emp,emp2 where emp.empno = emp2.empno;

--const：这个表至多有一个匹配行，
explain select * from emp where empno = 7369;
 
--system：表只有一行记录（等于系统表），这是const类型的特例，平时不会出现
```

 **possible_keys** 

 显示可能应用在这张表中的索引，一个或多个，查询涉及到的字段上若存在索引，则该索引将被列出，但不一定被查询实际使用

```sql
explain select * from emp,dept where emp.deptno = dept.deptno and emp.deptno = 10;
```

**key**

​	实际使用的索引，如果为null，则没有使用索引，查询中若使用了覆盖索引，则该索引和查询的select字段重叠。

```sql
explain select * from emp,dept where emp.deptno = dept.deptno and emp.deptno = 10;
```

**ref**

显示索引的哪一列被使用了，如果可能的话，是一个常数

```sql
explain select * from emp,dept where emp.deptno = dept.deptno and emp.deptno = 10;
```

**rows**

根据表的统计信息及索引使用情况，大致估算出找出所需记录需要读取的行数，此参数很重要，直接反应的sql找了多少数据，在完成目的的情况下越少越好

```sql
explain select * from emp;
```

**extra**

包含额外的信息。

```sql
--using filesort:说明mysql无法利用索引进行排序，只能利用排序算法进行排序，会消耗额外的位置
explain select * from emp order by sal;

--using temporary:建立临时表来保存中间结果，查询完成之后把临时表删除
explain select ename,count(*) from emp where deptno = 10 group by ename;

--using index:这个表示当前的查询时覆盖索引的，直接从索引中读取数据，而不用访问数据表。如果同时出现using where 表名索引被用来执行索引键值的查找，如果没有，表面索引被用来读取数据，而不是真的查找
explain select deptno,count(*) from emp group by deptno limit 10;

--using where:使用where进行条件过滤
explain select * from t_user where id = 1;

--using join buffer:使用连接缓存，情况没有模拟出来

--impossible where：where语句的结果总是false
explain select * from emp where empno = 7469;
```

