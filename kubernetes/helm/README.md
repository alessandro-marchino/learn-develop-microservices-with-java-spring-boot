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

## Loki

- deploymentMode: SingleBinary
- loki.commonConfig.replication_factor: 1
- loki.storage.type: 'filesystem'
- loki.schemaConfig. configs: [ {"from": "2024-01-01", "store": "tsdb", "index": {"prefix": "loki_index_", "period": "24h"}, "object_store": "filesystem", "schema": "v13"} ]
- singleBinary.replicas: 1
- read.replicas: 0
- backend.replicas: 0
- write.replicas: 0

## Grafana-Tempo

- tempo.traces.otlp.http
- tempo.traces.otlp.grpc
