# 트래블어드바이저(traveladvisor) — Microservices with Spring Cloud Gateway, OAuth2, Keycloak, Kafka, Debezium CDC and PostgreSQL

<br><br>

# 이 프로젝트를 만든 이유
<p align="center">
  <img src="https://media4.giphy.com/media/8dYmJ6Buo3lYY/giphy.gif?cid=7941fdc6vac0eapvo79w9tgrd48b8a9thrpwqw27t6rv76ty&ep=v1_gifs_search&rid=giphy.gif&ct=g" alt="baby trump" />
</p>



이 프로젝트는 MSA + 쿠버네티스 환경에서 DDD와 Hexagonal Architecture를 결합 한다고 했을 때 어떻게 구성되고 관리되어야 하는지에 대한 호기심에서 시작되었습니다. 작은 규모의 프로젝트에서는 모놀리식 아키텍처보다 구성과 관리의 복잡도가 높아지고, 러닝 커브가 높아 개발 속도가 느려질 수 있다는 점이 단점으로 올 수 있지만, 대규모 개발 환경에서는 조직 구조, 팀원 구성, 가용 가능 인원, 그리고 회사의 여유 자금 등 다양한 요인에 따라 MSA가 적합한 프로젝트 구조가 될 수 있다고 합니다.

진행하다 보니 Saga + Outbox 패턴을 알게 돼 공부해 데모로 적용해보기도 하고, 이와 더불어 DDD와 쿠버네티스 학습도 병행하기 위해 **정말 많은 레퍼런스**를 찾아가며 만들었습니다. 진행하면서 Saga, Outbox, DDD의 장점과 쿠버네티스의 높은 편의성에 대해 어느 정도 알게 되었습니다. 다만 Hexagonal 패턴.. 은 복잡성을 고려했을 때, 앞으로 진행할 사이드 프로젝트에는 적합하지 않을 것 같아 적용을 보류할 생각입니다. 이 프로젝트를 완성하기 위해 온종일 베이스 모듈만 만들다가 Git 워킹 디렉토리를 살펴보면 80개 파일의 코드에 수정 사항이 있다고 뜨기도 합니다. 이는 Adapter, Application, Domain Core가 추상화 레이어를 통해 철저히 나뉘어 각 계층 간 격벽 역할을 훌륭히 수행하고 있다는 증거이기도 하지만, 작은 규모에서는 뚜렷한 목표가 없으면 복잡도만 증가하고 유지보수와 개발 속도에 유의미한 효과는 없어 보이기도 합니다. 구역별 잘 나누어진 인터페이스로 인해 테스트를 적용하기 좋기는 하지만, 사실 이게 비즈니스를 해결하기 위한 코드가 아닌 아키텍처를 위한 코드를 짜고 있는 것 같은 착각이 들기도 합니다. 다만, 팀이 잘 나누어져 있고, 팀원들이 패턴에 대한 이해도가 동등하게 있으며, 각 팀별로 도메인을 맡아 처리할 수 있는 환경이라면 도입해보는 것도 좋아 보입니다.

마지막으로 이 프로젝트를 진행한 이유 중 하나는 과거 거대 버티컬 이커머스 프로젝트를 진행하면서 쿠버네티스를 처음 도입해 봤는데, 그때 느꼈던 쿠버네티스에 대한 지식의 부족함을 보완하고, 이번 기회에 다루는 방법에 관해 더욱 성숙해지는 계기를 만들고 싶었습니다.


<br>

# 프로젝트 설명
해외 여행을 계획할 때 가장 먼저 하는 일은 목적지에 도착하기 위한 항공권 예약입니다. 항공권 예약 이후에는 여행의 중심 거점이 될 호텔을 예약합니다. 호텔을 기준으로 다양한 액티비티를 즐기기 위해 여행 일정을 구성하고, 장거리 이동이나 현지 교통 편의를 위해 차량 예약을 진행하는 경우도 많습니다.

이 프로젝트는 이러한 해외 여행의 필수 요소인 항공권, 호텔, 차량 예약을 하나의 플랫폼에서 손쉽게 해결할 수 있는 올인원 예약 서비스를 제공합니다. 사용자는 별도의 서비스 간 이동 없이, 한 번의 로그인으로 통합된 예약 시스템을 경험할 수 있습니다.

프로젝트의 핵심은 개별 도메인 로직 구현보다는, 이를 뒷받침하는 MSA(Microservice Architecture)의 설계 및 구현에 중점을 두고 있습니다. 특히, 각 예약 서비스가 독립적으로 동작하면서도 서로 유기적으로 협력할 수 있도록 유연하고 확장 가능한 아키텍처를 목표로 하고 있습니다.

### 프로젝트 주요 특징

