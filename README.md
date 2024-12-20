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