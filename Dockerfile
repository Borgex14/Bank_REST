FROM openjdk:17-jdk-slim

WORKDIR /app

# Копируем JAR и ресурсы (включая миграции)
COPY target/*.jar app.jar
COPY src/main/resources/ /app/resources/

# Устанавливаем ожидание для PostgreSQL
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/,file:/app/resources/"]