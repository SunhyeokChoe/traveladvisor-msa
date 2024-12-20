# 트래블어드바이저(traveladvisor) — Microservices with Spring Cloud Gateway, OAuth2, Keycloak, Kafka, Debezium CDC and PostgreSQL

# 이 프로젝트를 만든 이유
<p align="center">
  <img src="https://media4.giphy.com/media/8dYmJ6Buo3lYY/giphy.gif?cid=7941fdc6vac0eapvo79w9tgrd48b8a9thrpwqw27t6rv76ty&ep=v1_gifs_search&rid=giphy.gif&ct=g" alt="baby trump" />
</p>



이 프로젝트는 MSA + 쿠버네티스 환경에서 DDD와 Hexagonal Architecture를 결합 한다고 했을 때 어떻게 구성되고 관리되어야 하는지에 대한 호기심에서 시작되었습니다. 작은 규모의 프로젝트에서는 모놀리식 아키텍처나 멀티레포보다 복잡도가 높아지고, 러닝 커브가 높아 개발 속도가 느려질 수 있다는 점이 단점으로 올 수 있지만, 대규모 개발 환경에서는 조직 구조, 팀원 구성, 가용 가능 인원, 그리고 회사의 여유 자금 등 다양한 요인에 따라 MSA가 적합한 프로젝트 구조가 될 수 있다고 합니다.

진행하다 보니 Saga + Outbox 패턴을 알게 돼 공부해 데모로 적용 해보기도 하고, 이와 더불어 쿠버네티스 학습도 병행하기 위해 **정말 많은 레퍼런스**를 찾아가며 만들었습니다. 진행하면서 Saga와 Outbox의 장점과 쿠버네티스의 편의성에 대해 어느 정도 알게 되었습니다. 다만 Hexagonal 패턴.. 은 앞으로 진행할 사이드 프로젝트에 적용할 생각은 다소 없어졌습니다. 온 종일 코딩만 하다가 Git 스테이지를 살펴보면 80개 파일의 코드에 수정 사항이 있다고 뜨기도 합니다. 이는 Adapter, Application, Domain Core가 추상화 레이어를 통해 철저히 나뉘어 각 계층 간 격벽 역할을 훌륭히 수행하고 있다는 반증이기도 하지만, 작은 규모에서는 뚜렷한 미래 목표가 없으면 복잡도만 증가하고 유지보수와 개발 속도에 유의미한 효과는 없어 보이기 때문입니다. 다만, 팀이 잘 나누어져 있고, 각자 도메인을 맡아 처리할 수 있는 환경이라면 도입 해보는 것도 좋아 보입니다.

마지막으로 이 프로젝트를 진행한 이유 중 하나는 과거 거대 버티컬 이커머스 프로젝트를 진행하면서 쿠버네티스를 처음 접해봤는데, 그때 느꼈던 부족함을 보완하고, 이번 기회에 쿠버네티스와 더욱 친숙해지는 계기를 만들고 싶었습니다.

# 개략적인 도메인 설명
해외 여행 시 가장 먼저 하는 것이 여행 목적지에 가기 위한 항공권 예약 입니다. 그 다음으로 중요한 것은 인근 호텔 예약입니다. 그리고 이 호텔을 중심으로 여러 액티비티를 즐깁니다. 여행 시 장거리 이동의 편의를 위해 현지에서 차량을 예약하기도 합니다. 이 프로젝트는 이처럼 해외 여행에 필수적인 숙박, 차량, 항공권을 손쉽게 한 번에 예약해주는 서비스입니다. 다만, 도메인 보다는 적절한 MSA 아키텍처 구현에 중점을 두고 있으므로 아키텍처 관점에서 봐주세요.

# MVP 구현 목표

## 1) MVP ver.1
- 간단한 호텔/차량/항공권 예약 시스템을 만듭니다.
- 코어 도메인 설계는 DDD 기반으로 합니다.
- Hexagonal Architecture를 도입해 외부 요인과의 강결합을 없애고, 코어 도메인과 서비스 레이어를 분리해 도메인을 보호합니다.
- 모든 애플리케이션 서버, DB 등의 리소스는 쿠버네티스 클러스터에 등록합니다.
- Reservation, Payment, Hotel, Car, Flight 다섯 개의 마이크로서비스를 기반으로 비즈니스 로직을 구성합니다.

  Hotel, Car, Flight 에 관한 테스트 정보를 얻기 위한 OTA* 벤더로 Amadeus를 사용합니다.

  ※ *OTA: OTA는 Online Travel Agency의 약자로, 온라인 여행 예약 대행 서비스를 의미합니다. Agoda와 Expedia가 대표적인 OTA로 잘 알려져 있습니다.

