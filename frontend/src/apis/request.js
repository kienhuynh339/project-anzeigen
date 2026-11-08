/**
 * @author Trung Kien Huynh
 */
const API_URL = "http://localhost:8080/api";

export function request(url, options = {}) {
  return fetch(`${API_URL}${url}`, {
    ...options,
    credentials: "include",
    headers: { ...options.headers, "X-XSRF-TOKEN": getCookie("XSRF-TOKEN") },
  }).then(async (response) => {
    // data = null, wenn bei der Konvertierung in ein JavaScript-Objekt ein Fehler auftritt.
    const data = await response.json().catch(() => null);
    if (!response.ok) {
      throw new Error(
        data?.message || `Fehler bei der Anfrage an ${API_URL}${url}`,
      );
    }
    return data;
  });
}

export function getCsrfToken() {
  return fetch(`${API_URL}/auth/csrf`, {
    method: "GET",
    credentials: "include",
  }).then((response) => {
    if (!response.ok) {
      throw new Error("CSRF-Token konnte nicht geladen werden.");
    }
  });
}

function getCookie(name) {
  const cookies = document.cookie.split("; ");

  for (const cookie of cookies) {
    // mit "...value" enthält value (ein Array) die restlichen Elemente
    const [key, ...value] = cookie.split("=");

    if (key === name) {
      return decodeURIComponent(value.join("="));
    }
  }

  return null;
}