1. 통합 예약 플랫폼

   - 항공권, 호텔, 차량 예약을 하나의 시스템에서 처리
   - 사용자의 시간과 노력을 절약하며 예약 과정의 편의성을 극대화
   - 예약 상태 추적, 취소 등 관리 기능 제공

2. MSA 아키텍처

   - 각 도메인(항공권, 호텔, 차량)이 독립적인 마이크로서비스로 구성
   - 서비스 간 데이터 연동과 트랜잭션 관리에 Saga 패턴과 Outbox 패턴 적용
   - 확장성과 장애 격리를 위한 쿠버네티스 기반의 배포 환경 지원

<br>

# MVP 구현 목표

## 1) MVP ver.1
- 간단한 호텔/항공권/차량 예약 시스템을 만듭니다.
- 코어 도메인 설계는 DDD 기반으로 합니다.
- Hexagonal Architecture를 도입해 외부 요인과의 강결합을 없애고, 코어 도메인과 서비스 레이어를 분리해 도메인을 보호합니다.
- 모든 애플리케이션과 리소스는 쿠버네티스 클러스터에 등록합니다.
- Batch, Member, Booking, Payment, Hotel, Flight, Car, Config, Gateway 마이크로서비스를 구성합니다.

  Hotel, Flight, Car 에 관한 테스트 정보를 얻기 위한 OTA* 벤더로 Amadeus를 사용합니다.

  ※ *OTA: OTA는 Online Travel Agency의 약자로, 온라인 여행 예약 대행 서비스를 의미합니다. Agoda와 Expedia가 대표적인 OTA로 잘 알려져 있습니다.

- Spring Cloud Gateway 를 사용해 게이트웨이 엣지 서버를 구성합니다.
- KeyCloak을 활용해 OAuth2 인증을 수행합니다. 인증 서버는 KeyCloak 서버이고, Gateway 서버가 리소스 서버입니다.
- 회복력과 가용성을 위해 Resilience4j를 적용합니다. 이는 서비스 내 리소스의 적절한 격리, DDos와 같은 brute force 공격 예방 등에 도움을 줍니다.
- 각 마이크로서비스를 추적하고 관리하기 위해 Observability(metrics, tracing, logs) 데이터를 지속해서 수집하고 Grafana에서 모니터링 합니다. merics의 경우 Spring Actuator + Micrometer를 통해 Prometheus가 이해할 수 있는 JSON 포맷으로 제공하고, Grafana에서 모니터링 및 쿼리 가능합니다. logs의 경우 데이터 소스로 Loki를 사용하고, 이 또한 Grafana와 통합했습니다. Tracing의 경우 다음의 두 가지 방식을 제공합니다.
    1. 각 마이크로서비스에 OpenTelemetry Java Agent 의존성을 추가하고 Exporter의 도움을 받아 분산 추적 데이터를 자동으로 Loki, Tempo에 보냅니다. 추적을 위해 OpenTelemetry 를 사용해 로그 Prefix 포맷을 Service Name, Trace ID, Span ID 형식으로 구조를 잡고, Grafana Loki + Tempo 에서 이를 시각화 합니다.
    2. 쿠버네티스 클러스터에서 Observability 추적이 필요한 Pod 내부에 Envoy proxy를 Sidecar로 마이크로서비스와 함께 배포하고, 각 Pod 간의 East-West API 요청/응답은 이 Envoy proxy를 통해 진행합니다. Istio를 사용해 각 Envoy proxy를 관리하고, 모니터링 합니다. 이 방식은 마이크로서비스에는 비즈니스 로직만 존재하고, 지표 수집은 분리된 Sidecar를 통해 진행하므로 [a] 방식에 비해 인프라 관리에 용이하고, 개발자는 비즈니스 코어에 집중하기 좋은 환경이 구성됩니다.
- 호텔/항공권/차량 예약 가능(도메인 용어로 Availibility 라고 합니다.) 목록은 Amadeus의 테스트 전용 API를 Spring Batch 애플리케이션을 통해 호출하여 적재합니다. 이미 이 작업은 선행했으므로 가데이터는 SQL 파일로 제공합니다. 따라서 따로 구동하지 않으셔도 됩니다.

## 2) MVP ver.2
- 기존의 카프카 처리 방식을 Spring Cloud 기반으로 변경합니다.

  Zookeeper, Spring Kafka → KRaft, Spring Cloud Function, Spring Cloud Stream(with Kafka)

- 테스트 호텔 예약 가능(Availibility) 데이터를 기반으로 Hotel, Flight , Car 가데이터를 DB에 지속해서 생성하고 업데이트 합니다.
- Hotel, Flight , Car 데이터를 Elasticsearch에 색인하고, 검색될 수 있도록 합니다.
- 글로벌 캐시 클러스터를 구성하고 배치 업데이트를 통해 Hotel, Flight , Car 예약 데이터가 실시간으로 업데이트 되도록 합니다.
- 외부 업체가 Hotel, Flight , Car 데이터를 요청하는 즉, 트래블어드바이저가 공급업체가 되는 상황을 모사하기 위해 Open API 엔드포인트와 게이트웨이를 구현합니다.
    - REST API를 Hateoas 규정에 맞게 구현합니다.
    - 게이트웨이에 들어오는 요청 중 외부 업체의 등급에 따라 Rate Limit 제한을 동적으로 조율합니다.

      e.g., 높은 등급 일 경우 느슨하게, 낮은 등급 일 경우 타이트하게 제한합니다.

