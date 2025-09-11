# Etapa 1: Construcción de la aplicación con Gradle
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# Copiar los archivos de Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Descargar dependencias sin compilar todo
RUN gradle build -x test --no-daemon || return 0

# Copiar el código fuente y compilar
COPY src ./src
RUN gradle clean build -x test --no-daemon

# Etapa 2: Imagen final para ejecución
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Definir variables de entorno con valores por defecto
ENV SONAR_TOKEN="mi-token-sonar"
ENV DB_URL="jdbc:postgresql://localhost:5432/mi_base"
ENV DB_USER="admin"
ENV DB_PASSWORD="12345"

# Exponer el puerto configurado en Spring Boot (ej: 9090)
EXPOSE 9090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
