# Docker 常用命令大全

适用于开发、部署、调试、监控等常见场景。

## ✅ 一、镜像操作


| 命令                           | 说明                     |
| ------------------------------ | ------------------------ |
| `docker pull image_name`       | 拉取镜像                 |
| `docker images`                | 查看本地所有镜像         |
| `docker rmi image_name`        | 删除镜像                 |
| `docker build -t name:tag .`   | 基于 Dockerfile 构建镜像 |
| `docker tag old_name new_name` | 给镜像打标签             |

## ✅ 二、容器操作


| 命令                             | 说明                       |
| -------------------------------- | -------------------------- |
| `docker run -it image /bin/bash` | 运行容器并进入交互模式     |
| `docker run -d -p 8080:80 image` | 后台运行并映射端口         |
| `docker ps`                      | 查看正在运行的容器         |
| `docker ps -a`                   | 查看所有容器（包括已停止） |
| `docker stop container_id`       | 停止容器                   |
| `docker start container_id`      | 启动已停止的容器           |
| `docker restart container_id`    | 重启容器                   |
| `docker rm container_id`         | 删除容器                   |

## ✅ 三、容器调试与管理


| 命令                                     | 说明                 |
| ---------------------------------------- | -------------------- |
| `docker exec -it container_id /bin/bash` | 进入运行中的容器     |
| `docker logs container_id`               | 查看容器日志         |
| `docker inspect container_id`            | 查看容器详细信息     |
| `docker top container_id`                | 查看容器内运行的进程 |
| `docker stats`                           | 实时查看容器资源占用 |

## ✅ 四、数据卷与挂载


| 命令                                 | 说明                 |
| ------------------------------------ | -------------------- |
| `docker volume create vol_name`      | 创建数据卷           |
| `docker volume ls`                   | 查看所有卷           |
| `docker run -v vol_name:/data image` | 挂载数据卷到容器     |
| `docker run -v $(pwd):/app image`    | 挂载当前目录到容器中 |

## ✅ 五、网络管理


| 命令                                  | 说明               |
| ------------------------------------- | ------------------ |
| `docker network ls`                   | 查看所有网络       |
| `docker network create net_name`      | 创建网络           |
| `docker network inspect net_name`     | 查看网络详情       |
| `docker run --network net_name image` | 连接容器到指定网络 |

## ✅ 六、Docker Compose


| 命令                     | 说明                  |
| ------------------------ | --------------------- |
| `docker-compose up -d`   | 启动所有服务（后台）  |
| `docker-compose down`    | 停止并清理容器/网络等 |
| `docker-compose ps`      | 查看服务状态          |
| `docker-compose logs -f` | 查看日志（跟随模式）  |

## ✅ 七、系统清理与空间释放


| 命令                     | 说明                       |
| ------------------------ | -------------------------- |
| `docker system prune`    | 清理无用镜像、容器、网络等 |
| `docker image prune`     | 清理无用镜像               |
| `docker container prune` | 清理已退出容器             |

## ✅ 八、导出与导入


| 命令                                    | 说明                   |
| --------------------------------------- | ---------------------- |
| `docker save -o image.tar image_name`   | 导出镜像到文件         |
| `docker load -i image.tar`              | 从文件导入镜像         |
| `docker export container_id > file.tar` | 导出容器文件系统       |
| `docker import file.tar`                | 导入容器文件系统为镜像 |
