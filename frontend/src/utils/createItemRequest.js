export function createItemRequest(formData) {
  return fetch("http://localhost:8080/api/items", {
    method: "POST",
    credentials: "include",
    body: formData,
  }).catch((error) => console.error(error));
}
