# Kubernetes

主讲：鲁班学院-华安

<hr>

本文参照和概念补充

kubernetes中文社区文档：http://docs.kubernetes.org.cn/

kubernetes官方文档：https://kubernetes.io/zh/docs/

kubernetes官方集群实践：https://www.kubernetes.org.cn/3096.html

Kubernetes（K8s）是Google在2014年发布的一个开源项目。据说Google的数据中心里运行着10多亿个容器，而且Google十年多前就开始使用容器技术。最初，Google开发了一个叫Borg的系统（现在命名为Omega）来调度如此庞大数量的容器和工作负载。在积累了这么多年的经验后，Google决定重写这个容器管理系统，并将其贡献到开源社区，让全世界都能受益。

这个项目就是Kubernetes。简单地讲，Kubernetes是Google Omega的开源版本。

从2014年第一个版本发布以来，Kubernetes迅速获得开源社区的追捧，包括Red Hat、VMware、Canonical在内的很多有影响力的公司加入到开发和推广的阵营。目前Kubernetes已经成为发展最快、市场占有率最高的容器编排引擎产品。

Kubernetes(k8s)一个用于容器集群的自动化部署、扩容以及运维的开源平台。通过Kubernetes,你可以快速有效地响应用户需求;快速而有预期地部署你的应用;极速地扩展你的应用;无缝对接新应用功能;节省资源，优化硬件资源的使用。为容器编排管理提供了完整的开源方案。

特点

- 可移植： 支持公有云，私有云，混合云，多重云（多个公共云）
- 可扩展**：** 模块化，插件化，可挂载，可组合
- 自动化： 自动部署，自动重启，自动复制，自动伸缩/扩展

kubernetes解决了什么？

- 多个进程协同工作
- 存储系统挂载
- 应用健康检查
- 应用实例的复制
- 自动伸缩/扩展
- 注册与发现
- 负载均衡
- 滚动更新
- 资源监控
- 日志访问
- 调试应用程序
- 提供认证和授权

## 安装环境准备

系统:ubuntu 18.04 LTS

虚拟化软件:VMware15 pro

Docker:docker-ce 19.03

K8S:1.15



系统要求

内存:2G

硬盘:20G

核心:2



创建三台虚拟机:

| name              | IP              |
| ----------------- | --------------- |
| kubernetes-master | 192.168.202.131 |
| kubernetes-node1  | 192.168.202.132 |
| kubernetes-node2  | 192.168.202.133 |



虚拟机基本设置:

```shell
#设置ROOT账户密码
sudo passwd root

#切换到root
su

#设置允许远程登陆root账户
vim /etc/ssh/sshd_config
PermitRootLogin yes

#重启服务
service ssh restart

#关闭交换空间
sudo swapoff -a

#避免开机启动交换空间注释/etc/fstab中的swap
#查看
free -h

#关闭防火墙
ufw disable
```



### 在线安装Docker

```shell
#更新软件源
sudo apt-get update

#安装依赖
sudo apt-get -y install apt-transport-https ca-certificates curl software-properties-common

#安装GPG证书
curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -

#增加一条软件源信息
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"

#更新软件源
sudo apt-get -y update

#安装docker-ce
sudo apt-get -y install docker-ce
```

配置docker镜像加速(参考Docker第一节课)



## 搭建Kubernetes集群

Master 

Master 是 Cluster 的大脑，它的主要职责是调度，即决定将应用放在哪里运行。

Node:

Node 是 Pod 真正运行的主机，可以是物理机，也可以是虚拟机。为了管理 Pod，每个 Node 节点上至少要运行 container runtime（比如 docker 或者 rkt）、`kubelet` 和 `kube-proxy` 服务。

Pod:

Kubernetes 使用 Pod 来管理容器，每个 Pod 可以包含一个或多个紧密关联的容器。

Pod 是一组紧密关联的容器集合，它们共享 PID、IPC、Network 和 UTS namespace，是 Kubernetes 调度的基本单位。Pod 内的多个容器共享网络和文件系统，可以通过进程间通信和文件共享这种简单高效的方式组合完成服务。

Service:

Service 是应用服务的抽象，通过 labels 为应用提供负载均衡和服务发现。匹配 labels 的 Pod IP 和端口列表组成 endpoints，由 kube-proxy 负责将服务 IP 负载均衡到这些 endpoints 上。

每个 Service 都会自动分配一个 cluster IP（仅在集群内部可访问的虚拟地址）和 DNS 名，其他容器可以通过该地址或 DNS 来访问服务，而不需要了解后端容器的运行。

kubernetes组件:

- etcd 保存了整个集群的状态,服务注册发现；
- kube-apiserver 提供了资源操作的唯一入口，并提供认证、授权、访问控制等机制；
- kube-controller-manager 负责维护集群的状态，比如故障检测、自动扩展、滚动更新等；
- kube-scheduler 负责资源的调度，按照预定的调度策略将 Pod 调度到相应的机器上；
- kubelet 负责维持容器的生命周期，同时也负责 Volume（CVI）和网络（CNI）的管理；
- Container runtime 负责镜像管理以及 Pod 和容器的真正运行（CRI），默认的容器运行时为 Docker；
- kube-proxy 负责为 Service 提供 cluster 内部的服务发现和负载均衡；

安装kubernetes(三台机器同时进行)

1.修改主机名

查看ubuntu环境配置文件cloud.cfg(/etc/cloud目录下)

```shell
#该选项为是否允许修改主机名,把preserve_hostname修改为true
preserve_hostname: true
```

2.使用hostnamectl修改主机名

```shell
hostnamectl set-hostname kubernetes-master
```

3.查看主机信息是否修改成功

```shell
hostnamectl
```

4.安装kubelet,kubeadm,kubectl

- kubelet:主要负责启动POD和容器
- kubeadm:用于初始化kubernetes集群
- kubectl:kubenetes的命令行工具，作用是部署和管理应用，查看各种资源，创建，删除和更新组件

```shell
#配置软件源
#安装系统工具
apt-get update && apt-get install -y apt-transport-https

# 安装 GPG 证书
curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add -

# 写入软件源
cat << EOF >/etc/apt/sources.list.d/kubernetes.list
> deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
> EOF

#安装kubelet,kubeadm,kubectl
apt-get update
apt-get install -y kubelet kubeadm kubectl

1.15.2

#设置kubectl开机启动并且启动kubelet
systemctl enable kubelet && systemctl start kubelet
```

#注意：安装完最后会打印一个版本号，记住版本后面配置master的配置文件需要用到

### 配置Master

1.导出配置文件

```shell
kubeadm config print init-defaults --kubeconfig ClusterConfiguration > kubeadm.yml
```

2.修改配置文件

```yaml
#修改advertiseAddress:为master主机IP
advertiseAddress: 192.168.202.128
#因为有墙,把镜像源修改为国内的，比如阿里云
imageRepository: registry.aliyuncs.com/google_containers
#顺便配置calico的默认网段(后面网络配置会用到)
podSubnet: "192.168.0.0/16"
```

3.拉取所需要的镜像

```shell
#查看所需镜像列表
kubeadm config images list --config kubeadm.yml
#拉取镜像
kubeadm config images pull --config kubeadm.yml
```

4.初始化主节点

```shell
#定了初始化时需要使用的配置文件，其中添加 --experimental-upload-certs 参数可以在后续执行加入节点时自动分发证书文件。追加的 tee kubeadm-init.log 用以输出日志
kubeadm init --config=kubeadm.yml --experimental-upload-certs | tee kubeadm-init.log
```

5.配置kubectl(第四步结尾输出步骤)

```shell
mkdir -p $HOME/.kube
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
```

6.验证主节点是否安装完成

```shell
kubectl get nodes
```

#### kubeadm init 的执行过程

- init：指定版本进行初始化操作
- preflight：初始化前的检查和下载所需要的 Docker 镜像文件
- kubelet-start：生成 kubelet 的配置文件 `var/lib/kubelet/config.yaml`，没有这个文件 kubelet 无法启动，所以初始化之前的 kubelet 实际上启动不会成功
- certificates：生成 Kubernetes 使用的证书，存放在 `/etc/kubernetes/pki` 目录中
- kubeconfig：生成 KubeConfig 文件，存放在 `/etc/kubernetes` 目录中，组件之间通信需要使用对应文件
- control-plane：使用 `/etc/kubernetes/manifest` 目录下的 YAML 文件，安装 Master 组件
- etcd：使用 `/etc/kubernetes/manifest/etcd.yaml` 安装 Etcd 服务
- wait-control-plane：等待 control-plan 部署的 Master 组件启动
- apiclient：检查 Master 组件服务状态。
- uploadconfig：更新配置
- kubelet：使用 configMap 配置 kubelet
- patchnode：更新 CNI 信息到 Node 上，通过注释的方式记录
- mark-control-plane：为当前节点打标签，打了角色 Master，和不可调度标签，这样默认就不会使用 Master 节点来运行 Pod
- bootstrap-token：生成 token 记录下来，后边使用 `kubeadm join` 往集群中添加节点时会用到
- addons：安装附加组件 CoreDNS 和 kube-proxy

### 配置Node

安装完成kubernetes之后，把node加入到集群即可，加入节点的令牌从`kubeadm-init.log`文件可以获取到

