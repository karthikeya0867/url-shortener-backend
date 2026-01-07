# ---------- BUILD STAGE ----------
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy project files
COPY . .

# FIX: make mvnw executable (Linux needs this)
RUN chmod +x mvnw

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests


# ---------- RUN STAGE ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Render provides PORT at runtime
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
