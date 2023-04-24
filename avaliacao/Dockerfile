FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/avaliacao-0.0.1-SNAPSHOT.jar /app/avaliacao.jar
EXPOSE 0
ENTRYPOINT  ["java", "-jar", "/app/avaliacao.jar"]