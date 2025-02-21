# Version
```
IntelliJ IDEA 2024.3.1.1 (Ultimate Edition)
Build #IU-243.22562.218, built on December 18, 2024
Licensed to JUNG CHAN SON
Subscription is active until December 15, 2025.
For educational use only.
Runtime version: 21.0.5+8-b631.28 amd64 (JCEF 122.1.9)
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Toolkit: sun.awt.windows.WToolkit
Windows 11.0
GC: G1 Young Generation, G1 Concurrent GC, G1 Old Generation
Memory: 10240M
Cores: 20
ide.experimental.ui=true
terminal.new.ui=true
jvm.dfa.analysis.ui.security.analysis.window.was.shown=true
IdeaVIM (2.18.1)
org.jetbrains.plugins.go-template (243.21565.122)
name.kropp.intellij.makefile (243.21565.122)
net.ashald.envfile (3.4.2)
com.devfive.vim_switch_ko (0.0.9)
com.intellij.ml.llm (243.22562.251.6)
com.intellij.bigdatatools.core (243.22562.218)
ru.adelf.idea.dotenv (2024.3)
com.intellij.bigdatatools.kafka (243.22562.145)
Kotlin: 243.22562.218-IJ
```
- Java Version=17 
- Spring Boot = 3.4.2
- springDependencyManagementVersion = 1.1.7
- DB 관련 도커 파일(설정 내용 포함)
  - docker/db/docker-compose.yaml

# AOS 프로젝트 세팅
프로젝트 루트 폴더에서 local.properties 파일 안에 API KEY 추가
Agora APP ID:
AGORA_APP_CERT=4e60af7c172b4a10957eac6b3ac9211e

# K8s 위에서 구동 시 설정할 값들
## 공통
PROFILE = prod

### User (회원)
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: ADJUSTMENT_MYSQL_URL
  value: {{ .Values.settlement_mysql_url}}
- name: PROFILE
  value: prod
- name: KAFKA_URL
  value: kraft-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092
- name: BANKING_SERVICE_URL
  value: banking-server-app.banking.svc.cluster.local
- name: MENU_SERVICE_URL
  value: store-server-app.store.svc.cluster.local

### store (메뉴, 카테고리)
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: STORE_MYSQL_URL
  value: {{ .Values.springEnv.storeMysqlUrl}}
- name: PROFILE
  value: prod
- name: KAFKA_URL
  value: kraft-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092

### Settlement (정산)
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: ADJUSTMENT_MYSQL_URL
  value: {{ .Values.settlement_mysql_url}}
- name: PROFILE
  value: prod
- name: KAFKA_URL
  value: kraft-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092
- name: BANKING_SERVICE_URL
  value: banking-server-app.banking.svc.cluster.local
- name: MENU_SERVICE_URL
  value: store-server-app.store.svc.cluster.local

### remote (소켓)
- name: USER_SERVICE_URL
  value: user-service-app.user.svc.cluster.local
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: REMOTE_SERVICE_REDIS
  value: redis-0.redis.remote.svc.cluster.local
- name: NOTIFICATION_SERVICE_URL
  value: notification-server-app.notification.svc.cluster.local

### order (주문)
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: ORDER_MYSQL_URL
  value: {{ .Values.order_mysql_url}}
- name: PROFILE
  value: prod
- name: KAFKA_URL
  value: kraft-cluster-kafka-bootstrap.kafka.svc.cluster.local
- name: BANKING_SERVICE_URL
  value: banking-server-app.banking.svc.cluster.local
- name: USER_SERVICE_URL
  value: user-service-app.user.svc.cluster.local
- name: MENU_SERVICE_URL
  value: store-service-app.store.svc.cluster.local

### Notification (알림 서버)
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: NOTIFICATION_MYSQL_URL
  value: {{ .Values.notification_mysql_url}}

### banking (뱅킹)
- name: CONFIG_SERVICE_URL
  value: config-server-app-service.config.svc.cluster.local
- name: BANKING_MYSQL_URL
  value: {{ .Values.banking_mysql_url}}
- name: PROFILE
  value: prod
- name: KAFKA_URL
  value: kraft-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092
****