## 3) MVP ver.3
- CI/CD 파이프라인을 구성합니다. (Github Actions 테스트/빌드 -> GKE 배포)
- React Native로 모바일 앱을 구현합니다.

<br>

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

<br>

# 애플리케이션 목록
- K8s 네임스페이스: `default`

| **애플리케이션**           | **컨테이너 포트**             |
|----------------------|-------------------------|
| batch                | 9100                    |
| member               | 9200                    |
| payment              | 9300                    |
| booking              | 9400                    |
| hotel                | 9500                    |
| flight               | 9600                    |
| car                  | 9700                    |
| grafana              | 3000                    |
| config               | 8071                    |
| gateway              | 8072                    |
| K8s Discovery Server | 8761                    |
| KeyCloak             | 8080(HTTP) / 8443(HTTPS) |
| Kafka Broker Cluster | 9092                    |

<br>

# 아키텍처 구성 (쿠버네티스 기반)
- **Spring Cloud Gateway:** 스프링 클라우드 게이트웨이는 여기서 OAuth2 Client 및 OAuth2 Resource Server 역할을 합니다. 모든 요청은 게이트웨이에 전달되며, 요청을 다운스트림 서비스로 전달하기 전에 액세스 토큰을 확인합니다. 인증되지 않은 요청에 대해서는 Authentication Code Grant (OAuth2 인증 타입 중 하나) 흐름 절차로 Keycloak으로부터 새로운 토큰 발급을 위해 인증을 요청합니다. 또한 요청에 대한 안정성을 위해 Resilience4j 를 활용합니다. 게이트웨이 서버는 모든 서비스의 입구 역할을 하는데, 너무 많은 책임을 지게 하면 단일 실패 지점(Single Point of Failure)이 되기 쉬우므로 최대한 가볍게 유지하도록 합니다.
- **KeyCloak**: OAuth2 및 OpenID Connect 표준을 지원하는 인증 서버로서, 사용자 인증, 권한 부여, 토큰 발급 등을 담당합니다.
- **마이크로서비스**: 마이크로서비스는 게이트웨이를 통해서만 접근 가능합니다. Booking 서비스는 내부적으로 여러 서비스를 호출하고 응답을 관리하기 때문에 요청에 대한 실패가 발생할 수 있으므로 이를 처리하기 위해 Circuit breaker, Retry 등의 패턴을 적용합니다.

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

<br>

# 사전 설치 리스트
- JDK 17
- Maven
- Docker Desktop + Kubernetes Cluster 활성화
- Kubectl

<br>

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

<br>

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

<br>

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

<br>

# 로컬 개발 환경에서의 리소스

쿠버네티스 클러스터 환경이 아닌 로컬 PC에서 마이크로서비스 인스턴스만 띄워 개발할 때 Redis, Kafka, PostgreSQL등의 리소스가 필요할 수 있습니다. 각 리소스는 infrastructure/local-resources 폴더에 정의돼 있으며 Docker Compose로 띄울 수 있습니다.

다음의 명령을 실행합니다.

```bash
$ cd infrastructure/docker-compose
$ docker compose up -d
```

<br>

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

