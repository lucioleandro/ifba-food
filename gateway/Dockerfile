FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar
EXPOSE 8082
ENTRYPOINT  ["java", "-jar", "/app/gateway.jar"]