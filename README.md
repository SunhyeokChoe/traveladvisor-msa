# 트래블어드바이저(traveladvisor) — Microservices with Spring Cloud Gateway, OAuth2, Keycloak, Kafka, Debezium CDC and PostgreSQL

# 이 프로젝트를 만든 이유
<p align="center">
  <img src="https://media4.giphy.com/media/8dYmJ6Buo3lYY/giphy.gif?cid=7941fdc6vac0eapvo79w9tgrd48b8a9thrpwqw27t6rv76ty&ep=v1_gifs_search&rid=giphy.gif&ct=g" alt="Centered Image" />
</p>

이 프로젝트는 MSA + 쿠버네티스 환경에서 DDD와 Hexagonal Architecture를 결합 한다고 했을 때 어떻게 구성되고 관리되어야 하는지에 대한 호기심에서 시작되었습니다. 작은 규모의 프로젝트에서는 모놀리식 아키텍처나 멀티레포보다 복잡도가 높아지고, 러닝 커브가 높아 개발 속도가 느려질 수 있다는 점이 단점으로 올 수 있지만, 대규모 개발 환경에서는 조직 구조, 팀원 구성, 가용 가능 인원, 그리고 회사의 여유 자금 등 다양한 요인에 따라 MSA가 적합한 프로젝트 구조가 될 수 있다고 합니다.

진행하다 보니 Saga + Outbox 패턴을 알게 돼 공부해 데모로 적용 해보기도 하고, 이와 더불어 쿠버네티스 학습도 병행하기 위해 **정말 많은 레퍼런스**를 찾아가며 만들었습니다. 진행하면서 Saga와 Outbox의 장점과 쿠버네티스의 편의성에 대해 어느 정도 알게 되었습니다. 다만 Hexagonal 패턴.. 은 앞으로 진행할 사이드 프로젝트에 적용할 생각은 다소 없어졌습니다. 온 종일 코딩만 하다가 Git 스테이지를 살펴보면 80개 파일의 코드에 수정 사항이 있다고 뜨기도 합니다. 이는 Adapter, Application, Domain Core가 추상화 레이어를 통해 철저히 나뉘어 각 계층 간 격벽 역할을 훌륭히 수행하고 있다는 반증이기도 하지만, 작은 규모에서는 뚜렷한 미래 목표가 없으면 복잡도만 증가하고 유지보수와 개발 속도에 유의미한 효과는 없어 보이기 때문입니다. 다만, 팀이 잘 나누어져 있고, 각자 도메인을 맡아 처리할 수 있는 환경이라면 도입 해보는 것도 좋아 보입니다.

마지막으로 이 프로젝트를 진행한 이유 중 하나는 과거 거대 버티컬 이커머스 프로젝트를 진행하면서 쿠버네티스를 처음 접해봤는데, 그때 느꼈던 부족함을 보완하고, 이번 기회에 쿠버네티스와 더욱 친숙해지는 계기를 만들고 싶었습니다.

# 프로젝트 비즈니스 개략적 설명
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