FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper from your project into the container
COPY gradlew .
COPY gradle gradle

# Grant execution permission to the Gradle wrapper script
RUN chmod +x ./gradlew

# Copy your source code into the container
COPY src src
COPY build.gradle settings.gradle ./

# Run the Gradle build
RUN ./gradlew clean build -x test

# Start with a new stage to reduce the image size
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=0 /app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]