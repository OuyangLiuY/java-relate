## 1、表架构呈现

- ### student(sid,sname,sage,ssex) 学生表 

- ### teacher(tid,tname) 教师表

- ### course(cid,cname,tid) 课程表 

- ### sc(sid,cid,score) 成绩表 

## 2、SQL

```sql
-- 1.学生表
CREATE TABLE student(
sid INT PRIMARY KEY AUTO_INCREMENT,
sname VARCHAR(20),
sage DATE,
ssex ENUM ('男','女')
);

-- 2.课程表中使用了外键教师编号，因而需要先建立教师表
CREATE TABLE teacher(
tid INT PRIMARY KEY AUTO_INCREMENT,
tname VARCHAR(20)
);

-- 3.建立课程表
CREATE TABLE course(
cid INT PRIMARY KEY AUTO_INCREMENT,
cname VARCHAR(20),
tid INT,
FOREIGN KEY (tid) REFERENCES teacher (tid)
);

-- 4.建立成绩表
CREATE TABLE sc(
sid INT,
cid INT,
score INT
);

-- 先给student表插入数据
INSERT INTO student VALUES (1,'赵雷','1990-01-01','男'),
	(2,'钱电','1990-12-21','男'),
	(3,'孙风','1990-05-20','男'),
	(4,'李云','1990-08-06','男'),
	(5,'周梅','1991-12-01','女'),
	(6,'吴兰','1992-03-01','女'),
	(7,'郑竹','1989-07-01','女'),
	(8,'王菊','1990-01-20','女');
	
-- 给teacher表插入数据,这里不可以先给course表插入数据,因为course表外键连接到teacher的主键
INSERT INTO teacher VALUES(1,'张三'),(2,'李四'),(3,'王五');
		
--  给course表插入数据
INSERT INTO course VALUES(1,'语文',2),(2,'数学',1),(3,'英语',3);

-- 最后给sc表插入数据
INSERT INTO sc VALUES(1,1,90),(1,2,80),(1,3,90),(2,1,70),(2,2,60),(2,3,80),(3,1,80),
(3,2,80),(3,3,80),(4,1,50),(4,2,30),(4,3,20),(5,1,76),(5,2,87),(6,1,31),(6,3,34),(7,2,89),(7,3,98);
```

## 3、问题集锦

### 3.1、查询  课程1比  课程 2 成绩高的学生的信息及课程分数

```sql
select s.*,sc1.score,sc2.score  from student s, sc sc1,sc sc2 where sc1.cid = 1 and sc2.cid = 2 and sc1.score > sc2.score and sc1.sid = s.sid and sc2.sid = s.sid;
```

### 3.2、查询平均成绩大于等于60分的同学的学生编号和学生姓名和平均成绩

```
```



SELECT s.sid,s.sname,AVG(sc1.`score`) AS 'avg_score' FROM student s ,sc sc1 WHERE s.sid=sc1.`sid`
GROUP BY s.sid HAVING avg_score>=60 ORDER BY avg_score DESC;
（3）查询名字中含有"风"字的学生信息

SELECT s.sid,s.sname,s.sage,s.ssex FROM student s WHERE sname LIKE '%风%';
（4）查询课程名称为"数学"，且分数低于60的学生姓名和分数

SELECT s.sname,sc1.score FROM student s ,sc sc1 WHERE s.sid=sc1.sid AND cid=2 AND sc1.score<60;
（5）查询所有学生的课程及分数情况

SELECT sc1.sid,c.cname,sc1.score FROM course c,sc sc1 WHERE c.cid=sc1.cid;
（6）查询没学过"张三"老师授课的同学的信息

第一种普通写法：

SELECT sc1.sid FROM sc sc1,course c,teacher t WHERE c.tid = t.tid AND c.cid = sc1.cid AND t.tname='张三';
第二种子查询写法：

