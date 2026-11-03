/**
 * @author Trung Kien Huynh
 */
import { useEffect, useState } from "react";
const API_URL = "http://localhost:8080/api";
export function useFetch(url, dependencies, initialData = null) {
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
          throw Error(result?.errorMessage || `Fehler beim Datenabruf: ${url}`);
        }
        setData(result);
      })
      .catch((error) => {
        console.log(error);
        setError(true);
        setData(null);
      })
      .finally(() => {
        setLoading(false);
      });
  }, dependencies);

  return { data, loading, error };
}
