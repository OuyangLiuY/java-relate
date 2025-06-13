查看资源
kubectl get pods 查看所有Pod
kubectl describe
查看资源详情
kubectl describe pod my-pod
kubectl logs
查看容器日志
kubectl logs my-pod -c my-container
kubectl exec
进入容器执行命令
kubectl exec -it my-pod -- /bin/bash
kubectl apply -f
部署资源
kubectl apply -f deployment.yaml
kubectl delete
删除资源
kubectl delete pod my-pod
kubectl top
查看资源使用情况（需安装 metrics-server）
kubectl top pod 查看CPU/内存
kubectl scale
水平扩缩容
kubectl scale deployment myapp --replicas=5
kubectl rollout
管理部署更新
kubectl rollout status deployment/myapp
kubectl port-forward
本地端口转发
kubectl port-forward pod/my-pod 8080:80


| 指令             | 作用         | 示例                         |
| ---------------- | ------------ | ---------------------------- |
| kubectl get      | 查看资源     | kubectl get pods 查看所有Pod |
| kubectl describe | 镜像地址错误 | 镜像仓库权限。镜像失效       |
| kubectl logs     |              |                              |
| kubectl logs     |              |                              |