- Spring Cloud Gateway 를 사용해 게이트웨이 엣지 서버를 구성합니다.
- KeyCloak을 활용해 OAuth2 인증을 수행합니다. 인증 서버는 KeyCloak 서버이고, Gateway 서버가 리소스 서버입니다.
- 회복력과 가용성을 위해 Resilience4j를 적용합니다. 이는 서비스 내 리소스의 적절한 격리, DDos와 같은 brute force 공격 예방 등에 도움을 줍니다.
- 각 마이크로서비스를 추적하고 관리하기 위해 Observability(metrics, tracing, logs) 데이터를 지속해서 수집하고 Grafana에서 모니터링 합니다. merics의 경우 Spring Actuator + Micrometer를 통해 Prometheus가 이해할 수 있는 JSON 포맷으로 제공하고, Grafana에서 모니터링 및 쿼리 가능합니다. logs의 경우 데이터 소스로 Loki를 사용하고, 이 또한 Grafana와 통합했습니다. Tracing의 경우 다음의 두 가지 방식을 제공합니다.
    1. 각 마이크로서비스에 OpenTelemetry Java Agent 의존성을 추가하고 Exporter의 도움을 받아 분산 추적 데이터를 자동으로 Loki, Tempo에 보냅니다. 추적을 위해 OpenTelemetry 를 사용해 로그 Prefix 포맷을 Service Name, Trace ID, Span ID 형식으로 구조를 잡고, Grafana Loki + Tempo 에서 이를 시각화 합니다.
    2. 쿠버네티스 클러스터에서 Observability 추적이 필요한 Pod 내부에 Envoy proxy를 Sidecar로 마이크로서비스와 함께 배포하고, 각 Pod 간의 East-West API 요청/응답은 이 Envoy proxy를 통해 진행합니다. Istio를 사용해 각 Envoy proxy를 관리하고, 모니터링 합니다. 이 방식은 마이크로서비스에는 비즈니스 로직만 존재하고, 지표 수집은 분리된 Sidecar를 통해 진행하므로 [a] 방식에 비해 인프라 관리에 용이하고, 개발자는 비즈니스 코어에 집중하기 좋은 환경이 구성됩니다.
- 호텔/항공권/차량 예약 가능(호텔 도메인 용어로 Availibility 라고 합니다.) 목록은 Amadeus의 테스트 전용 API를 Spring Batch 애플리케이션을 통해 호출하여 적재합니다. 이미 이 작업은 선행했으므로 가데이터는 SQL 파일로 제공합니다. 따라서 따로 구동하지 않으셔도 됩니다.

## 2) MVP ver.2
- 기존의 카프카 처리 방식을 Spring Cloud 기반으로 변경합니다.

  Zookeeper, Spring Kafka → KRaft, Spring Cloud Function, Spring Cloud Stream(with Kafka)

- 테스트 호텔 예약 가능(Availibility) 데이터를 기반으로 Hote, Flight , Car 가데이터를 DB에 지속해서 생성하고 업데이트 합니다.
- Hote, Flight , Car 데이터를 Elasticsearch에 색인하고, 검색될 수 있도록 합니다.
- 글로벌 캐시 클러스터를 구성하고 배치 업데이트를 통해 Hote, Flight , Car 예약 데이터가 실시간으로 업데이트 되도록 합니다.
- 외부 업체가 Hote, Flight , Car 데이터를 요청하는 즉, 트래블어드바이저가 공급업체가 되는 상황을 모사하기 위해 Open API 엔드포인트와 게이트웨이를 구현합니다.
    - REST API를 Hateoas 규정에 맞게 구현합니다.
    - 게이트웨이에 들어오는 요청 중 외부 업체의 등급에 따라 Rate Limit 제한을 동적으로 조율합니다.

      e.g., 높은 등급 일 경우 느슨하게, 낮은 등급 일 경우 타이트하게 제한합니다.

## 3) MVP ver.3
- React로 프론트오피스를 구현합니다.

# 시스템 구성
- Language
  - Java 17
- Application
  - Spring Boot
    - Web
    - Security
    - Batch
    - Data JPA
    - Data Redis Reactive
    - Validation
    - Actuator
    - Devtools
  - Spring Cloud
    - Config
    - Gateway
    - Kubernetes Discovery Client
    - Open Feign
    - Resilience4j
    - Kafka
  - Compile & Build
    - maven-compiler-plugin
    - jib-maven-plugin
  - Utility Belt
    - Google Guava
  - Document
    - SpringDoc
