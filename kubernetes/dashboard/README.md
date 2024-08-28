# Kubernetes dashboard

## Recent Kubernetes cluster

Run the following scripts

```bash
kubectl apply -n kubernetes-dashboard -f dashboard-service-account.yml
kubectl apply -n kubernetes-dashboard -f dashboard-cluster-role-binding.yml
kubectl apply -n kubernetes-dashboard -f dashboard-service-account-token.yml
```

Start via

```bash
kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443
```

and connect to [https://localhost:8443](https://localhost:8443)

## Legacy Kubernetes cluster

Run the following scripts

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
```

Start via

```bash
kubectl proxy
```

and connect to
[http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/)

## Token

Obtain token via
```bash
kubectl -n kubernetes-dashboard create token admin-user

kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath={".data.token"} | base64 -d
```