<br>

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
            jwk-set-uri: "http://localhost:8080/realms/master/protocol/openid-connect/certs"
   ```
5. Client 테스트를 위한 Client scopes 추가

   Client scopes → Create client scope에서 다음과 같이 설정합니다.
  - Name: TEST
  - Type: None
  - Protocol: OpenID Connect

6. gateway 클라이언트에 TEST 스코프 추가

   Clients → Client scopes → TEST 스코프를 추가하고 추가된 TEST 스코프를 확인합니다.

<br>

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

<br>

# Saga 패턴

Saga는 마이크로서비스 전반에 걸쳐 분산된 장기 실행 트랜잭션(LLT, Long Lived Transactions)을 의미하며, 기본 아이디어는 LLT를 완료하기 위해 서비스 전반에 걸쳐 로컬 ACID 트랜잭션 체인을 만드는 것입니다.

이 데모 프로젝트에서 여행 예약 과정을 하나의 논리적인 LLT로 묶어 처리합니다. 그러기 위해선 `예약 생성 → 호텔 예약 → 항공권 예약 → 차량 예약 → 예약 승인 응답` 이라는 일련의 절차가 필요합니다. 이 유즈케이스를 충족시키기 위해 카프카를 이벤트 버스로 사용해 Saga 패턴을 구현합니다.

Saga 패턴을 조정(Coordinate)하기 위해 Booking 마이크로서비스를 Orchestrator로 사용합니다.

<br>

# Outbox 패턴

Outbox 없이 Saga로만 구현한 경우를 살펴봅시다. 장기 실행 트랜잭션이 있는 Saga 패턴에는 로컬 데이터베이스 트랜잭션과 이벤트 게시 및 소비 작업이 있습니다. 로컬 데이터베이스에 트랜잭션을 커밋한 후 이벤트를 게시 한 상황에서 게시 된 작업이 실패하면 Saga는 정상적으로 진행될 수 없습니다. 그리고 시스템은 일관성 없는 상태로 남게 됩니다. 이를 해결해보려고 반대로 먼저 이벤트를 게시한 다음 로컬 데이터베이스에 트랜잭션을 커밋하려고 하면 로컬 데이터베이스 트랜잭션이 실패할 수 있게 되고, 이 경우 게시하지 말아야 할 잘못된 이벤트를 이미 게시했기에 상황은 더 나빠질 뿐입니다.

이 문제를 해결하려면 Saga와 Outbox 패턴을 결합하여 일관된 시스템 상태를 만들 수 있는 솔루션을 만들어야 합니다.

Outbox 패턴은 로컬 ACID 트랜잭션을 사용하여 일관된 분산 트랜잭션을 생성합니다. 이 패턴은 일관된 방식으로 Saga 패턴을 완성하는데 도움을 줍니다. 즉, Saga와 상호 협력 관계입니다. Outbox 패턴을 정리하면 다음과 같습니다.

- Saga 패턴과 결합 시 Saga 상태를 일관되게 만드는 효과가 있습니다.
- 비즈니스 로직과 이벤트 생성 작업을 하나의 로컬 트랜잭션으로 묶어 강력한 일관성을 제공합니다.
- 나중에 게시할 도메인 이벤트를 로컬 데이터베이스에 보관하는 데 사용됩니다.
- Outbox 테이블을 사용하면 멱등성(여러 번 실행해도 같은 결과를 반환)을 보장하고 동시 작업으로 인한 데이터 손상을 방지합니다.
- Outbox 테이블을 사용하면 낙관적 락 및 데이터베이스 제약 조건을 적용할 수 있습니다.
- 이벤트 퍼블리싱 부분을 구현하는 방법으로는 CDC(Change Data Capture)와 Pulling Outbox Table 두 가지가 있습니다.
  - **CDC(Change Data Capture)**: Outbox 테이블의 트랜잭션 로그는 파일로 남습니다. 이 로그를 활용해 구현합니다.
  - **Pulling Outbox Table**: 스케줄러를 주기적으로 이벤트들을 쿼리해 구현합니다.

Outbox 패턴을 구현할 때 중요한 점은 이벤트를 직접 게시하는 게 아닌 Outbox 테이블이라는 로컬 데이터베이스 테이블에 이벤트를 보관한다는 것입니다. 이 테이블은 로컬 데이터베이스 작업에 사용하는 것과 동일한 효과를 갖습니다. 따라서 이벤트가 ACID를 사용하여 로컬 데이터베이스 내에서 원자적으로 생성되고 관리될 것입니다. Outbox 테이블에 저장된 이벤트를 읽고 게시하여 Outbox 패턴을 완료하는 것이 주안점입니다.

<br>

## Outbox 패턴은 장점만 있는 것이 아닙니다.

Outbox 패턴은 서비스 로직 변경과 Outbox 이벤트 저장을 하나의 트랜잭션으로 묶어 처리해야 하므로, 서비스 DB 내에 Outbox 테이블이 존재해야 합니다. 상당히 복잡한 아키텍처로 구성된 토스뱅크에서는 수백 개의 MSA 서버가 독립된 스키마(DB)를 바라보고 있고, 이 스키마들은 수십 개의 분리된 물리 서버 위에 그룹핑되어 존재한다고 합니다. 그리고 사용하는 DB 역시 Oracle, MySQL, MongoDB 등 다양한 DB를 Polyglot 형태로 사용한다고 합니다. 이것이 바로 Outbox를 적용하고자 할 때 트레이드 오프를 고려해야 하는 경계가 됩니다. 수백 개의 MSA에 일괄적으로 Outbox 패턴을 적용하려면, DB 종류, 물리 서버, 스키마마다 다양한 형태의 아웃박스 테이블들을 만들어줘야 하고, 테이블에서 메시지를 발행하는 애플리케이션도 각각 작성해야 합니다. 토스뱅크는 이러한 복잡도를 피하기 위해 Outbox 테이블을 따로 구성하는 게 아닌, Kafka 브로커에 이벤트를 발송하는 형태로 구축된 것으로 보입니다. 그리고 PDL(Producer Dead Letter) 이라고 하는 컴포넌트를 구현해 모든 서버에서 동일한 메시지 브로커를 바라볼 수 있도록 하고, PDL 에 접근하기 위한 라이브러리를 만들어 제공하므로 모든 마이크로서비스에 일괄 적용하는데 편리한 점이 있다고 합니다.

이 데모 프로젝트에서는 마이크로서비스 아키텍처의 복잡성을 적정 수준으로 유지하고, Saga 패턴의 핵심 원리를 빠르고 간단하게 파악할 수 있도록 Outbox 패턴을 사용합니다.

<br>

# CDC(Change Data Capture)

CDC는 데이터베이스의 변경 사항(삽입, 수정, 삭제)을 실시간으로 감지하고 이를 다른 시스템으로 전달하는 기술입니다. 이 기술을 활용하면 시스템 간의 데이터 일관성을 유지할 수 있습니다. 사용 사례로는 데이터베이스 복제, 이벤트 소싱, 실시간 분석 파이프라인, 마이크로서비스 간 데이터 동기화 등이 있습니다. CDC에 특화된 대표적인 도구로는 Debezium, Apache Nifi, AWS DMS 등이 있습니다.

이 데모 프로젝트에서는 오픈소스 CDC 플랫폼인 Debezium을 사용합니다. 이 도구는 Kafka connect의 Source connector이며, DB의 변경 사항을 캡쳐하고 Kafka 토픽에 이를 발행합니다. 이 발행된 토픽은 해당 토픽을 구독하고 있던 Kakfa consumer가 받아 후속 조치를 취할 수 있습니다.

![Debezium CDC 내부 동작](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/cdc.png)

Debezium CDC 내부 동작

이 데모 프로젝트에서 DB로 Postgres를 사용하는데, 트랜잭션 로그가 WAL 에 기록됩니다. 이 WAL에 관찰 대상 테이블의 신규 트랜잭션이 있는지 파악하고 존재하는 경우 이를 읽어 PGOutput 플러그인을 통해 JSON으로 디코딩되어 Replication Slot으로 전달됩니다. 이 과정은 Push 모델입니다.

그 후 Debezium Source Connector는 지속해서 Replication Slot에 새로운 데이터가 발생했는지 Polling 하고, 받아올 데이터가 있는 경우 가져옵니다. 이 과정은 Pull 모델입니다.

<br>

# Debezium CDC

Debezium은 오픈소스 CDC 플랫폼이며, 실제로 여러 IT 대기업에서 안정적으로 사용하고 있는 검증된 도구입니다.

![image.png](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/cdc%2520with%2520kafka.png)

Debezium Postgres Source Connector가 Replication Slot에 추가된 트랜잭션이 있는지 지속해서 확인하고, 존재하는 경우 Kafka 토픽에 발행합니다. Saga 액션 흐름의 Orchestrator 역할을 하는 Booking 마이크로서비스는 debezium 관련 토픽에 새로운 메시지가 들어왔는지 Polling하고, 이를 가져옵니다. Saga의 전체 단계 중 한 부분에 관해 작업을 마치면 해당 Saga 액션을 완수했음을 알리고, 다음 액션이 실행될 수 있도록 Kafka에 이벤트를 발행합니다. Orchestrator로부터 지령을 전달받은 마이크로서비스는 Saga 액션을 수행합니다.

<br>

# Saga 패턴 + Outbox 패턴 + CDC를 결합한 비즈니스 구현

![Saga, Outbox, Booking 상태 정의](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/saga+outbox+booking%2520status.png)

Saga, Outbox, Booking 상태 정의

예약에 관해 Orchestrator 책임을 갖는 Booking 마이크로서비스를 중심으로 예약 시퀀스를 살펴보면 다음과 같습니다.

## 1) 호텔/항공권/차량 예약 성공 케이스

![호텔/항공권/차량 예약 성공 케이스](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/booking%2520all%2520success.png)

호텔/항공권/차량 예약 성공 케이스

Booking Status Flow 를 중심으로 설명하겠습니다. 모든 도메인 이벤트는 과거형으로 작성합니다.

1. 사용자가 예약을 요청합니다.
2. Booking 서비스는 `Booking Created Event(예약 생성 이벤트)`를 Hotel 서비스로 전송합니다. 이 시점에서 Booking 서비스의 로컬 데이터베이스에는 예약 상태를 `PENDING(진행 중)` 으로 표시합니다.
3. Hotel 서비스는 `Booking Created Event`를 전달받아 호텔 예약/결제를 수행하고 로컬 데이터베이스에 `HOTEL_BOOKED(호텔 예약/결제 완료)` 상태로 기록합니다.
4. Booking 서비스에서 호텔 예약/결제 완료 이벤트를 Kafka 메시지 큐에서 받아와 로컬 데이터베이스에 예약 상태를 `HOTEL_BOOKED`로 변경합니다. 그리고 `Hotel BOOKED Event`를 Flight 서비스에 전달합니다.
5. Flight 서비스는 `Hotel BOOKED Event`를 전달받아 항공권 예약/결제를 수행하고 로컬 데이터베이스에 `FLIGHT_BOOKED(항공권 예약/결제 완료)` 상태로 기록합니다.
6. Booking 서비스에서 항공권 예약/결제 완료 이벤트를 Kafka 메시지 큐에서 받아와 로컬 데이터베이스에 예약 상태를 `FLIGHT_BOOKED`로 변경합니다. 그리고 `Flight BOOKED Event`를 Car 서비스에 전달합니다.
7. Car 서비스는 `Car BOOKED Event`를 전달받아 차량 예약/결제를 수행하고 로컬 데이터베이스에 `Car_BOOKED(차량 예약/결제 완료)` 상태로 기록합니다.
8. Booking 서비스에서 차량 예약/결제 완료 이벤트를 Kafka 메시지 큐에서 받아와 로컬 데이터베이스에 예약 상태를 `APPROVED(승인 됨)` 으로 변경하고 예약을 마칩니다.

예약 흐름의 기조는 위와 같으며 보시다시피 Kafka를 활용해 프로세스를 비동기로 처리합니다.

실패 케이스에 따른 보상 흐름은 다음과 같습니다.

## 2) 호텔 예약 실패 케이스

![호텔 예약 실패 케이스](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/booking%2520hotel%2520failed.png)

호텔 예약 실패 케이스

## 3) 항공권 예약 실패 케이스

![항공권 예약 실패 케이스](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/booking%2520flight%2520failed.png)

항공권 예약 실패 케이스

## 4) 차량 예약 실패 케이스

![차량 예약 실패 케이스](https://gist.github.com/SunhyeokChoe/e892c5958a4a064b70929dec459e6462/raw/110a45b77ba8d2cda620356c19b456de6bcbdb25/car%2520failed.png)

차량 예약 실패 케이스

<br>

# Saga Action 인터페이스 및 상태 정의

Saga 패턴 구현을 위한 인터페이스 정의는 다음과 같습니다.

```java
public interface SagaAction<T> {
	void process(T event);
	void compensate(T event);
}