- Infrastructure
  - Kubernetes
  - Docker Compose
- Data Source
  - PostgreSQL
- Event Streaming
  - Kafka(with Zookeeper)

    ※ KRaft 방식으로 변경 예정

- Service Discovery
  - Kubernetes Discovery Server
- Authentication Server
  - KeyCloak
- Collect Observabilities
  - OpenTelemetry
  - Spring Actuator
  - Micrometer
  - Prometheus
  - Loki
  - Tempo
- Monitoring
  - Grafana
- Collect Observabilities + Monitoring + request/response Flow Control
  - Istio
- Kubernetes resources distributor
  - Helm
- Cloud Provider
  - GCP(Google Cloud Platform)

    ※ 가이드 작성 예정

  - AWS(Amazon Web Services)

    ※ 가이드 작성 예정

# 마이크로서비스 목록
- K8s 네임스페이스: `default`

| 마이크로서비스 | 컨테이너 포트 | K8s 클러스터 내 서비스 DNS | ClusterIP 포트 |
| --- | --- | --- | --- |
| batch | 9100 |  |  |
| member | 9200 |  |  |
| payment | 9300 |  |  |
| booking | 9400 |  |  |
| hotel | 9500 |  |  |
| flight | 9600 |  |  |
| car | 9700 |  |  |
| Grafana | 3000 |  |  |
| Config | 8071 |  |  |
| Gateway | 8072 | gateway-service |  |
| K8s Discovery Server | 8761 |  | 80 |
| KeyCloak | 8080(HTTP) / 8443(HTTPS) |  |  |
| Kafka Broker Cluster | 9092 | kafka-controller-0.kafka-controller-headless.default.svc.cluster.local | <none> |

# 아키텍처 구성 (쿠버네티스 기반)
- **Spring Cloud Gateway:** 스프링 클라우드 게이트웨이는 여기서 OAuth2 Client 및 OAuth2 Resource Server 역할을 합니다. 모든 요청은 게이트웨이에 전달되며, 요청을 다운스트림 서비스로 전달하기 전에 액세스 토큰을 확인합니다. 인증되지 않은 요청에 대해서는 Authentication Code Grant (OAuth2 인증 타입 중 하나) 흐름 절차로 Keycloak으로부터 새로운 토큰 발급을 위해 인증을 요청합니다. 또한 요청에 대한 안정성을 위해 Resilience4j 를 활용합니다. 게이트웨이 서버는 모든 서비스의 입구 역할을 하는데, 너무 많은 책임을 지게 하면 단일 실패 지점(Single Point of Failure)이 되기 쉬우므로 최대한 가볍게 유지하도록 합니다.
- **KeyCloak**: OAuth2 및 OpenID Connect 표준을 지원하는 인증 서버로서, 사용자 인증, 권한 부여, 토큰 발급 등을 담당합니다.
- **마이크로서비스**: 마이크로서비스는 게이트웨이를 통해서만 접근 가능합니다. Reservation 서비스는 내부적으로 여러 서비스를 호출하고 응답을 관리하기 때문에 요청에 대한 실패가 발생할 수 있으므로 이를 처리하기 위해 Circuit breaker, Retry 등의 패턴을 적용합니다.

  각 마이크로서비스마다 개별적인 인증 및 인가를 구현하는 것은 복잡하고 유지보수가 어렵습니다. 이를 해결하기 위해 OAuth2 및 OpenID Connect(OIDC) 표준을 지원하는 KeyCloak과 같은 통합 인증 서버를 게이트웨이 서버에 연동해 Resource Server 책임을 부여해 이곳에서 중앙집중식으로 인증 및 권한을 체크하도록 합니다.

- **데이터베이스 논리적 분리**: 이 프로젝트에서 데이터베이스 분리 전략은 논리적 분리로 합니다. 즉, 하나의 데이터베이스 인스턴스(물리 서버 또는 클라우드 DB)에 각 서비스별로 별도의 스키마를 생성해 데이터베이스를 논리적으로 분리합니다. 이는 소규모 프로젝트에서 운영 및 유지보수가 간편하고, 인프라 비용 절감 효과가 있기 때문입니다. 단점은 데이터베이스 인스턴스에 장애가 발생하면 모든 서비스가 영향을 받을 수 있다는 것입니다. 추후 서비스가 커지고 트래픽이 늘어나면 물리적 분리로의 전환을 권장합니다.
- **Resilience4j Bulkhead 패턴**: 리소스를 격벽의 형태로 나누어 격리해 일부 컴포넌트에서 문제가 발생했을 때 전체 시스템으로의 장애로 전파되지 않게 해주는 패턴입니다. 정리하면, **리소스 격리를 통한 장애 격리**라고 할 수 있습니다.
- **Resilience4j Circuit breaker 패턴**: 서비스 요청 실패 발생 시 새로 들어오는 요청을 제한해 서버가 회복하는데 시간을 벌어다 줄 수 있습니다. 이를 통해 얻는 이점은 다음과 같습니다.
  - Fail fast
  - Fail gracefully
  - Recover seamlessly
