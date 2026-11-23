
# ==========================================
# STAGE 1: BUILD
# ==========================================

# Basis-Image eclipse-temurin:27-jdk verwenden
# Java Development Kit, um die Java-Anwendung zu kompilieren und daraus eine ausführbare JAR-Datei zu erstellen
FROM eclipse-temurin:27-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew ausführbar machen ("./gradlew" braucht das Ausführungsrecht)
RUN chmod +x gradlew

# Abhängigkeiten herunterladen
RUN ./gradlew dependencies --no-daemon

# Quellcode kopieren
COPY src ./src

# Eine ausführbare Spring-Boot-JAR-Datei erstellen
RUN ./gradlew bootJar --no-daemon


# ==========================================
# STAGE 2: RUNTIME
# ==========================================

# Java Runtime Environment enthält die benötigte Laufzeitumgebung zum Ausführen von Java-Anwendungen
FROM eclipse-temurin:27-jre

WORKDIR /app

# JAR übernehmen
COPY --from=build /app/build/libs/*.jar app.jar

# Bilder übernehmen
COPY --from=build /app/src/main/resources/images ./src/main/resources/images

# Container verwendet Port 8080
EXPOSE 8080

# ENTRYPOINT legt das Hauptprogramm fest
# "java" startet die Java-Laufzeit
# "-jar" führt eine JAR-Datei aus ("app.jar")
ENTRYPOINT ["java", "-jar", "app.jar"]