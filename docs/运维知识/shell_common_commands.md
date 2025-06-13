# Shell 常用命令大全

涵盖文件操作、文本处理、权限管理、系统信息、网络、压缩解压、进程管理等常见场景，适用于日常开发和运维。

## ✅ 一、文件和目录操作


| 命令                   | 作用                           |
| ---------------------- | ------------------------------ |
| `ls -l`                | 列出当前目录下文件（详细信息） |
| `cd /path/to/dir`      | 进入指定目录                   |
| `pwd`                  | 显示当前路径                   |
| `mkdir mydir`          | 创建目录                       |
| `rm -rf file_or_dir`   | 删除文件或目录（危险）         |
| `cp a.txt b.txt`       | 拷贝文件                       |
| `mv a.txt /path/`      | 移动或重命名文件               |
| `touch newfile.txt`    | 创建空文件                     |
| `find . -name "*.log"` | 查找当前目录下所有`.log` 文件  |

## ✅ 二、文本处理工具


| 命令                            | 作用                 |
| ------------------------------- | -------------------- |
| `cat file.txt`                  | 显示文件内容         |
| `more / less file.txt`          | 分页查看大文件       |
| `head -n 10 file.txt`           | 查看前10行           |
| `tail -n 20 file.txt`           | 查看后20行           |
| `tail -f logfile.log`           | 实时查看日志输出     |
| `grep "keyword" file.txt`       | 查找包含某关键词的行 |
| `cut -d ":" -f2 file.txt`       | 分隔并取第2列        |
| `awk '{print $1, $3}' file.txt` | 按列处理文本         |
| `sed 's/old/new/g' file.txt`    | 批量替换文本内容     |

## ✅ 三、权限管理


| 命令                    | 作用                 |
| ----------------------- | -------------------- |
| `chmod +x script.sh`    | 赋予执行权限         |
| `chown user:group file` | 修改所有者           |
| `sudo command`          | 以管理员权限执行命令 |

## ✅ 四、压缩与解压


| 命令                     | 作用               |
| ------------------------ | ------------------ |
| `tar -czf a.tar.gz dir/` | 压缩目录为 .tar.gz |
| `tar -xzf a.tar.gz`      | 解压 .tar.gz       |
| `zip -r a.zip dir/`      | 压缩为 .zip        |
| `unzip a.zip`            | 解压 zip 文件      |

## ✅ 五、系统与资源信息


| 命令           | 作用                     |
| -------------- | ------------------------ |
| `top` / `htop` | 实时查看系统资源使用情况 |
| `df -h`        | 查看磁盘空间使用         |
| `free -m`      | 查看内存使用             |
| `uptime`       | 查看系统运行时间         |
| `uname -a`     | 查看系统内核信息         |
| `whoami`       | 当前用户                 |
| `ps aux`       | 查看进程                 |
| `kill -9 PID`  | 强制终止进程             |
| `history`      | 查看命令历史             |

## ✅ 六、网络相关


| 命令                   | 作用             |
| ---------------------- | ---------------- |
| `ping www.baidu.com`   | 测试连通性       |
| `curl http://url`      | 发送 HTTP 请求   |
| `wget http://url`      | 下载文件         |
| `netstat -tlnp`        | 查看端口监听情况 |
| `ss -lntp`             | 更快替代 netstat |
| `ifconfig` / `ip addr` | 查看网络配置     |

## ✅ 七、Shell 脚本常用


| 命令                            | 作用                   |
| ------------------------------- | ---------------------- |
| `echo $VAR`                     | 打印变量值             |
| `read VAR`                      | 读取用户输入           |
| `for var in list; do ... done`  | for 循环               |
| `if [ $a -eq $b ]; then ... fi` | 条件判断               |
| `$(command)` / `` `command` ``  | 命令替换               |
| `source script.sh`              | 执行脚本并保留环境变量 |

## ✅ 八、其他实用技巧


| 命令               | 说明             |
| ------------------ | ---------------- |
| `alias ll='ls -l'` | 创建命令别名     |
| `crontab -e`       | 编辑定时任务     |
| `date`             | 显示当前时间     |
| `man <command>`    | 查看命令帮助文档 |
