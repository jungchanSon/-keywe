FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/user-service-0.0.1.jar

COPY ${JAR_FILE} user-service-app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/user-service-app.jar"]