SELECT s.* FROM student s WHERE s.sid NOT IN 
(SELECT sc1.sid FROM sc sc1,course c,teacher t WHERE c.tid = t.tid AND c.cid = sc1.cid AND t.tname='张三');
（7）查询学过"张三"老师授课的同学的信息

第一种普通写法：

SELECT s.* FROM student s,sc sc1,course c,teacher t WHERE 
sc1.sid=s.sid AND t.tid=c.tid AND c.cid=sc1.cid AND  t.tname='张三';
第二种子查询写法：

SELECT s.* FROM student s WHERE s.sid IN 
(SELECT sc1.sid FROM sc sc1,course c,teacher t WHERE c.tid = t.tid AND c.cid = sc1.cid AND t.tname='张三');
（8）查询学过编号为 1 并且也学过编号为 2 的课程的同学的信息

SELECT s.* FROM student s,sc sc1,sc sc2 WHERE s.sid = sc1.sid AND sc1.sid = sc2.sid AND sc1.cid = 1 AND sc2.cid =2;
（9）查询学过编号为 1 但是没有学过编号为 2 的课程的同学的信息

-- 第一步：先用student表左连接查出学过课程1的和学过课程2的所有学生信息
SELECT s.* FROM student s
LEFT JOIN (SELECT * FROM sc WHERE cid=1) sc1 ON s.sid = sc1.sid
LEFT JOIN (SELECT * FROM sc WHERE cid=2) sc2 ON s.sid = sc2.sid; 
-- 第二步：筛选学过编号为1但是没有学过编号为2的课程的同学的信息
SELECT s.* FROM student s
LEFT JOIN (SELECT * FROM sc WHERE cid=1) sc1 ON s.sid = sc1.sid
LEFT JOIN (SELECT * FROM sc WHERE cid=2) sc2 ON s.sid = sc2.sid
WHERE (sc1.cid=1 AND sc2.cid IS NULL);
（10）查询没有学全所有课程的同学的信息

-- 第一种写法：
-- 第一步：先用student表左连接查出学过课程1、2、3及其它的所有学生信息
SELECT s.* FROM student s
LEFT JOIN (SELECT * FROM sc WHERE cid=1) sc1 ON s.sid = sc1.sid
LEFT JOIN (SELECT * FROM sc WHERE cid=2) sc2 ON s.sid = sc2.sid
LEFT JOIN (SELECT * FROM sc WHERE cid=3) sc3 ON s.sid = sc3.sid;
-- 筛选没有学全所有课程的同学的信息
SELECT s.* FROM student s
LEFT JOIN (SELECT * FROM sc WHERE cid=1) sc1 ON s.sid = sc1.sid
LEFT JOIN (SELECT * FROM sc WHERE cid=2) sc2 ON s.sid = sc2.sid
LEFT JOIN (SELECT * FROM sc WHERE cid=3) sc3 ON s.sid = sc3.sid
WHERE (sc1.cid IS NULL OR sc2.cid IS NULL OR sc3.cid IS NULL);
-- 上面写法比较繁琐，建议用楼下写法
-- 第二种写法：
-- 第一步：先把三个课程都学的学员编号查出来
SELECT sc1.sid FROM sc sc1,sc sc2,sc sc3 
WHERE (sc1.cid=1 AND sc2.cid =2 AND sc3.cid =3 AND sc1.sid=sc2.sid AND sc1.sid = sc3.sid);
-- 第二步：对立的查询思路，三个课程都学完的同学对立面是三个课程没学完或者一个都没学的
SELECT s.* FROM student s WHERE s.sid NOT IN 
(SELECT sc1.sid FROM sc sc1,sc sc2,sc sc3 
WHERE (sc1.cid=1 AND sc2.cid =2 AND sc3.cid =3 AND sc1.sid=sc2.sid AND sc1.sid = sc3.sid))
GROUP BY s.sid;
（11）查询至少有一门课与学号为"1"的同学所学相同的同学的信息

