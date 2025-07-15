# pull image mysql

Docker pull image on my local:

```bash
docker pull docker.xuanyuan.me/mysql:8.0
```

```bash
docker run -d \
  --name mysql-server \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -p 3306:3306 \
  -v ~/orbstack_mysql/data:/var/lib/mysql \
  -v ~/orbstack_mysql/conf.d:/etc/mysql/conf.d \
  mysql:8.0

```





# Docker run redis

Set data dir

```c
docker run -d \
  --name redis-server \
  -p 6379:6379 \
  -v ~/orbstack_redis/data:/data \
  redis:latest \
  redis-server --save 60 1 --appendonly yes
```

  start it on my local:

```bash
docker exec -it redis-server redis-cli
```



limit memory:

```bash
docker run -d \
  --name redis-server \
  -p 6379:6379 \
  redis:latest \
  redis-server --maxmemory 512mb --maxmemory-policy allkeys-lru
```

