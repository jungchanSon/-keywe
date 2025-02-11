FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/order-service-0.0.1.jar

COPY ${JAR_FILE} save-service-app.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "/save-service-app.jar"]
