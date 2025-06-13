# 1、排查故障通用流程

思路：

> **现象 → Pod 状态 → 日志 → 事件 → 网络 → 配置 → 资源 → 容器 → 节点**

# 2、关键步骤+命令

1、查看Pod状态

```bash
kubectl get pods -n <namespace>
```

STATUS 为CrashLoopBackOff、Pending、ImagePullBackOff等表示异常

2、查看详细信息（如事件/错误信息）

3、查看容器日志

4、进入容器排查

5、查看资源使用（内存/CPU不足）

6、检测部署/服务状态

7、网络连通性排查

8、检查ConfigMap/Secret 挂载问题

3、常见问题对照表


| 问题现象         | 排查方向           | col3                   |
| ---------------- | ------------------ | ---------------------- |
| CrashLoopBackOff | 日志中查看配置错误 | 应用崩溃，OOM          |
| ImagePullBackOff | 镜像地址错误       | 镜像仓库权限。镜像失效 |