- **Resilience4j Fallback 패턴**: 요청 실패에 대한 대안으로 Fallback 경로로 전환합니다.
- **Resilience4j Retry 패턴**: 서비스에 잠시 장애가 발생했을 때 요청 실패 시 요청을 재시도 합니다.
- **Resilience4j Rate limit 패턴**: 특정 시간 구간 내에 들어오는 요청의 수를 제한합니다. 짧은 시간 내 과도한 요청을 보내는 클라이언트를 제한 할 수 있습니다.
- **Aggregation 패턴**: 특정 서비스에서 여러 다른 서비스나 저장소로부터 데이터를 모으는 패턴입니다.
- **Observability(Spring Actuator + Micrometer: metrics, Loki)**
  - **Spring Actuator + Spring Micrometer**: 각 마이크로서비스 내 메트릭을 수집합니다. Spring Micrometer는 벤더 중립적인 API 인터페이스를 사용해 코드 실행 시간, 호출 추적을 할 수 있도록 해주고, 이 메트릭 정보를 외부 모니터링 시스템으로 보내줍니다.
  - OpenTelemetry: 각 마이크로서비스에서 추적 가능한 로그를 생성하고 Loki, Tempo로 전달합니다. 메트릭은 Spring Actuator + Micrometer 에서 수집하므로 OpenTelemetry는 로그 관련 책임만 갖도록 합니다.
  - Grafana: metrics, tracing, logs 를 모니터링하고, 알람을 받아볼 수 있는 다재다능한 도구입니다.

# 사전 설치 리스트
- JDK 17
- Maven
- Docker Desktop + Kubernetes Cluster 활성화
- Kubectl

# Kubernetes 클러스터에 배포해 프로젝트 시작하기

## 1) Secret 생성

```bash
# PROD
$ kubectl create secret generic prod-traveladvisor-postgres-secret \
  --from-literal=datasource_postgres_url='jdbc:postgresql://postgres:5432/postgres?binaryTransfer=true&reWriteBatchedInserts=true&stringtype=unspecified' \
  --from-literal=datasource_postgres_username='prod-admin' \
  --from-literal=datasource_postgres_password='prod-admin'

# QA
$ kubectl create secret generic qa-traveladvisor-postgres-secret \
  --from-literal=datasource_postgres_url='jdbc:postgresql://postgres:5432/postgres?binaryTransfer=true&reWriteBatchedInserts=true&stringtype=unspecified' \
  --from-literal=datasource_postgres_username='qa-admin' \
  --from-literal=datasource_postgres_password='qa-admin'

# DEV
$ kubectl create secret generic dev-traveladvisor-postgres-secret \
  --from-literal=datasource_postgres_url='jdbc:postgresql://postgres:5432/postgres?binaryTransfer=true&reWriteBatchedInserts=true&stringtype=unspecified' \
  --from-literal=datasource_postgres_username='dev-admin' \
  --from-literal=datasource_postgres_password='dev-admin'

# 생성 확인  
$ kubectl get secret | grep traveladvisor-postgres-secret
prod-traveladvisor-postgres-secret                                  Opaque               3      26s
qa-traveladvisor-postgres-secret                                    Opaque               3      21s
dev-traveladvisor-postgres-secret                                   Opaque               3      12s
```

## 2) KeyCloak 배포

```bash
# KeyCloak 배포
# localhost:80으로 접속 가능합니다.
$ helm install keycloak keycloak
# 실행 후 다음의 코드를 실행해 엔트리포인트를 얻습니다.
export HTTP_SERVICE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[?(@.name=='http')].port}" services keycloak)
export SERVICE_IP=$(kubectl get svc --namespace default keycloak -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
echo "http://${SERVICE_IP}:${HTTP_SERVICE_PORT}/"
# 다음의 코드를 입력해 ID, PWD 를 얻습니다.
echo Username: user
echo Password: $(kubectl get secret --namespace default keycloak -o jsonpath="{.data.admin-password}" | base64 -d)
# 엔트리포인트: localhost:80
# Username: user
# Password: password
# localhost:80 에 접속해 위 정보를 입력해 로그인 합니다.
```