SELECT DISTINCT s.* FROM student s,sc sc1 WHERE 
s.sid=sc1.sid AND sc1.cid IN(SELECT cid FROM sc WHERE sid=1) AND s.sid<> 1;  
（12）查询和"1"号的同学学习的课程完全相同的其他同学的信息

-- 第一步：先查出1号同学学习的所有的课程编号
-- 当前数据设计1号同学选修了所有课程
SELECT cid FROM sc WHERE sid=1;

-- 第二步：查出选修了1号学生没有选修课程的学生编号，这一步很关键，我们用cid去过滤，所以后面再用not in的时候那个子集里一定有和1号学生选修的课程完全相同的同学
-- 因为1号同学选修了所有课程，所以没有符合条件的学生编号
SELECT sid FROM sc WHERE cid NOT IN (SELECT cid FROM sc WHERE sid=1);
-- 如果查询结果中有重复的sid，因为有的同学选修的1号同学没选的课程不止一门，可以使用distinct对sid进行去重处理

-- 第三步：查询选修的课程是1号学生选修课程的子集的学生编号
-- 因为第二步查出的是选修了1号学生没有选修课程的学生编号,逆向思维，再用not in，双重否定变肯定，查出的就是和1号同学有一门、多门、或者全部课程的同学编号
-- 可以使用distinct对sid进行去重处理
SELECT sid FROM sc WHERE sid NOT IN (SELECT sid FROM sc WHERE cid NOT IN (SELECT cid FROM sc WHERE sid=1));

-- 第四步：从上述查询结果中，筛选出选修的课程数量与1号学生选修的课程数量相等的其他学生的编号
SELECT sid FROM sc WHERE sid NOT IN 
(SELECT sid FROM sc WHERE cid NOT IN (SELECT cid FROM sc WHERE sid=1)) 
GROUP BY sid 
HAVING COUNT(*) = (SELECT COUNT(*) FROM sc WHERE sid =1) AND sid <>1;

-- COUNT(*):统计返回的行数 当sid=1时，有3行数据
SELECT COUNT(*) FROM sc WHERE sid=1; -- 3

-- 第五步:以上述查询结果为筛选条件，从student表中查询出与1号学生学习的课程完全相同的其他学生的信息
SELECT s.* FROM student s  WHERE sid IN 
(SELECT sid FROM sc WHERE sid NOT IN 
(SELECT sid FROM sc WHERE cid NOT IN (SELECT cid FROM sc WHERE sid=1)) 
GROUP BY sid 
HAVING COUNT(*) = (SELECT COUNT(*) FROM sc WHERE sid =1) AND sid <>1);
/*第十二题思路总结：
01号之外的其他学生可以分成两个大类，一类是选修了01号学生没有选修的课程的学生
另一类学生选修的课程是01号学生选修的课程的子集，这个子集是选修了和1号学生里的一门、多门、
采用逆向思维，可以先找出选修了01号学生没选课程的学生编号，然后以01号学生选修的课程数量为筛选条件，
从剩下的选修的课程是01号学生选修的课程的子集这类学生中筛选出与01号学生所选课程完全相同的学生编号，
此编号包含了01，以剔除了01之后的编号为筛选条件
从student表中选出和01号同学学习的课程完全相同的其他同学的信息*/
（13）查询没学过"张三"老师讲授的任一门课程的学生信息

-- 第一步：张三老师tid是1，cid是2，逆向思维，查询学过"张三"老师讲授的任一门课程的学生编号
SELECT sc1.sid FROM sc sc1,course c,teacher t WHERE
t.tid = c.tid AND sc1.cid = c.cid AND t.tname='张三';
-- 第二步：以上查询结果为筛选条件，从student表中查询出没学过"张三"老师讲授的任一门课程的学生信息
SELECT s.* FROM student s WHERE s.sid NOT IN 
(SELECT sc1.sid FROM sc sc1,course c,teacher t WHERE
t.tid = c.tid AND sc1.cid = c.cid AND t.tname='张三');
（14）查询出只有两门课程的全部学生信息

