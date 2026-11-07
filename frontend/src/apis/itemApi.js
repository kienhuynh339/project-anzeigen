/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";

export function deleteItem(itemId) {
  return request(`/items/${itemId}`, {
    method: "DELETE",
  });
}

export function updateItemStatus(itemId, itemStatus) {
  const body = { status: itemStatus };
  return request(`/items/${itemId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function createItem(
  title,
  categoryId,
  description,
  locationId,
  postcode,
  locationExtraInfo,
  price,
  status,
  images,
) {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("categoryId", categoryId);
  formData.append("description", description);
  formData.append("locationId", locationId);
  formData.append("postcode", postcode);
  formData.append("locationExtraInfo", locationExtraInfo);
  formData.append("price", price);
  formData.append("status", status);
  images.forEach((image) => {
    formData.append("images", image);
  });
  return request(`/items`, {
    method: "POST",
    body: formData,
  });
}

export function updateItem(
  itemId,
  title,
  categoryId,
  description,
  locationId,
  postcode,
  locationExtraInfo,
  price,
  status,
  images,
) {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("categoryId", categoryId);
  formData.append("description", description);
  formData.append("locationId", locationId);
  formData.append("postcode", postcode);
  formData.append("locationExtraInfo", locationExtraInfo);
  formData.append("price", price);
  formData.append("status", status);
  images.forEach((image) => {
    formData.append("images", image);
  });
  return request(`/items/${itemId}`, {
    method: "PUT",
    body: formData,
  });
}
