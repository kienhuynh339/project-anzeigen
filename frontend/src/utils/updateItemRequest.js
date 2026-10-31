export function updateItemRequest(itemId, loggedInUserId, formData) {
  return fetch(
    `http://localhost:8080/api/items/${itemId}?loggedInUserId=${loggedInUserId}`,
    { method: "PUT", body: formData },
  ).catch((error) => console.error(error));
}