```shell
kubeadm join 192.168.202.128:6443 --token abcdef.0123456789abcdef     --discovery-token-ca-cert-hash sha256:783e4149509b878bb8b87bd69aa95e4468ae15c8d259c9be6e79db73289a4148
```

在master主机验证节点是否加入到了集群

```shell
kubectl get nodes
```

### 网络配置

查看kubernetes组件运行状态，发现有两个没有启动，这时候需要进行网络配置

```shell
kubectl get pod -n kube-system -o wide
```

至于容器之间为什么不能直接通信，参考docker第三节课内容。



在配置网络之前，因为之前设置的是动态获取IP地址，这里三台机器修改为静态IP和防止DNS配置文件被更改

```shell
#修改配置文件
vim /etc/netplan/50-cloud-init.yaml

#yml文件一定要主要空格
network:
    ethernets:
        ens33:
           addresses: [192.168.202.128/24]
           gateway4: 192.168.202.2
           nameservers:
                addresses: [192.168.202.2]
    version: 2

#刷新配置
netplan apply
```



修改DNS

```shell
#ubuntu18的dns配置文件是交给systemd-resolved这个服务管理的，有可能会被他覆盖我们自定的dns地址，所以先停止systemd-resolved的服务
systemctl stop systemd-resolved

#修改dns
vim /etc/systemd/resolved.conf

nameserver 8.8.8.8
```



#### CNI

CNI(Container Network Interface) 是一个标准的，通用的接口。在容器平台，Docker，Kubernetes，Mesos 容器网络解决方案 flannel，calico，weave。只要提供一个标准的接口，就能为同样满足该协议的所有容器平台提供网络功能，而 CNI 正是这样的一个标准接口协议。

#### Kubernetes 中的 CNI 插件

CNI 的初衷是创建一个框架，用于在配置或销毁容器时动态配置适当的网络配置和资源。插件负责为接口配置和管理 IP 地址，并且通常提供与 IP 管理、每个容器的 IP 分配、以及多主机连接相关的功能。容器运行时会调用网络插件，从而在容器启动时分配 IP 地址并配置网络，并在删除容器时再次调用它以清理这些资源。

运行时或协调器决定了容器应该加入哪个网络以及它需要调用哪个插件。然后，插件会将接口添加到容器网络命名空间中，作为一个 veth 对的一侧。接着，它会在主机上进行更改，包括将 veth 的其他部分连接到网桥。再之后，它会通过调用单独的 IPAM（IP地址管理）插件来分配 IP 地址并设置路由。

在 Kubernetes 中，kubelet 可以在适当的时间调用它找到的插件，为通过 kubelet 启动的 pod进行自动的网络配置。

#### Calico

Calico 为容器和虚拟机提供了安全的网络连接解决方案，并经过了大规模生产验证（在公有云和跨数千个集群节点中），可与 Kubernetes，OpenShift，Docker，Mesos，DC / OS 和 OpenStack 集成。

Calico 还提供网络安全规则的动态实施。使用 Calico 的简单策略语言，您可以实现对容器，虚拟机工作负载和裸机主机端点之间通信的细粒度控制。

1.安装网络插件 Calico

```shell
#官方文档安装：https://docs.projectcalico.org/v3.7/getting-started/kubernetes/
#在Master操作即可
kubectl apply -f https://docs.projectcalico.org/v3.7/manifests/calico.yaml
#验证是否成功
kubectl get pods --all-namespaces
```

#注意:如果启动不成功，很简单，在master把节点删除，重新设置node的kubeadm然后重启，然后重新加入集群。

### 实例:运行Tomcat容器

1.健康检查

```shell
kubectl get cs

#输出
NAME                 STATUS    MESSAGE             ERROR
scheduler            Healthy   ok                  
controller-manager   Healthy   ok                  
etcd-0               Healthy   {"health":"true"}
#scheduler调度服务，主要作用是将 POD 调度到 Node       
#controller-manage自动化修复服务，主要作用是 Node 宕机后自动修复 Node 回到正常的工作状态           
#etcd-0服务注册与发现

master apiserver     
node runtime kubelet kube-proxy node1 node2 
```

2.查看master状态

```
kubectl cluster-info
```

3.查看node状态

```
kubectl get nodes
```

4.运行tomcat容器

```shell
#使用kubectl命令创建两个监听8080端口的tomcat pod(Kubernetes运行容器的最小单元)
kubectl run tomcat --image=tomcat --replicas=2 --port=80

#查看pod状态
kubectl get pods

#查看已部署的服务
kubectl get deployment

#映射服务
kubectl expose deployment tomcat --port=8080 --type=LoadBalancer

#查看已经发布的服务
kubectl get services

#查看服务详情
kubectl describe service tomcat
```

5.访问

6.停止服务并删除

```shell
kubectl delete deployment tomcat
kubectl delete service tomcat
```

