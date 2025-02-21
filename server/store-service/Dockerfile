FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/store-service-0.0.1.jar

COPY ${JAR_FILE} store-service-app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/store-service-app.jar"]