다음의 KeyCloak 설정을 임포트 합니다.

[realm-export.json](realm-export.json)

## 3) Kafka 배포 (with Zookeeper)

```bash
$ cd infrastructure/helm
$ helm install confluent cp-helm-charts --version 0.6.0
```

브로커 replicaCount는 3개로 설정했습니다. 브로커 DNS와 포트는 다음과 같습니다.

- `confluent-cp-kafka:9092`

## 4) Prometheus 배포

```bash
$ cd infrastructure/helm
$ helm install prometheus kube-prometheus
```

디폴트로 ClustIP로 생성되므로 외부에서 직접적인 접근은 불가능하지만, 다음의 명령을 통해 임시로 접근 가능하도록 할 수 있습니다.

```bash
echo "Prometheus URL: http://127.0.0.1:9090/"
    kubectl port-forward --namespace default svc/prometheus-kube-prometheus-prometheus 9090:9090
```

## 5) Grafana Loki 배포

```bash
$ cd infrastructure/helm
$ helm install loki grafana-loki
```

## 6) Grafana Tempo 배포

```bash
$ cd infrastructure/helm
$ helm install tempo grafana-tempo
```

## 7) Grafana 배포

```bash
$ cd infrastructure/helm
$ helm install grafana grafana
```

Grafana는 디폴트로 ClusterIP 서비스 타입으로 배포됩니다. 만약 브라우저로 접속해 모니터링이나 디버깅을 해야 하는 경우 다음을 수행합니다.

```bash
echo "Browse to http://127.0.0.1:3000"
kubectl port-forward svc/grafana 3000:3000
```

아이디와 비밀번호를 알기 위해 다음을 입력합니다.

```bash
$ echo "User: admin"
  echo "Password: $(kubectl get secret grafana-admin --namespace default -o jsonpath="{.data.GF_SECURITY_ADMIN_PASSWORD}" | base64 -d)"
```

## 8) Kubernetes Discovery Server 배포

```bash
$ cd infrastructure/kubernetes
$ kubectl apply -f kubernetes-discoveryserver-deployment.yml
```

## 9) Skaffold 로 아주 쉽게 마이크로서비스 배포 (🥲정말 감동적인 도구🥲)
<p align="center">
  <img src="https://media1.giphy.com/media/F3O8iAVrKgiR6QtgnE/giphy.gif?cid=7941fdc6kwhets7tqiro7l44okmdi8xlh6qhr51cwjd7ccsn&ep=v1_gifs_search&rid=giphy.gif&ct=g" alt="So gooooooood" />
</p>

구글 선생님께서 만드신 Skaffold는 코드 변경 사항을 감지하고 자동으로 빌드, 푸쉬 및 배포해주는 도구입니다. 로컬 환경에서 Kubernetes 애플리케이션의 반복적인 테스트를 간단히 수행할 수 있습니다. 각 마이크로서비스 내에 Jib 의존성을 갖고 있으며, 이는 빌드 시 자동으로 도커 이미지를 만들어줍니다. skaffold.yaml 에서 또한 Jib를 통해 빌드 하도록 설정했으며, Helm을 통한 배포를 수행하도록 자동화했습니다. 따라서 손쉽게 다음의 명령을 수행하면 빌드, 도커 이미지 생성, Helm을 통한 배포가 한 방에 이루어집니다!

```bash
# skaffold를 먼저 다운로드 받으신 후 프로젝트 루트에서 다음을 수행해주세요.

# 프로젝트 첫 실행이라면 먼저 JAR 파일을 로컬 Maven Repository에 생성해야 합니다.
$ mvn clean install

# 이후부터는 다음의 명령만 수행하면 됩니다.
$ skaffold dev
```

기본적으로 모든 마이크로서비스 로그가 하나의 터미널에서 표시됩니다.

### 9-1) 쿠버네티스 클러스터에 배포된 서비스 디버깅 하기 (🥲더 감동적인 도구🥲)

그런데, 호스트 PC에서 어떻게 쿠버네티스 클러스터에 배포된 Pod의 컨테이너 내 자바 애플리케이션 디버깅을 할까요?

