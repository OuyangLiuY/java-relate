# 用户命令

root下添加新的账户:

```shell
useradd  chopsticks
echo ywbroot | passwd  --stdin chopsticks
echo "chop_four ALL = (root) NOPASSWD:ALL" | tee /etc/sudoers.d/chopsticks
chmod 0440 /etc/sudoers.d/chopsticks


//修改用户名
usermod -l chopsticks -d /home/chopsticks -m chop_five


在命令框 输入命令：ls  -lht  
将会一一列出当前目录下所有文件的大小，以及所有文件大小的统计总和。或者直接打入ll命令
使用du -sh * 
命令也可以列出当前文件以及文件夹的大小。这个命令要注意：sh与*之前要有个空格的。列出home目录所有文件大小的总和命令为：
du -s  /home 或 du -sh /home
du -s ，du -sh，ls -lh，
比如查找文件名为backup.sh文件的大小，命令为：
du -s  backup.sh ，ls -lh backup.sh
```

### SCP

 使用ssh上传和下文件到服务器：

1：下载到服务器：

```shell
scp username@servername:/path/filename /local_dir  (本地目录)
```

 例如：scp root@192.168.0.101:/var/www/test.txt 把192.168.0.101上的/var/www/test.txt 的文件下载到/var/www/local_dir（本地目录）

2：上传到服务器 ：

```shell
scp /path/filename username@servername:/path 
```

例如：scp /var/www/test.php root@192.168.0.101:/var/www/ 把本机/var/www/目录下的test.php文件上传到192.168.0.101这台服务器上的/var/www/目录中

 下载或者上传整个目录使用-r命令：scp -r username@servername：

