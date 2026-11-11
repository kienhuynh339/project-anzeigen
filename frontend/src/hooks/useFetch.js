/**
 * @author Trung Kien Huynh
 */
import { useEffect, useState } from "react";
const API_URL = "http://localhost:8080/api";

export const getPath = Object.freeze({
  messagesByChatId: (chatId) =>
    `/messages/chat/${chatId}?page=0&size=9999&sort=createdAt,asc`,
  chats: () => `/chats`,
  categories: () => `/categories`,
  userByUserId: (userId) => `/users/${userId}`,
  locationsByPostcode: (postcode) =>
    `/locations?postcode=${postcode}&page=0&size=15`,
  postcodesByPostcodePrefix: (postcodePrefix) =>
    `/postcodes?postcodePrefix=${postcodePrefix}&page=0&size=15`,
  itemByItemId: (itemId) => `/items/${itemId}`,
  itemByItemIdToEdit: (itemId) => `/items/${itemId}/edit`,
  items: (searchTerm, categoryId, priceMin, priceMax, locationId, radiusKm) =>
    `/items?searchTerm=${searchTerm}&categoryId=${categoryId}&minPrice=${priceMin}&maxPrice=${priceMax}&locationId=${locationId}&radiusKm=${radiusKm}&`,
  itemsByUserId: (userId) => `/items/user/${userId}?`,
  itemsFavorites: () => `/items/user/favorites?`,
  imageIdsByItemId: (itemId) => `/images/item/${itemId}`,
});

export function useFetch(url, dependencies = [], initialData = null) {
  const [data, setData] = useState(initialData);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    if (!url) return;
    setLoading(true);
    setError(false);
    fetch(`${API_URL}${url}`, {
      method: "GET",
      credentials: "include",
    })
      .then(async (response) => {
        const result = await response.json().catch(() => null);
        if (!response.ok) {
          throw Error(
            `Fehler beim Datenabruf: ${url}. ${result?.errorMessage}`,
          );
        }
        if (result === null) {
          setData(initialData);
        } else {
          setData(result);
        }
      })
      .catch((error) => {
        console.error(error);
        setError(true);
        setData(initialData);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [...dependencies]);

  return { data, loading, error };
}