Skaffold는 디버깅을 위해 Pod를 재구성하고, 포트를 해당 Pod로 포워딩 해주는 역할을 합니다. 그리고 Intellij, VS Code, Cloud Code IDE에 Cloud Code 플러그인을 사용하면 로컬 호스트 PC에서 디버깅 하는 것과 같은 효과를 낸다고 합니다. 즉, 프로세스간 통신 사이에 디버깅 소통 규격으로 JDWP를 준수하는 기능을 넣어 놓은 것으로 보입니다. (런타임: JVM, 프로토콜: JDWP)

Intellij IDE 를 대상으로 설명하겠습니다. Cloud Code 플러그인이 설치되어 있어야 합니다.

1. Run/Debug Configurations 창에 접속합니다.
2. Add New Configuration → Cloud Code: Kubernetes를 선택합니다.
3. 다음과 같이 설정한 후 Apply → OK 버튼을 클릭합니다.

   ![image 4.png](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/66656689cc7389d3ea2a517cfe3b92e23d6b5ca5/image%25204.png)
   ![image 5.png](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/66656689cc7389d3ea2a517cfe3b92e23d6b5ca5/image%25205.png)

4. Debug 버튼을 클릭하고 정상 동작을 확인합니다.

   ![image.png](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/672bfd9cf048b8a700aeec9fe979382aac35e196/image%25206.png)

   이제 IDE에서 디버그 모드가 동작합니다. 👍

## 10) 쿠버네티스 클러스터에 배포된 전체 리소스 보기

```bash
# helm으로 배포된 리소스 보기
$ helm ls

# Service 보기. gatewayserver의 경우 서버 타입이 LoadBalancer 이므로
# EXTERNAL-IP가 할당될 때 까지 기다려야 합니다. <pending> 상태가 아닐 때 외부에서 접근 가능합니다.
$ kubectl get svc -w

# Pod 보기
$ kubectl get po
```

Intellij를 사용 중이시라면 Google Cloud Code 플러그인을 설치하면 리소스를 에디터에서 GUI로 손쉽게 모니터링하고 관리할 수 있습니다.

# 도커 이미지 생성 및 도커 허브에 푸쉬하기

만약 서버 소스 코드 수정 후 각 이미지를 다시 생성하고 배포하려면 먼저 도커 허브에 public 저장소를 생성한 후 다음의 절차를 따라주세요.

```bash
# 모든 마이크로서비스 JAR 파일 생성 및 도커 이미지 생성
$ mvn clean install

# 이미지 생성 확인
$ docker images | grep com.traveladvisor

# 태그 만들기
$ docker tag {LOCAL_DOCKER_IMAGE_NAME}:{LOCAL_TAG_NAME} {DOCKER_HUB_REPOSITORY_NAME}:{DOCKER_HUB_TAG_NAME}
# e.g., config-service 이미지에 대해 태그 생성 예시는 다음과 같습니다.
$ docker tag com.traveladvisor/configserver:1.0.0-SNAPSHOT sunhyeok/com.traveladvisor:configserver-1.0.0-SNAPSHOT

# 도커 허브에 업로드
$ docker push {DOCKER_HUB_REPOSITORY_NAME}:{DOCKER_HUB_TAG_NAME}
# e.g., config-service 이미지 배포 예시는 다음과 같습니다.
# 다음은 도커 허브 저장소 sunhyeok/com.traveladvisor 에 config-service-1.0.0-SNAPSHOT 라는 태그 이름으로 푸쉬됩니다.
$ docker push sunhyeok/com.traveladvisor:configserver-1.0.0-SNAPSHOT

# infrastructure/microservices/{MICROSERVICE_NAME}/ 폴더로 이동 후
$ helm dependencies build

# infrastructure/environments/{PROFILE}/ 폴더로 이동 후
$ helm dependencies build

$ cd infrastructure/environments/
$ helm upgrade traveladvisor prod-env # 실행하고자 하는 프로파일 지정

# Pod 실행 및 컨테이너 Running 상태 확인
$ kubectl get po -w
```

※ 도커 이미지 이름에 “com.traveladvisor” 가 포함되는 모든 이미지 제거하는 방법: `docker images | grep com.traveladvisor | awk '{print $3}' | xargs docker rmi -f`

※ 개발을 진행하다보면 정말 많은 이미지 생성 및 제거가 발생하고, 이로 인해 종종 현재 코드와 빌드된 이미지 코드 간의 캐시가 일치하지 않는 정합성 문제가 발생할 수 있습니다. 이럴 땐 Maven 캐시를 제거하는 것을 권장합니다. Maven 캐시 제거 방법:

- Windows: `rd /s /q C:\Users\{사용자_이름}\.m2\repository`
- Linux: `rm -rf ~/.m2/repository`

