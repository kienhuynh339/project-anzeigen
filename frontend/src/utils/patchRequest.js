export function patchRequest(url, name, value) {
  return fetch(url, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ [name]: value }),
  }).then((response) => {
    if (!response.ok) {
      throw new Error("Fehler beim Aktualisieren: " + url);
    }
  });
}
