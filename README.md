# Master Microservices with SpringBoot, Docker, Kubernetes

Companion repository for Udemy Course
[Master Microservices with SpringBoot, Docker, Kubernetes](https://www.udemy.com/course/master-microservices-with-spring-docker-kubernetes/).

## Commands

- To switch to java 21: `sdk use java 21.0.3-tem`
- To generate the images: `mvn compile jib:dockerBuild`
- To run just the dependencies for the microservices: `dcupd accountsdb cardsdb loansdb redis`

## Keycloak

URL for configuration endpoints: [http://localhost:7080/realms/master/.well-known/openid-configuration](http://localhost:7080/realms/master/.well-known/openid-configuration)

## Kubernetes dashboard

Create via the following commands

```bash
# Add kubernetes-dashboard repository
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
# Deploy a Helm Release named "kubernetes-dashboard" using the kubernetes-dashboard chart
helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard
# Expose
kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443
```

Create user

