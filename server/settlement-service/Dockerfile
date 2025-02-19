FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/adjustment-service-0.0.1.jar

COPY ${JAR_FILE} adjustment-service-app.jar

EXPOSE 8310

ENTRYPOINT ["java", "-jar", "/adjustment-service-app.jar"]
