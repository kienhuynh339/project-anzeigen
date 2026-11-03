export function updateItemRequest(itemId, formData) {
  return fetch(`http://localhost:8080/api/items/${itemId}`, {
    method: "PUT",
    credentials: "include",
    body: formData,
  }).catch((error) => console.error(error));
}
