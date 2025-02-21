FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/infra-gateway-0.0.1.jar

COPY ${JAR_FILE} infra-gateway-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/infra-gateway-app.jar"]
