FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/notification-service-0.0.1.jar

COPY ${JAR_FILE} notification-service-app.jar

EXPOSE 8110

ENTRYPOINT ["java", "-jar", "/notification-service-app.jar"]
