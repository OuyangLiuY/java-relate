# 三大日志

redo log：redo log buffer pool 记录数据未被刷盘的数据，是循环写的

undo log：记录数据库修改前的数据

bin log：完整记录SQL执行的数据，类似记录整个SQL



# Mysql 事务执行过程

1. 事务写入之前先将旧数据备份在Undo log
2. 然后修改内存中的缓冲池数据，将修改后的数据写入 redo log buffer
3. 事务执行时将redo log buffer的数据写入redo log
4. 并且将redo log的状态改为prepare
5. 再将事务信息写入binlog
6. 最后将 Redo log中写入 commit标记



## 问题：

如果redo log 写入失败：那么

如果 bin log 写入失败：那么事务直接回滚