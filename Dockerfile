# Use an official OpenJDK runtime as a parent image
FROM --platform=linux/amd64 openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar .

# Expose the port the app runs on
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
