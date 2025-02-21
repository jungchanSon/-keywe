# 백엔드 알림사항
백엔드를 실행시키기 위해서는 config server의 key값이 필요합니다.  
김동철 학생에서 문의해주세요.

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
# Helm Chart로 실행하기
    ```shell
    cd helm-chart
    helm install [이름] [서비스 디렉토리] -n ['-service'를 제외한 이름] 
    ex) helm install banking banking-service -n banking
    ex) helm install config banking-service -n config
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

# API 문서
https://docs.google.com/spreadsheets/d/1huSgzLygolfjA3KAWP8FflUO0kZdy00xJI2B1RC3LPw/edit?usp=sharing

# 기능 정의서
https://docs.google.com/spreadsheets/d/1aNdunMym_30aH1Gi0TDTLe-Qp9nzORdN2DXdG5eWF5M/edit?usp=sharing

# 요구사항 정의서
https://docs.google.com/spreadsheets/d/1-yhAZcwYMlgATe1t7OFI1PVGGE-hXSn922OOGwUoW0w/edit?usp=sharing

# 피그마
https://www.figma.com/design/E5yhnT8CBfmRQoaYydJ55t/%EC%8B%B8%ED%94%BC-%EA%B3%B5%ED%86%B5-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-(%EB%8F%99%EC%B2%A0%ED%8C%80)?node-id=0-1&p=f&t=94mkCFgGBZPeBwiS-0
