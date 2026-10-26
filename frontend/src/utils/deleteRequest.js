/**
 * @author Trung Kien Huynh
 */
export function deleteRequest(url) {
  return fetch(url, {
    method: "DELETE",
  }).then((response) => {
    if (!response.ok) {
      throw new Error(`Fehler beim Löschen: ${url}`);
    }
  });
}