public enum SagaActionStatus {
    STARTED,      // 시작
    PROCESSING,   // 진행 중
    COMPENSATING, // 보상 진행 중
    SUCCEEDED,    // 성공
    COMPENSATED   // 보상 완료
}
```

Saga 패턴 구현에는 process, compensate 두 개의 메서드가 필요합니다. process로 Saga 단계를 순차적으로 진행하고, 어떤 시점에 실패하는 경우 다시 역순으로 순차적으로 트랜잭션을 보상(Compensating)해야 합니다. 보상 작업은 compensate 메서드에 정의합니다.

<br>

# Kafka 토픽 구성

> 💡 토픽 네이밍 컨벤션은 다음과 같이 구성했습니다.
`<트랜잭션_타입(명사)>.<발행자_서비스_이름>.<소비자_서비스_이름>.<액션(동사)>`
트랜잭션 타입을 최상위 카테고리로 두면, 해당 토픽은 어느 비즈니스 범주에 포함되는지 한눈에 알기 쉬워 추후 서비스가 커졌을 때 함께 불어난 토픽 유지보수에 많은 노력을 들이지 않아도 될 것 같아 위와 같은 구조로 정의했습니다.
>

<aside>


Kafka 토픽으로의 메시지 발행은 Debezium Postgres Source Connector가 outbox 테이블의 WAL 로그의 변경사항을 감지하고 메시지를 생성해 Kafka 토픽으로 전달합니다.

각 마이크로서비스에 Kafka Producer를 정의하고 메시지를 직접 발행하는 방식이 아닙니다.

</aside>

※ 상호 비동기 통신 마이크로서비스 목록: booking ↔ (hotel, flight, car)

### 호텔/항공권/차량 예약 승인 트랜잭션 흐름 (booking_request_flow)

- `booking_request_flow.booking.hotel.request_booking`: booking → hotel 예약 요청
- `booking_request_flow.hotel.booking.notify_booking_completed`: hotel → booking 예약 완료 알림
- `booking_request_flow.booking.flight.request_booking`: booking → flight 예약 요청
- `booking_request_flow.flight.booking.notify_booking_completed`: flight → booking 예약 완료 알림
- `booking_request_flow.booking.car.request_booking`: booking → car 예약 요청
- `booking_request_flow.flight.booking.notify_booking_completed`: car → booking 예약 완료 알림

### 호텔/항공권/차량 예약 취소 트랜잭션 흐름 (booking_cancel_flow)

- `booking_cancel_flow.booking.hotel.cancel_booking`: booking → hotel 예약 취소 요청
- `booking_cancel_flow.hotel.booking.notify_booking_canceled`: hotel → booking 예약 취소 완료 알림
- `booking_cancel_flow.booking.flight.cancel_booking`: booking → flight 예약 취소 요청
- `booking_cancel_flow.flight.booking.notify_booking_canceled`: flight→ booking 예약 취소 완료 알림
- `booking_cancel_flow.booking.car.cancel_booking`: booking → car 예약 취소 요청
- `booking_cancel_flow.car.booking.notify_booking_canceled`: car → booking 예약 취소 완료 알림

이를 booking 서비스의 application.yaml에 정의하면 다음과 같습니다.

```yaml
spring:
    # Spring Cloud Function
    function:
      # booking 마이크로서비스와 hotel, flight, car 마이크로서비스들 간의 예약 요청/응답을 처리하는 function
      definition: hotelBookingFlow;flightBookingFlow;carBookingFlow;hotelCancelFlow;flightCancelFlow;carCancelFlow
    # Spring Cloud Stream
    stream:
      kafka:
        binder:
          brokers:
            - localhost:9092
      bindings:
        # ===== 예약 요청 플로우(booking_request_flow) =====
        # 호텔 예약 완료 응답 처리
        hotelBookingCompleted-in-0:
          destination: booking_request_flow.hotel.booking.notify_booking_completed
          group: ${spring.application.name}-hotel-booking

        # 항공권 예약 완료 응답 처리
        flightBookingCompleted-in-0:
          destination: booking_request_flow.flight.booking.notify_booking_completed
          group: ${spring.application.name}-flight-booking

        # 차량 예약 완료 응답 처리
        carBookingCompleted-in-0:
          destination: booking_request_flow.car.booking.notify_booking_completed
          group: ${spring.application.name}-car-booking

        # ===== 예약 취소 플로우(booking_cancel_flow) =====
        # 호텔 예약 취소 완료 응답 처리
        hotelCancelCompleted-in-0:
          destination: booking_cancel_flow.hotel.booking.notify_booking_canceled
          group: ${spring.application.name}-hotel-booking-canceling

        # 항공권 예약 취소 완료 응답 처리
        flightCancelCompleted-in-0:
          destination: booking_cancel_flow.flight.booking.notify_booking_canceled
          group: ${spring.application.name}-flight-booking-canceling

        # 차량 예약 취소 완료 응답 처리
        carCancelCompleted-in-0:
          destination: booking_cancel_flow.car.booking.notify_booking_canceled
          group: ${spring.application.name}-car-booking-canceling
