FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/banking-service-0.0.1.jar

COPY ${JAR_FILE} banking-service-app.jar

EXPOSE 8300

ENTRYPOINT ["java", "-jar", "/banking-service-app.jar"]
