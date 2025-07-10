# Etapa 1: construir el JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagen ligera para correr la app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto (ajusta si usas otro)
EXPOSE 8080

# Lanza el JAR y permite pasar variables de entorno como MONGO_URI
ENTRYPOINT ["java", "-jar", "app.jar"]
