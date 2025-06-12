# Multi-stage build for Railway optimization
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage with smaller JRE image - Using Eclipse Temurin
FROM eclipse-temurin:17-jre-alpine

# Install necessary packages for Railway
RUN apk add --no-cache curl

# Railway uses PORT environment variable
EXPOSE $PORT

# Copy the built JAR from builder stage
COPY --from=builder /app/target/midjourney-proxy-*.jar /app/app.jar

# Set working directory
WORKDIR /app

# Create a simple startup script that works
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'echo "=== Midjourney Proxy Starting ==="' >> /app/start.sh && \
    echo 'echo "PORT: ${PORT:-8080}"' >> /app/start.sh && \
    echo 'echo "DISCORD_GUILD_ID: ${DISCORD_GUILD_ID:-NOT_SET}"' >> /app/start.sh && \
    echo 'echo "DISCORD_CHANNEL_ID: ${DISCORD_CHANNEL_ID:-NOT_SET}"' >> /app/start.sh && \
    echo 'echo "MJ_API_SECRET: ${MJ_API_SECRET:+SET}"' >> /app/start.sh && \
    echo 'echo "Starting Java application..."' >> /app/start.sh && \
    echo 'exec java -Xmx512m -Djava.awt.headless=true -Dserver.port=${PORT:-8080} -Dspring.profiles.active=railway -jar app.jar' >> /app/start.sh && \
    chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]
