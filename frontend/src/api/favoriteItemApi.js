/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";

export function addFavoriteItem(itemId) {
  const body = { itemId: itemId };
  return request(`/item-favorites`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function deleteFavoriteItem(itemId) {
  return request(`/item-favorites/item/${itemId}`, {
    method: "DELETE",
  });
}
