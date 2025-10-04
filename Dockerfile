# ---------- STAGE 1: BUILD ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:21-jre

WORKDIR /app
EXPOSE 8080 9090

COPY --from=builder /app/target/*.jar usuarios.jar
ENTRYPOINT ["java", "-jar", "usuarios.jar"]