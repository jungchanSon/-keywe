FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/remote-service-0.0.1.jar

COPY ${JAR_FILE} remote-service-app.jar

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "/remote-service-app.jar"]
