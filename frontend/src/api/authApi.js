import { request } from "./request";

// Registrieren
export function register(profileName, accountName, password) {
  const body = {
    profileName: profileName,
    accountName: accountName,
    password: password,
  };
  return request(`/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

// Anmelden
export function login(accountName, password) {
  const body = {
    accountName: accountName,
    password: password,
  };
  return request(`/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

// Abmelden
export function logout() {
  return request(`/auth/logout`, {
    method: "POST",
  });
}

// Aktuellen Benutzer abrufen
export function getCurrentUser() {
  return request("/users/me", {
    method: "GET",
  });
}
