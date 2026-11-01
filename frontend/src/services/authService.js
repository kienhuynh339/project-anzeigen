const API_URL = "http://localhost:8080/api";

async function request(url, options = {}) {
  const response = await fetch(`${API_URL}${url}`, {
    ...options,
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
  });

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    throw new Error(data?.message || "Request failed");
  }

  return data;
}

// REGISTRIEREN
export async function register(profileName, accountName, password) {
  return request("/auth/register", {
    method: "POST",
    body: JSON.stringify({
      profileName,
      accountName,
      password,
    }),
  });
}

// ANMELDEN
export async function login(accountName, password) {
  return request("/auth/login", {
    method: "POST",
    body: JSON.stringify({
      accountName,
      password,
    }),
  });
}

// ABMELDEN
export async function logout() {
  return request("/auth/logout", {
    method: "POST",
  });
}

// AKTUELLEN BENUTZER ABRUFEN
export async function getCurrentUser() {
  return request("/users/me", {
    method: "GET",
  });
}
