# Helm

## Namespaces

Creazione dei vari namespace

```bash
kubectl create namespace keycloak
kubectl create namespace kafka
kubectl create namespace prometheus
kubectl create namespace loki
kubectl create namespace tempo
kubectl create namespace grafana
```

## Valori modificati nei chart

### Keycloack

- auth.adminPassword
- service.type
- service.ports.http
- service.ports.https
- postgresql.auth.postgresPassword
- postgresql.auth.password

```bash
helm install keycloak ./keycloak --namespace keycloak
```

### Kafka

- controller.replicaCount
- listeners.client.protocol
- listeners.controller.protocol
- listeners.interbroker.protocol
- listeners.external.protocol

```bash
helm install kafka ./kafka --namespace kafka
```

### Kube-Prometheus

- server.additionalScrapeConfigs.internal.jobList
- alertmanager.enabled

```bash
helm install prometheus ./kube-prometheus --namespace prometheus
```

To expose Prometheus:

```bash
kubectl port-forward --namespace default svc/prometheus-kube-prometheus-prometheus 9090:9090
```

## Grafana-Loki

Nessuna modifica

```bash
helm install loki ./grafana-loki --namespace loki
```

## Grafana-Tempo

- tempo.traces.otlp.http
- tempo.traces.otlp.grpc

```bash
helm install tempo ./grafana-tempo --namespace tempo
```

## Grafana

- datasources.secretDefinition
- admin.password

```bash
helm install grafana ./grafana --namespace grafana
```
