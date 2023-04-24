FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/pagamentos-0.0.1-SNAPSHOT.jar /app/pagamentos.jar
EXPOSE 0
ENTRYPOINT  ["java", "-jar", "/app/pagamentos.jar"]