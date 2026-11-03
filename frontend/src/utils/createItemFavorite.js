/**
 * @author Trung Kien Huynh
 */

import { createRequest } from "./createRequest";

export function createItemFavorite(itemId) {
  const newItemFavorite = { itemId: itemId };
  return createRequest(
    "http://localhost:8080/api/item-favorites",
    newItemFavorite,
  );
}
