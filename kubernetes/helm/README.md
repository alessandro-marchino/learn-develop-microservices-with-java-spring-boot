# Helm

## Valori modificati nei chart

### Keycloack

- auth.adminPassword
- service.type
- service.ports.http
- service.ports.https
- postgresql.auth.postgresPassword
- postgresql.auth.password

### Kafka

- controller.replicaCount
- listeners.client.protocol
- listeners.controller.protocol
- listeners.interbroker.protocol
- listeners.external.protocol

### Prometheus

- server.extraScrapeConfigs
- server.service.type
- alertmanager.service.type

To expose Prometheus:

```bash
kubectl port-forward --namespace default svc/prometheus-server 9091:9090
```
