## **K8s 线上问题排查流程**

## 典型问题：

1. DNS 问题：内网dns忘记配置 ，导致服务调用找不到

```shell
kubectl exec -it pod-name -- nslookup svc-name
# 进入pod内部，使用nslookup 查看当前域名是否能解析成对应的ip
```



### **一、问题快速确认**

#### **✅ 常见问题分类：**



| **类型**             | **表现**                                 |
| -------------------- | ---------------------------------------- |
| 服务不可用           | Pod 启动不了、接口无法访问、503 错误     |
| 性能下降             | 响应慢、CPU飙高、内存爆炸                |
| 网络异常             | 跨服务无法通信、DNS 失败、请求丢失       |
| 数据错误             | 配置失效、环境变量错、容器内文件路径丢失 |
| 异常重启 / CrashLoop | Pod 重启频繁、状态 CrashLoopBackOff      |



### **二、通用排查思路**

```
问题确认 → Pod排查 → 节点排查 → 网络排查 → 日志排查 → 配置检查 → 服务依赖
```



### **三、详细排查步骤**

#### **1️⃣ 问题确认 & 定位命名空间**



```
kubectl get ns
kubectl get pods -n your-namespace
```

#### **2️⃣ Pod 层排查**

```
kubectl get pods -n your-ns
kubectl describe pod pod-name -n your-ns
```

状态说明：

| **状态**         | **含义**                       |
| ---------------- | ------------------------------ |
| Running          | 正常运行中                     |
| Pending          | 没有被调度成功（可能资源不足） |
| CrashLoopBackOff | 不断重启（代码/配置问题常见）  |
| OOMKilled        | 被内存杀死（资源问题）         |
| ImagePullBackOff | 拉取镜像失败                   |

#### **3️⃣ 查看日志**

```
kubectl logs pod-name -n your-ns [--container container-name]
kubectl logs --previous pod-name -n your-ns
```

#### **4️⃣ 检查资源（CPU、内存）**

```
kubectl top pod -n your-ns
kubectl top node
```

资源不足可能导致 Pending、OOMKilled。

#### **5️⃣ 探针状态**



```
kubectl describe pod pod-name -n your-ns
```

- readinessProbe 就绪探针失败 → 不对外服务
- livenessProbe 存活探针失败 → 会触发自动重启

#### **6️⃣ 网络排查**

**跨Pod 通信失败？**

- 使用 kubectl exec 到容器内部 curl / ping

```
kubectl exec -it pod-name -- bash
curl http://svc-name.namespace.svc.cluster.local
kubectl exec -it pod-name -- nslookup svc-name
```

#### **DNS 问题？**

kubectl exec -it pod-name -- nslookup svc-name

#### **7️⃣ 服务是否注册成功**

检查 Spring Cloud / Consul 等注册中心日志。



#### **8️⃣ 查看 Deployment 状态**



```
kubectl describe deployment your-deploy-name -n your-ns
kubectl get replicaset -n your-ns
```

看是否副本不足、滚动失败、更新卡住

#### **9️⃣ 查看配置项**

- 是否改了配置未生效？
- 探针路径、数据库密码等是否变动？

```
kubectl describe configmap your-cm -n your-ns
kubectl describe secret your-secret -n your-ns
```





### **四、工具辅助建议**



| **工具**             | **作用**                 |
| -------------------- | ------------------------ |
| Prometheus + Grafana | 查看指标（CPU、RT、QPS） |
| ELK / Loki           | 日志聚合搜索             |
| K9s / Lens           | 界面化查看状态           |
| kubectl debug        | 调试容器网络、进程       |
| ArgoCD / Helm        | 管理部署、快速回滚       |



### **五、应急处理建议**



| **问题类别** | **快速应对方式**                                    |
| ------------ | --------------------------------------------------- |
| 某服务不可用 | 回滚镜像版本，或使用 kubectl rollout undo           |
| Pod 频繁重启 | 提高资源限制，检查探针配置                          |
| 服务调用失败 | 检查 svc、网络策略、DNS、Pod 状态                   |
| 全站服务异常 | 排查 ingress、gateway、核心依赖服务（如 Redis、DB） |
| 配置错误     | 检查 ConfigMap / Secret / 环境变量                  |



### **六、排查流程图**



```
【入口问题】
     ↓
kubectl get pods 查看状态
     ↓
Pod状态异常？→ describe + logs → 初步定位

Pod正常但服务异常？
     ↓
探针失败？网络失败？资源问题？配置错误？
     ↓
traceId、metrics、调用链分析 → 定位服务或依赖问题
     ↓
回滚、扩容、重启、修复配置
```



# 更多排查命令和步骤

完整的Kubernetes线上问题排查流程，包括：
1. 问题分类与初步判断
2. 详细的排查步骤（节点/Pod/网络/存储/组件）
3. 高级排查工具介绍
4. 常见问题解决方案
5. 可视化排查流程图
6. 问题解决与预防措施

## 1. 问题分类与初步判断

### 1.1 问题类型识别
- **应用层问题**：Pod运行异常、服务不可用
- **网络问题**：服务间通信失败、Ingress访问异常
- **存储问题**：PV/PVC挂载失败、存储性能问题
- **资源问题**：节点资源不足、OOM Killer
- **集群组件问题**：API Server、kubelet、etcd异常

### 1.2 快速诊断命令
```bash
# 查看集群整体状态
kubectl get componentstatuses

# 查看节点资源情况
kubectl top nodes

# 查看异常Pod
kubectl get pods --all-namespaces --field-selector=status.phase!=Running

# 集群健康状态
kubectl get componentstatuses
kubectl version --short

# 资源概览
kubectl get nodes -o wide
kubectl top nodes
kubectl get pods -A -o wide | grep -v Running

# 事件检查
kubectl get events -A --sort-by=.metadata.creationTimestamp
```