# 로컬 개발 시 빠르게 소스 코드 반영 보고 싶은 경우 각 마이크로서비스 로컬 실행

게이트웨이 서버와 상관 없는 비즈니스 코어 로직을 개발할 때에는 Skaffold 도움 조차 필요 없을 수 있습니다. 이럴 때는 로컬에서 마이크로서비스를 바로 실행해 기민하게 개발할 수 있어야 합니다. 따라서 각 마이크로서비스의 소스 코드가 즉각 반영될 수 있도록 DevTools 의존성을 추가했습니다.

```yaml
<profiles>
	<!-- 개발 환경(default) 프로파일일 경우 DevTools 의존성을 포함시킵니다. 즉, 개발 환경에서만 Hot Reload 기능을 사용하도록 합니다. -->
	<profile>
		<id>default</id>
		<activation>
			<property>
				<name>spring.profiles.active</name>
				<value>default</value>
			</property>
		</activation>
		<dependencies>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-devtools</artifactId>
				<scope>runtime</scope>
				<optional>true</optional>
			</dependency>
		</dependencies>
	</profile>
</profiles>
```

인텔리제이 사용자의 경우 Settings → Build, Excecution, Deployment → Compiler → Build project automatically 체크 박스를 체크하셔야 합니다.

# 로컬 개발 환경에서의 리소스

쿠버네티스 클러스터 환경이 아닌 로컬 PC에서 마이크로서비스 인스턴스만 띄워 개발할 때 Redis, Kafka, PostgreSQL등의 리소스가 필요할 수 있습니다. 각 리소스는 infrastructure/local-resources 폴더에 정의돼 있으며 Docker Compose로 띄울 수 있습니다.

다음의 명령을 실행합니다.

```bash
$ cd infrastructure/docker-compose/local-resources
$ docker compose up -d
```

# 새로운 마이크로서비스 추가 시 체크리스트

1. 새로운 마이크로서비스의 pom.xml 에 다음을 추가 합니다.

    ```yaml
    <!-- 용도: Config Server의 설정을 주입받기 위해 사용 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    
    <!-- 용도: K8s의 Discovery Service에게 탐지되기 위한 Discovery Client 사용 -->
    <dependency>
    		<groupId>org.springframework.cloud</groupId>
    		<artifactId>spring-cloud-starter-kubernetes-discoveryclient</artifactId>
    </dependency>
    ```

2. config-service 마이크로서비스의 resources/configs 하위에 생성한 마이크로서비스 애플리케이션 이름으로 파일을 생성합니다. config를 주입 받아야 하는 경우에만 이 단계를 수행해주세요.
3. infrastructure/microservices 에 헬름 차트를 생성합니다.
4. infrastructure/environments/{PROFILE}/Chart.yaml 의 dependencies에 의존을 추가합니다.
5. infrastructure/kube-prometheus/values.yaml 의 additionalScrapeConfigs.internal.jobList에 마이크로서비스 metrics 수집 대상 엔드포인트를 추가합니다. helm 리소를 수정했으므로 반드시 `helm dependencies build` 명령을 수행해야 합니다.
6. skaffold.yaml 파일 내 build.artifacts, deploy.helm.releases 에 새로운 마이크로서비스 내용을 추가합니다.

# Keycloak 인증 서버 설정

