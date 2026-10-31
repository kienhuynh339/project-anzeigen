/**
 * @author Trung Kien Huynh
 */
import { useEffect, useState } from "react";

export function useFetch(url, dependencies, initialData = null) {
  const [data, setData] = useState(initialData);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    if (!url) return;
    setLoading(true);
    setError(false);
    fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw Error(`Fehler beim Datenabruf: ${url}`);
        }
        return response.json();
      })
      .then((result) => {
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
