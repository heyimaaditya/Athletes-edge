# Base image hum Java 17 ka use karenge
FROM openjdk:17-jdk-slim

# Install curl for container healthcheck
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl ca-certificates \
 && rm -rf /var/lib/apt/lists/*

# Container ke andar ek working directory banao
WORKDIR /app

# IMPORTANT: Sirf target folder ko copy karo
COPY target/*.jar app.jar

# Application ko start karne ki command
ENTRYPOINT ["java","-jar","app.jar"]
