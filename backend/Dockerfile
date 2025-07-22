# ---------- STAGE 1: Build the JAR ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy dependency files first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- STAGE 2: Run the app ----------
FROM eclipse-temurin:17-jre-alpine

# Install dumb-init for proper signal handling
RUN apk add --no-cache dumb-init

# Create non-root user for security
RUN addgroup -g 1001 -S appuser && \
    adduser -S -D -H -u 1001 -h /app -s /sbin/nologin -G appuser -g appuser appuser

WORKDIR /app

# Copy the JAR file
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

# Use dumb-init for proper signal handling and optimized JVM flags
ENTRYPOINT ["dumb-init", "--", "java", \
    "-Xmx1g", "-Xms512m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]