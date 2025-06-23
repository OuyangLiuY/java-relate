# 如何一行命令杀死所有Java进程


Linux/MacOs

```bash
ps -ef | grep '[j]ava' | awk '{print $2}' | xargs kill -9
```

- ps -ef : 列出当前系统所有正在运行的进程详细信息。
- grep '[j]ava' : 选出那些命令中包含‘java’ 字符串的进程。
- awk '{print $2}'：从选出的结果中，提取第二列，也就是进程的PID（Process ID）
- xargs kill -9：将前面提取到的所有PID作为参数，传递给 kill -9 命令。kill -9 会发送SIGKILL 信号，强制终止这些进程。
