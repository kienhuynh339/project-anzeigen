import { useEffect, useState } from "react";

export function useFetch(url, dependencies) {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw Error(`Fehler beim Datenabruf: ${url}`);
        }
        return response.json();
      })
      .then((data) => {
        setData(data);
      });
  }, dependencies);

  return data;
}
