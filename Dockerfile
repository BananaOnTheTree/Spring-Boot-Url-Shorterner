# ---------- build stage ----------
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# (optional) for healthcheck via curl
RUN apk add --no-cache curl

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