1. Keycloak 설치 및 실행
  - [2) Keycloak 배포](https://github.com/SunhyeokChoe/traveladvisor-msa?tab=readme-ov-file#2-keycloak-%EB%B0%B0%ED%8F%AC) 의 가이드에 따라 컨테이너를 띄웁니다. 공식문서의 경우 [여기](https://www.keycloak.org/getting-started/getting-started-docker)를 참조해 주세요.
  - 포트는 `8080`이며, 개발모드(start-dev)는 최초 시작 시 자동으로 관리자 계정을 생성합니다.
2. Realm 설정
  - ※ real-export.json 내용을 통해 손쉽게 import 해도 됩니다. Create realm 단계에서 Browse 를 클릭해 import 할 수 있습니다.
  - Realm은 보안 영역을 나타내며, 새로운 Realm을 생성합니다. 기본으로 `master` Realm이 생성되어 있으며, 이는 다른 Realm 을 관리하는데 사용됩니다.
  - Realm name을 `traveladvisor`로 생성합니다.
3. `traveladvisor` realm에 OpenID Connect 클라이언트 생성
   - External Service 혹은 API로부터 게이트웨이로 들어오는 요청 검증 → Client Credentials Grant Type 설정
     - Client authentication: ON 설정: 인증 값을 클라이언트가 자체 소유 가능하도록 합니다.
     - Authentication flow: Service accounts roles 만 체크합니다. 이는 Client Credentials Grant 를 의미합니다.

      User가 scope를 갖고 있어야 해당 스코프에 접근 가능하듯이 Client 또한 scope를 가져야 해당 스코프에 접근 가능합니다. Client > Client scopes 탭에 접속해 할당할 수 있습니다.

   - 외부 클라이언트(웹 브라우저, 모바일)로부터 게이트웨이로 들어오는 요청 검증 → Authorization Code Grant Type 설정
     - Client authentication: ON 설정: 인증 값을 클라이언트가 자체 소유 가능하도록 합니다.
     - Authentication flow: Standard flow 만 체크합니다. 이는 Authorization Code Grant 를 의미합니다.
     - Valid redirect URIs: *로 설정합니다. 이 데모 프로젝트는 아직 UI를 제공하지 않기 때문입니다. 하지만 프로덕션 환경에서는 *가 아닌 반드시 우리 도메인의 URI로 명시해주어야 합니다. *로 설정하면 해커가 액세스 토큰을 자신의 웹사이트로 전달할 가능성이 있기 때문입니다.
     - Valid post logout redirect URIs: 공백으로 설정합니다.
     - Web origins: *로 설정합니다. 이 또한 데모를 위해 느슨하게 설정했으며, 실제로는 모든 오리진을 허용하면 안됩니다.

     이 요청 흐름은 외부 퍼블릭 클라이언트와의 인증/인가 흐름이므로 Users에서 유저를 생성해야 합니다.
4. 게이트웨이 애플리케이션 설정
   ```yaml
    security:
      oauth2:
        resourceserver:
          jwt:
            jwk-set-uri: "http://localhost:7080/realms/master/protocol/openid-connect/certs"
   ```
5. Client 테스트를 위한 Client scopes 추가

   Client scopes → Create client scope에서 다음과 같이 설정합니다.
  - Name: TEST
  - Type: None
  - Protocol: OpenID Connect

6. gateway 클라이언트에 TEST 스코프 추가

   Clients → Client scopes → TEST 스코프를 추가하고 추가된 TEST 스코프를 확인합니다.

# OAuth2를 지원하는 KeyCloak 기반 Spring Cloud Gateway 구성
게이트웨이 서버는 모든 요청을 받는 엣지 포인트가 되므로 리소스 서버와 인증/인가 확인 책임을 갖도록 했습니다. 즉, 인바운드 요청에 대해 사용자가 회원가입/로그인은 했는지, 해당 리소스에 권한이 있는지 체크합니다. 이러한 책임을 구현하기 위해 다음의 종속성으로 구성됩니다.

- `spring-cloud-starter-gateway`: Spring Cloud 기반 게이트웨이 서버
- `spring-security-oauth2-resource-server`: OAuth2 리소스 서버
- `spring-boot-starter-security`: 보안
- `spring-security-oauth2-jose`: JWT 토큰 파싱

인증 서버로 KeyCloak을 사용합니다. 이는 웹 클라이언트/모바일 클라이언트/API 등의 다양한 엔드 유저의 요청을 게이트웨이 서버로부터 전달 받아 인증을 처리하고, OAuth 2.0 + OIDC에 기반한 JWT 토큰을 발행하는 역할을 합니다.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-oauth2-resource-server</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-oauth2-jose</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>com.github.dasniko</groupId>
  <artifactId>testcontainers-keycloak</artifactId>
  <version>3.2.0</version>
 <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>1.19.6</version>
  <scope>test</scope>
</dependency>
```

# Saga 패턴

Saga는 마이크로서비스 전반에 걸쳐 분산된 장기 실행 트랜잭션(LLT, Long Lived Transactions)을 의미하며, 기본 아이디어는 LLT를 완료하기 위해 서비스 전반에 걸쳐 로컬 ACID 트랜잭션 체인을 만드는 것입니다.

이 데모 프로젝트에서 여행 예약 과정을 하나의 논리적인 LLT로 묶어 처리합니다. 그러기 위해선 `예약 생성 → 호텔 예약 → 항공권 예약 → 차량 예약 → 예약 승인 응답` 이라는 일련의 절차가 필요합니다. 이 유즈케이스를 충족시키기 위해 카프카를 이벤트 버스로 사용해 Saga 패턴을 구현합니다.

Saga 패턴을 조정(Coordinate)하기 위해 Reservation 마이크로서비스를 Orchestrator로 사용합니다.

