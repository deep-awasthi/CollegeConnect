# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first to cache them
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Create upload directory and set environment variables
RUN mkdir -p /app/uploads
ENV UPLOAD_DIR=/app/uploads
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=collegeconnect
ENV DB_USER=postgres
ENV DB_PASS=postgres
ENV REDIS_HOST=cache
ENV REDIS_PORT=6379

ENTRYPOINT ["java", "-jar", "app.jar"]
