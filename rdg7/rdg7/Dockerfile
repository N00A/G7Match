# Etapa 1: Construcción de la aplicación con Gradle
# Usa imagen base con Gradle y JDK 17 para compilar la app
FROM gradle:8.7-jdk17 AS build

# Define /app como directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos principales de configuración de Gradle
COPY build.gradle settings.gradle ./

# Copia el directorio del wrapper de Gradle
COPY gradle ./gradle

# Ejecuta el build de Gradle (descarga dependencias y compila con pruebas)
# --no-daemon evita usar procesos en segundo plano
RUN gradle build --no-daemon

# Copia el código fuente al contenedor
COPY src ./src

# Limpia y vuelve a compilar toda la app (con pruebas incluidas)
RUN gradle clean build --no-daemon

# Etapa 2: Imagen final que se usará para ejecutar la app compilada
# Usa una imagen liviana con JDK 17 para producción
FROM eclipse-temurin:17-jdk

# Define /app como directorio de trabajo
WORKDIR /app

# Copia el archivo .jar generado desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Define la URL de conexión por defecto a SQL Server
ENV DB_URL="jdbc:sqlserver://localhost:1433;databaseName=mi_base"

# Define el usuario por defecto para la base de datos
ENV DB_USER="sa"

# Define la contraseña por defecto para la base de datos
ENV DB_PASSWORD="12345"

# Define un token ficticio por defecto para análisis con SonarQube
ENV SONAR_TOKEN="mi-token-sonar"

# Expone el puerto 9090 (el que usará la app Spring Boot)
EXPOSE 9090

# Comando que se ejecuta cuando se inicia el contenedor
ENTRYPOINT ["java", "-jar", "app.jar", "--server.address=0.0.0.0"]