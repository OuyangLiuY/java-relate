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
du -s  /home或du -sh /home
du -s ，du -sh，ls -lh，
比如查找文件名为backup.sh文件的大小，命令为：
du -s  backup.sh ，ls -lh backup.sh
```

