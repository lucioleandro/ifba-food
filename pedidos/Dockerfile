FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/pedidos-0.0.1-SNAPSHOT.jar /app/pedidos.jar
ENTRYPOINT  ["java", "-jar", "/app/pedidos.jar"]