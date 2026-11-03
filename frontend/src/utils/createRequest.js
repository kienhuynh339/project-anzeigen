/**
 * @author Trung Kien Huynh
 */
export function createRequest(url, createdObject) {
  return fetch(url, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(createdObject),
  }).then((response) => {
    if (!response.ok) {
      throw new Error("Fehler beim Erstellen: " + url);
    }
  });
}
