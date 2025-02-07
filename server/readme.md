# 디렉토리 구조
```
├─docker-compose.yaml   - 전체 서비스에 대한 docker-compose 파일
├─buile.gradle          - Root Project에 대한 gradle 파일
├─gradle.properties     - Gradle 변수 파일
├─settings.gradle       - 하위 모듈에 대한 의존성 설정 파일
├─docker                - 도커 관련 디렉토리 (db, kafka, zookeeper 등)
├─common                - 공용 모듈
├─infra-gateway         - 게이트웨이 모듈
└─user-service          - 유저 서비스 모듈
```

# Docker-Compose 명령어
- 전체 서비스 컨테이너 실행
    ```shell
    docker-compose up -d --build
    ```
- 전체 DB 컨테이너 실행
    ```shell
    cd docker/db  |
    docker-compose up -d
    ```
- Zookeeper, Kafka 컨테이너 실행
  ```shell
    cd docker/kafka |
    docker-compose up -d
  ```