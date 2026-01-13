# syntax=docker/dockerfile:1.4

# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app

# -------------------------
# Pass GitHub credentials as a secret at build time
# -------------------------
# You will create a file `.github_credentials.env` containing:
# GITHUB_USERNAME=your_username
# GITHUB_TOKEN=ghp_...
# Do NOT commit this file

# Copy parent POM
COPY pom.xml .

# Copy Maven settings for private repos
COPY settings.xml .m2/

# Copy frontend files (so npm install works)
COPY package.json package-lock.json copy-files.js postcss.config.js tailwind.config.js ./

# Copy source code
COPY src ./src

# Build with Maven using BuildKit mounts
RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=secret,id=github_credentials,target=/run/secrets/github_credentials \
    bash -c '\
        set -a && \
        [ -f /run/secrets/github_credentials ] && . /run/secrets/github_credentials && \
        set +a && \
        mvn clean package -Pdocker -DskipTests -U --settings .m2/settings.xml \
    '

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Persistent data directory
RUN mkdir -p /data

# Copy the built JAR
COPY --from=build /app/target/jsonbin-config-manager-0.0.1-SNAPSHOT.jar app.jar

# Expose app port
EXPOSE 3000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
