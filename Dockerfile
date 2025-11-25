# ===============================
# Stage 1: Build the Application
# ===============================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy only pom.xml first (to cache dependencies)
# This ensures that if you only change code, you don't re-download all libraries.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy the source code
COPY src ./src

# 3. Build the JAR file (Skip tests to speed up deployment)
RUN mvn clean package -DskipTests

# ===============================
# Stage 2: Run the Application
# ===============================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a temporary volume for Spring Boot (Tomcat requires this for temp files)
VOLUME /tmp

# 1. Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# 2. Expose the port (Default 8080)
EXPOSE 8080

# 3. Run the app with JAVA_OPTS support
# Changing to "sh -c" allows us to inject environment variables (like memory limits)
# into the Java process at runtime.
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]