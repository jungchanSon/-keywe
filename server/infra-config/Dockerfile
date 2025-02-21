FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/infra-config-0.0.1.jar

RUN microdnf install -y openssh-clients && microdnf clean all
RUN mkdir -p /root/.ssh && chmod 700 /root/.ssh
RUN ssh-keyscan -t ecdsa github.com >> /root/.ssh/known_hosts

COPY ${JAR_FILE} infra-config-app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/infra-config-app.jar"]
