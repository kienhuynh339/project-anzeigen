/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";

// Registrieren
export function register(profileName, accountName, password) {
  const body = {
    profileName: profileName,
    accountName: accountName,
    password: password,
  };
  return request(`/users/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

// Aktuellen Benutzer abrufen
export function getCurrentUser() {
  return request("/users/me", {
    method: "GET",
  });
}

export function updateProfileName(profileName) {
  const body = { profileName: profileName };
  return request(`/users/profile-name`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function updateAccountName(accountName) {
  const body = { accountName: accountName };
  return request(`/users/account-name`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function updatePassword(password) {
  const body = { password: password };
  return request(`/users/password`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}
