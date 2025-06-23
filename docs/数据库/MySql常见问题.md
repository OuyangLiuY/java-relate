# MySql 相关问题

## Mysql 数据库系统CPU飘升到100%

1、查看具体是谁占用了CPU

```bash
top -Hp $(pidof mysqld)
```

找到时哪个线程占用了CPU，

记录占用CPU高的线程号。

2、线程号转化为十六进制

```bash
printf "%x\n" 12345
```

## 找出cpu对应的SQL

1、开启performance_schema 并使用如下SQL：

```sql
SELECT
THREAD_ID, EVENT_ID, SQL_TEXT, TIMER_WAIT
FROM
performance_schema.events_statements_history
ORDER BY
TIMER_WAIT DESC
LIMIT 10;
```

或者根据线程id查找：

```SQL
SELECT
t.PROCESSLIST_ID, t.THREAD_OS_ID, es.SQL_TEXT
FROM
performance_schema.threads t
JOIN
performance_schema.events_statements_current es
ON
t.THREAD_ID = es.THREAD_ID
WHERE
t.THREAD_OS_ID = '十六进制线程号';
```

## 定位慢SQL

开启慢日志查询：

```
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1; -- 或更小
```

## 排查可能的问题

| 问题类型       | 排查方法                                         |
| -------------- | ------------------------------------------------ |
| SQL未加索引    | Explian 分析语句，重点关注type=ALL和rows特别大的 | 
| 频繁的全表扫描 | 检查表结构、索引情况，是否能加覆盖索引           | 
|热点数据的争用|是否多线程频繁操作同一行数据（可查information_schema.innodb_trx）|
|不合理的join|多表联查，未建索引或者顺序不当|
|高并发读写冲突|查看SHOW PROCESSLIST 中是否有大量Waiting for...状态|

## Mysql 参数优化

| 参数 | 建议 |
| --- | --- | 
| query_cache_size | 建议禁用，mysql8.0默认关闭 |
| innodb_buffer_pool_size | 设置为物理内存的60%～70% |
|max_connections|不要设置太大|
|thread_cache_size|设置适中，如100|

## 解决方案建议

- 优化高频SQL，必要时加缓存（如Redis）
- 添加或重建索引，避免全表扫描
- 控制并发连接数
- 查询优化：尽量避免子查询，OR、LIKE、‘%xxx%’等
- 若业务逻辑复杂，可拆分成多个子服务减压。

## 一健排查脚本

```bash
#!/bin/bash

# 获取 MySQL 主进程 PID
MYSQL_PID=$(pidof mysqld)
if [ -z "$MYSQL_PID" ]; then
  echo "❌ 没有找到 mysqld 进程"
  exit 1
fi

echo "✅ MySQL 主进程 PID: $MYSQL_PID"
echo "📊 查询高 CPU 使用的线程..."

# top 中获取最高 CPU 的线程 ID（取前 1 个）
TOP_THREAD=$(top -Hp $MYSQL_PID -b -n1 | awk 'NR>7 {print $1, $9}' | sort -k2 -nr | head -1 | awk '{print $1}')
if [ -z "$TOP_THREAD" ]; then
  echo "❌ 没找到高 CPU 线程"
  exit 1
fi

# 转换为十六进制
HEX_THREAD=$(printf "%x\n" $TOP_THREAD)
echo "🔥 高 CPU 线程: $TOP_THREAD (十六进制: $HEX_THREAD)"

echo ""
echo "🚀 执行以下 SQL 获取正在运行的 SQL 语句："
cat <<EOF

USE performance_schema;

SELECT 
    t.THREAD_ID,
    t.PROCESSLIST_ID,
    t.THREAD_OS_ID,
    es.EVENT_ID,
    es.SQL_TEXT,
    es.TIMER_WAIT/1000000000 AS ms
FROM 
    performance_schema.threads t
JOIN 
    performance_schema.events_statements_current es 
    ON t.THREAD_ID = es.THREAD_ID
WHERE 
    t.THREAD_OS_ID = '${HEX_THREAD}';
EOF

echo ""
echo "⚠️ 请将上述 SQL 粘贴到 MySQL 客户端中执行。"
```