## 2. 详细排查流程

### 1.节点层面：

```bash
# 检查节点状态
kubectl get nodes -o wide

# 查看节点详细情况
kubectl describe node <node-name>

# 检查节点资源使用
kubectl top node <node-name>

# 登录问题节点检查
ssh <node-ip>
# 检查系统负载
uptime
top
# 检查磁盘空间
df -h
# 检查内存
free -m
```

### 2.Pod层面排查：

```shell
# 查看Pod状态
kubectl get pods -o wide -n <namespace>

# 查看Pod详情（重点关注Events部分）
kubectl describe pod <pod-name> -n <namespace>

# 查看Pod日志
kubectl logs <pod-name> -n <namespace>
kubectl logs <pod-name> -n <namespace> --previous # 查看前一个容器的日志

# 进入Pod调试
kubectl exec -it <pod-name> -n <namespace> -- /bin/sh		
```

### 3.服务与网络排查：

```shell
# 检查Service
kubectl get svc -n <namespace>
kubectl describe svc <service-name> -n <namespace>

# 检查Endpoint
kubectl get endpoints -n <namespace>

# 检查Ingress
kubectl get ingress -n <namespace>
kubectl describe ingress <ingress-name> -n <namespace>

# 网络连通性测试
kubectl run network-test --image=busybox -it --rm --restart=Never -- sh
# 在临时容器中测试
wget -O- <service-name>.<namespace>.svc.cluster.local:<port>
ping <service-name>.<namespace>.svc.cluster.local	
```

### 4.储存排查：

```shell
# 检查PVC/PV状态
kubectl get pvc -n <namespace>
kubectl get pv

# 查看存储类
kubectl get storageclass

# 检查存储详情
kubectl describe pvc <pvc-name> -n <namespace>
kubectl describe pv <pv-name>	
```

### 5.集群组建排查：

```shell
# 检查kubelet状态
systemctl status kubelet
journalctl -u kubelet -f

# 检查容器运行时
systemctl status docker/containerd
journalctl -u docker/containerd -f

# 检查API Server日志
kubectl logs -n kube-system kube-apiserver-<node-name>

# 检查etcd健康状态
kubectl exec -n kube-system etcd-<node-name> -- etcdctl endpoint health	
```



## 3. 高级排查工具

### 3.1 监控工具

- **Prometheus + Grafana**：查看历史指标
- **kube-state-metrics**：集群状态指标
- **Metrics Server**：资源使用指标

### 3.2 网络诊断工具

- **kubectl debug**：创建调试容器

- **tcpdump**：抓包分析

  ```shell
  kubectl run tcpdump -n <namespace> --image=corfr/tcpdump -it --rm -- \
  tcpdump -i any -w /tmp/dump.pcap host <pod-ip>
  ```

- **netshoot**：网络诊断工具箱

  ```bash
  kubectl run netshoot -n <namespace> --image=nicolaka/netshoot -it --rm -- /bin/bash
  ```

### 3.3 日志收集

- **ELK Stack**：集中式日志

- **Fluentd + Loki**：轻量级日志方案

- **kubectl tail**：多Pod日志跟踪

  ```bash
  stern <pod-prefix> -n <namespace>
  ```

## 4. 常见问题与解决方案

### 4.1 Pod处于Pending状态

**可能原因**：

- 资源不足（CPU/内存）
- 没有匹配的节点（NodeSelector/Affinity/Taints）
- PVC无法绑定PV

**排查**：

```bash
kubectl describe pod <pod-name> # 查看Events
kubectl get events --sort-by=.metadata.creationTimestamp
```

### 4.2 Pod不断重启

**可能原因**：

- 应用崩溃（检查应用日志）
- 存活探针失败
- OOM被杀死

**排查**：

```bash
kubectl logs <pod-name> --previous
kubectl describe pod <pod-name> | grep -i "exit code"
dmesg | grep -i "oom" # 在节点上执行
```

### 4.3 服务无法访问

**可能原因**：

- Service selector与Pod labels不匹配
- 网络策略阻止访问
- 端口配置错误

**排查**：

```bash
kubectl get endpoints <service-name>
kubectl run test --image=busybox -it --rm --restart=Never -- wget -O- <service>:<port>
kubectl get networkpolicy -n <namespace>
```

## 5. 排查流程图

```
开始
↓
收集问题现象（错误信息、影响范围）
↓
检查集群整体状态（kubectl get nodes, pods）
↓
定位问题层面：
   ├─ 节点问题？ → 检查节点资源/组件
   ├─ Pod问题？ → 检查Pod状态/日志
   ├─ 网络问题？ → 检查Service/Ingress/NetworkPolicy
   └─ 存储问题？ → 检查PVC/PV
↓
使用高级工具深入分析（tcpdump, debug容器）
↓
验证解决方案
↓
实施修复并监控效果
↓
记录问题和解决方案
结束
```

## 6. 问题解决与预防

### 6.1 临时解决方案

- 重启Pod：`kubectl delete pod <pod-name>`
- 扩容节点或调整资源限制
- 回滚到稳定版本：`kubectl rollout undo deployment/<deploy-name>`

### 6.2 长期改进

- 完善监控告警（Prometheus AlertManager）
- 实施健康检查（Readiness/Liveness Probe）
- 资源请求与限制优化
- 定期集群健康检查

### 6.3 事后复盘

- 记录问题时间线
- 分析根本原因
- 制定预防措施
- 更新运维手册

