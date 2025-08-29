# Base image hum Java 17 ka use karenge
FROM openjdk:17-jdk-slim

# Container ke andar ek working directory banao
WORKDIR /app

# IMPORTANT: Sirf target folder ko copy karo
# Yeh build context ko chota rakhta hai aur build fast karta hai
COPY target/*.jar app.jar

# Application ko start karne ki command
ENTRYPOINT ["java","-jar","app.jar"]