# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar solo el POM para aprovechar caché de dependencias
COPY pom.xml .

# Descargar dependencias sin-compilar para maximizar caché
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests -B

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no root para seguridad
RUN addgroup -g 1000 appgroup && adduser -u 1000 -G appgroup -s /bin/sh -D appuser

# Copiar el JAR construido desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Establecer permisos correctos
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no root
USER appuser

# Exponer puerto de la aplicación
EXPOSE 8080

# Variables de entorno para producción
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:+HeapDumpOnOutOfMemoryError"
ENV SPRING_PROFILES_ACTIVE=prod

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Iniciar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]