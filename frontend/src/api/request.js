/**
 * @author Trung Kien Huynh
 */
const API_URL = "http://localhost:8080/api";

export function request(url, options = {}) {
  return fetch(`${API_URL}${url}`, {
    ...options,
    credentials: "include",
  }).then(async (response) => {
    // data = null, wenn bei der Konvertierung in ein JavaScript-Objekt ein Fehler auftritt.
    const data = await response.json().catch(() => null);
    if (!response.ok) {
      throw new Error(
        data?.errorMessage || `Fehler bei der Anfrage an ${API_URL}${url}`,
      );
    }
    return data;
  });
}
