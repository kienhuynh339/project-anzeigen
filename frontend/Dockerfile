
# ==========================================
# STAGE 1: BUILD
# ==========================================

# Basis-Image node:24-alpine verwenden
# Ein Docker-Image ist eine Vorlage, auf deren Grundlage ein Container erstellt werden kann
# "AS build" gibt dieser Build-Stage einen Namen (wird später benötigt, um Dateien aus dieser Stage in eine andere Stage zu kopieren)
FROM node:24-alpine AS build

# Docker erstellt /app (im Container), falls es noch nicht existiert, und setzt es als aktuelles Arbeitsverzeichnis
WORKDIR /app

# Package-Dateien in den Docker-Build-Container kopieren
# package.json enthält die Informationen über Projektname, Version, installierte Abhängigkeiten,...
# package-lock.json enthält die konkrete Auflösung der Abhängigkeiten inklusive Versionen
# "./" ist das Zielverzeichnis für das Kopieren ("./" -> "/app")
COPY package.json package-lock.json ./

# Dependencies installieren
# "npm ci" steht für Clean Install
# RUN: Wird beim Erstellen des Images ausgeführt (RUN führt einen Befehl während des Docker-Build-Prozesses aus)
# CMD: Wird beim Starten des Containers ausgeführt
RUN npm ci

# Den restlichen Inhalt des lokalen Projektverzeichnisses in "/app" kopieren
# Erster . (Quelle): Das Docker-Build-Verzeichnis (Build-Kontext).
# Zweiter . (Ziel): Das aktuelle Arbeitsverzeichnis innerhalb des Docker-Images, das durch WORKDIR festgelegt wird.
# Das Docker-Build-Verzeichnis ist das Verzeichnis auf dem Host-Rechner, dessen Inhalt Docker beim Erstellen eines Images (Build) als Ausgangspunkt verwendet (auch als Build-Kontext bezeichnet)
# Der Build-Kontext ist in compose.yaml festgelegt
# Docker kann nur Dateien kopieren, die im Build-Kontext sind und nicht durch .dockerignore ausgeschlossen wurden.
COPY . .

# Build erstellen (im "/app/dist")
RUN npm run build


# ==========================================
# STAGE 2: NGINX
# ==========================================

# Basis-Image nginx:alpine (nginx ist ein Webserver) verwenden
# Jede FROM-Anweisung startet eine neue Build-Stage
FROM nginx:alpine

# Eigene Nginx-Konfiguration übernehmen
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Den Build im "/app/dist" in das Standard-Webverzeichnis von nginx kopieren
COPY --from=build /app/dist /usr/share/nginx/html

# Container verwendet Port 80
EXPOSE 80

# Nginx im Vordergrund starten
# CMD gibt an, welche Befehle beim Start eines Containers ausgeführt werden sollen
# "nginx" startet den Nginx-Webserver
# "-g" -> die folgenden Angaben sind globale Konfigurationsanweisungen ("daemon off;" ist eine globale Konfigurationsanweisung)
# ohne "-g" funktioniert "daemon off;" nicht
# "daemon off;" -> Nginx läuft im Vordergrund (Docker erwartet, dass der Hauptprozess des Containers im Vordergrund läuft)
# Docker überwacht den Hauptprozess eines Containers. Wenn dieser Prozess beendet wird, betrachtet Docker den Container als beendet
# ohne "daemon off;" läuft Nginx im Hintergrund und der Prozess endet sich -> Container beenden
CMD ["nginx", "-g", "daemon off;"]
