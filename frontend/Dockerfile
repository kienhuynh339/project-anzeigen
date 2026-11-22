
# ==========================================
# STAGE 1: BUILD
# ==========================================

FROM node:24-alpine AS build

WORKDIR /app

# Package-Dateien kopieren
COPY package.json package-lock.json ./

# Dependencies installieren
RUN npm ci

# React-Quellcode kopieren
COPY . .

# Produktionsbuild erstellen
RUN npm run build


# ==========================================
# STAGE 2: NGINX
# ==========================================

FROM nginx:alpine

# Eigene Nginx-Konfiguration übernehmen
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Fertige React-Anwendung kopieren
COPY --from=build /app/dist /usr/share/nginx/html

# HTTP-Port
EXPOSE 80

# Nginx im Vordergrund starten
CMD ["nginx", "-g", "daemon off;"]