```

<br>

# RDB 중 MySQL 이 아닌 PostgreSQL를 선택한 이유 (사담)

MySQL은 Bin 로그로, PostgreSQL은 WAL을 통해 트랜잭션 변경 사항을 기록하고, 복제하고, 복구하는데 사용됩니다. 이 두 가지 방식 모두 트랜잭션 관리 및 복구 기능을 제공하지만, 안정성과 데이터 일관성 측면에서 차이가 있습니다.

## 1) Bin 로그와 WAL 의 차이점

- MySQL Bin 로그
  - 작동 방식: SQL 문장 기반(Row-based 또는 Statement-based)으로 트랜잭션 변경 사항을 기록하며, 변경 사항을 다른 복제 서버로 전송하여 복제합니다.
  - 목적: 주로 복제를 위해 설계됐기에 복구보다는 변경된 트랜잭션 이벤트의 전파에 중점을 두고 있습니다.
  - 구조적 한계: Bin 로그는 MySQL 서버의 트랜잭션 수행 시점과 약간의 지연이 발생할 수 있으며, 비동기 복제를 사용하는 경우 Master와 Slave 데이터베이스 간의 실시간 데이터 일관성 보장이 어렵습니다. 특히 대규모 트랜잭션 처리 시 이러한 지연이 데이터 무결성에 영향을 줄 수 있습니다.
- PostgresSQL WAL
  - 작동 방식: Page 단위로 모든 변경 사항을 기록하여 트랜잭션의 모든 단계가 기록되며, 복제와 복구에 모두 사용됩니다.
  - 목적: WAL은 트랜잭션의 원자성과 일관성을 보장하며, 데이터 무결성 유지를 위해 설계됐습니다.
  - 데이터 일관성 보장 및 안정성: PostgreSQL은 WAL 기반 동기 복제를 통해 Master와 Slave 간의 데이터 일관성을 보다 신뢰성 있게 유지할 수 있습니다. 그리고 WAL은 시스템 장애 발생 시 복구에 유리하며, 트랜잭션의 원자성(Atomicity)과 내구성(Durability)을 확실히 보장합니다.

## 2) 결론

MySQL의 Bin 로그는 복제의 편의성이 장점이지만, 안정성과 데이터 일관성 측면에서는 PostgreSQL의 WAL이 더 우수합니다. 대규모 트랜잭션의 원자성을 유지해야 하고, 트랜잭션 데이터 복구와 데이터 무결성이 중요한 비즈니스에서는 PostgreSQL의 WAL 기반 시스템이 더 적합합니다. 반면, 읽기 성능 최적화와 복제의 편의성을 우선해야 하는 비즈니스의 경우 MySQL을 보완적으로 활용할 수 있습니다. 따라서 트랜잭션 안정성이 아주 중요한 예약/결제 도메인에는 PostgreSQL을 사용하기로 결정했습니다.

※ 사실 이 프로젝트는 DB 복제를 다루지 않기도 하고, 무엇보다 사용 경험이 없지만, 요즘 뜨고 있는 PostgreSQL을 사용해보고 싶었습니다. 🥸

<br>

# Service Discovery 패턴과 Service Registration 패턴 (사담)

서비스 디스커버리 패턴은 서비스 레지스트리에서 실행 및 관리 중인 서버 인스턴스에 대한 모든 정보를 추적하는 걸 포함하는 개념입니다. 서비스 레지스트리의 목적은 서버 인스턴스가 생성될 때마다 레지스트리 내부에 등록하고, 종료 또는 삭제될 때 레지스트리에서 제거하는 것입니다. 만약 10개의 채팅 서버가 뜬다면 레지스트리에 모든 서버를 등록하게 되고, 서비스 디스커버리는 이 정보를 바탕으로 적절한 서비스를 찾을 수 있게 되는 것입니다. 정리하면, 서비스 디스커버리와 레지스트레이션은 각 마이크로서비스가 서로 어떻게 찾고 통신할 것인가에 대한 문제 해결 수단인 것입니다.

여기에 두 가지 종류의 로드밸런서 개념이 있는데,  하나는 클라이언트 사이드 서비스 디스커버리이고, 다른 하나는 서버 사이드 서비스 디스커버리이다. 이 프로젝트에는 쿠버네티스를 활용하여 서버 사이드 서비스 디스커버리를 도입했습니다. 클라이언트 사이드 서비스 디스커버리의 경우 유레카 서버와 같은 디스커버리 서비스를 따로 구성해야 하고, 지속해서 이 서버를 유지/관리해야 하는 비용이 들어갑니다. 따라서 관리의 부담을 줄이기 위해 K8s의 Discovery Server의 힘을 빌리기로 했습니다. 검색 대상이 되는 각 마이크로서비스는 `spring-cloud-starter-netflix-eureka-client` 가 아닌 `spring-cloud-starter-kubernetes-discoveryclient` 의존성을 갖고, 메인 애플리케이션 클래스에 `@EnableDiscoveryClient` 를 부착하면 됩니다.

<br>

# Helm 차트를 사용하는 이유 (사담)

이러한 작은 규모에서는 수동으로 Kubernetes 매니페스트를 작성하고 일일이 배포하는 작업이 수월할 수 있습니다. 하지만, 실제 현업에서는 수백 개의 마이크로서비스와 매니페스트를 관리해야 하는 경우가 많습니다. 이를 고려할 때, 수동 배포 및 매니페스트 관리 작업에는 다음과 같이 여러 가지 문제점이 존재합니다.

## **1) 수십 수백 개의 매니페스트 관리**

수십 수백 개의 매니페스트 파일을 작성했다고 가정해봅시다. Kubernetes 클러스터에 이를 배포하려면 `kubectl apply` 명령을 사용해 각 매니페스트 파일을 하나씩 지정하여 실행해야 합니다.

이 작업은 단순히 시간이 오래 걸릴 뿐만 아니라, 매니페스트가 늘어날수록 사람이 실수할 가능성도 증가합니다. 이런 방식은 대규모 마이크로서비스 환경에서 비효율적입니다.

## **2) 환경별 설정 차이**

현업에서는 Development, QA, Production과 같은 다양한 환경이 존재합니다. 환경별로 요구사항이 다르기 때문에, 다음과 같은 문제가 발생할 수 있습니다.

- **Development 환경**: Replica 1개면 충분.
- **QA 환경**: Replica 최소 3개 필요.
- **Production 환경**: Replica 최소 12개 필요.

각 환경에 맞는 desired state를 정의하기 위해서는 모든 환경에 대응되는 매니페스트 파일을 개별적으로 작성하고 관리해야 합니다. 이는 시간이 많이 소요되며, 파일 간 일관성을 유지하기도 어렵습니다.

이러한 문제를 해결하기 위해 Helm이 등장했습니다. Helm은 Kubernetes의 패키지 매니저로, 다음과 같은 장점을 제공합니다.

- 여러 매니페스트 파일을 하나의 Chart로 묶어 관리.
- 환경별 설정 차이를 `values.yaml` 파일로 간단히 정의.
- 한 번의 명령으로 매니페스트를 생성하고 배포.
