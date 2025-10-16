# Multi-stage build for Spring Boot application
# 1) Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
# Pre-download dependencies to leverage Docker layer caching
RUN mvn -B -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# 2) Runtime stage
FROM eclipse-temurin:17-jre

# Optional: Add a non-root user
RUN useradd -ms /bin/bash appuser
USER appuser

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /workspace/target/librarytest-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

# Default JVM opts can be overridden at runtime
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
