# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Maven files first (layer caching – only re-downloads deps if pom.xml changes)
COPY pom.xml .
COPY src ./src

# Install Maven and build
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# ─── Stage 2: Run ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the final JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Railway injects PORT env variable; Spring Boot reads it via server.port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
