# Helm

## Valori modificati nei chart

### Keycloack

- auth.adminPassword
- service.type
- service.ports.http
- service.ports.https
- postgresql.auth.postgresPassword
- postgresql.auth.password

```bash
helm install keycloak ./keycloak
```

### Kafka

- controller.replicaCount
- listeners.client.protocol
- listeners.controller.protocol
- listeners.interbroker.protocol
- listeners.external.protocol

```bash
helm install kafka ./kafka
```

### Kube-Prometheus

- server.additionalScrapeConfigs.internal.jobList
- alertmanager.enabled

```bash
helm install prometheus ./kube-prometheus
```

To expose:

```bash
kubectl port-forward svc/prometheus-kube-prometheus-prometheus 9090:9090
```

### Grafana-Loki

Nessuna modifica

```bash
helm install loki ./grafana-loki
```

### Grafana-Tempo

- tempo.traces.otlp.http
- tempo.traces.otlp.grpc

```bash
helm install tempo ./grafana-tempo
```

### Grafana

- datasources.secretDefinition
- admin.password

```bash
helm install grafana ./grafana
```

To expose:

```bash
kubectl port-forward svc/grafana 3000:3000
```


## Deploy prodotto

### Dev

```bash
helm install eazybank-dev ./dev-env
```
