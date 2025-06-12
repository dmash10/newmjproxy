# Multi-stage build for Railway optimization
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code and validation script
COPY src ./src
COPY validate-env.sh ./validate-env.sh

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage with smaller JRE image - Using Eclipse Temurin
FROM eclipse-temurin:17-jre-alpine

# Install necessary packages for Railway
RUN apk add --no-cache curl

# Create application user
ARG user=spring
ARG group=spring
ENV SPRING_HOME=/app

RUN addgroup -g 1000 ${group} && \
    adduser -u 1000 -G ${group} -h "$SPRING_HOME" -s /bin/sh -D ${user} && \
    mkdir -p $SPRING_HOME/config $SPRING_HOME/logs && \
    chown -R ${user}:${group} $SPRING_HOME

USER ${user}
WORKDIR $SPRING_HOME

# Copy the built JAR from builder stage
COPY --from=builder /app/target/midjourney-proxy-*.jar ./app.jar

# Copy validation script
COPY --from=builder /app/validate-env.sh ./validate-env.sh

# Railway uses PORT environment variable
EXPOSE $PORT

# Simple health check for Railway
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=5 \
    CMD curl -f http://localhost:${PORT:-8080}/health || exit 1

# Create startup script with proper variable expansion
RUN echo '#!/bin/sh' > start.sh && \
    echo 'chmod +x validate-env.sh' >> start.sh && \
    echo './validate-env.sh' >> start.sh && \
    echo 'echo "Starting Midjourney Proxy..."' >> start.sh && \
    echo 'echo "Java version: $(java -version 2>&1 | head -n 1)"' >> start.sh && \
    echo 'exec java -XX:MaxRAMPercentage=75 -Djava.awt.headless=true -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Dserver.port=${PORT:-8080} -Duser.timezone=UTC -Dspring.profiles.active=railway -jar app.jar' >> start.sh && \
    chmod +x start.sh

ENTRYPOINT ["./start.sh"]