SELECT s.* FROM student s,sc GROUP BY sc.sid HAVING COUNT(sc.sid)=2 AND s.sid=sc.sid;
（15）查询1990年出生的学生信息(注：student表中sage列的类型是datetime)

-- 第一种写法：
SELECT s.* FROM student s WHERE s.sage>='1990-01-01' AND s.sage<='1990-12-31';
-- 第二种写法：模糊查询
SELECT s.* FROM student s WHERE s.sage LIKE '1990-%';
（16）查询每门课程的平均成绩，结果按平均成绩降序排列，平均成绩相同时，按课程编号升序排列

SELECT sc.cid,AVG(score) AS avg_score FROM sc 
GROUP BY sc.cid ORDER BY avg_score DESC,sc.cid ASC;
（17）查询任何一门课程成绩在70分以上的姓名、课程名称和分数

SELECT s.sname,c.cname,sc.score FROM student s,course c,sc 
WHERE s.sid = sc.sid AND sc.cid = c.cid AND sc.score >=70;
（18）查询平均成绩大于等于85的所有学生的学号、姓名和平均成绩,并按照平均成绩降序排列

SELECT s.sid,s.sname,AVG(score) AS avg_score FROM student s,sc 
WHERE s.sid = sc.sid GROUP BY s.sid HAVING avg_score >=85 ORDER BY avg_score DESC;
（19）查询成绩不及格的课程和学生姓名

SELECT s.sname,c.cname,sc.score FROM student s,course c,sc 
WHERE s.sid=sc.sid AND sc.cid=c.cid AND sc.score<60;
（20）查询课程编号为1且课程成绩在80分以上的学生的学号和姓名

SELECT s.sid,s.sname FROM student s,sc WHERE 
s.sid=sc.sid AND sc.cid =1 AND sc.score>=80 GROUP BY s.`sid`;
（21）求每门课程的学生人数

SELECT cid AS '课程编号',COUNT(sid)AS '课程人数' FROM sc GROUP BY sc.cid;
（22）统计每门课程的学生选修人数（超过5人的课程才统计）。要求输出课程号和选修人数

SELECT cid AS '课程编号',COUNT(sid)AS '课程人数' FROM sc GROUP BY sc.cid HAVING COUNT(sid)>5 ORDER BY COUNT(sid),sc.cid ASC;
（23）查询不同课程成绩相同的学生的学生编号、课程编号、学生成绩

SELECT s1.sid,sc1.cid,sc1.score,s2.sid,sc2.cid,sc2.score FROM student s1,student s2,sc sc1,sc sc2 
WHERE s1.sid=sc1.sid AND s2.sid=sc2.sid AND sc1.cid <>sc2.cid AND sc1.score = sc2.score;
（24）检索至少选修两门课程的学生学号

SELECT sid FROM sc GROUP BY sc.sid HAVING COUNT(cid)>=2;
（25）查询选修了全部课程的学生信息

SELECT * FROM student s,sc WHERE s.sid =sc.sid GROUP BY s.sid HAVING COUNT(cid)=3;
（26）查询各学生的年龄(年龄保留整数)

SELECT s.sname,ROUND((TO_DAYS('2020-06-11')-TO_DAYS(s.sage))/365) AS age FROM student s;
（27）查询本月过生日的学生姓名和出生年月

-- _____ :五个下划线长度
SELECT s.sname,s.sage FROM student s WHERE s.sage LIKE '_____07%';
（28）查询下月过生日的学生

SELECT s.sname,s.sage FROM student s WHERE s.sage LIKE '_____08%';
（29）查询学全所有课程的同学的信息

SELECT s.* FROM student s,sc  WHERE 
s.sid=sc.sid GROUP BY s.sid HAVING COUNT(sc.cid)=3;


原文链接：https://blog.csdn.net/william_leilei/article/details/106766388