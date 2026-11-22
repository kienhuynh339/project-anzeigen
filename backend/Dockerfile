
# ==========================================
# STAGE 1: BUILD
# ==========================================

FROM eclipse-temurin:27-jdk AS build

WORKDIR /app

# Gradle-Projektdateien kopieren
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle Wrapper ausführbar machen
RUN chmod +x gradlew

# Abhängigkeiten herunterladen
RUN ./gradlew dependencies --no-daemon

# Quellcode kopieren
COPY src ./src

# Spring-Boot-Anwendung bauen
RUN ./gradlew bootJar --no-daemon


# ==========================================
# STAGE 2: RUNTIME
# ==========================================

FROM eclipse-temurin:27-jre

WORKDIR /app

# JAR übernehmen
COPY --from=build /app/build/libs/*.jar app.jar

# Bilder zusätzlich übernehmen
COPY --from=build /app/src/main/resources/images ./src/main/resources/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]