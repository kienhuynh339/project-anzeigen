import { createContext, useContext, useRef, useState, useEffect } from "react";
import { useSearchContext } from "./SearchContext";
import { getPath } from "../hooks/useFetch";
import { useFetch } from "../hooks/useFetch";

const ItemListContext = createContext(null);

const sortOption = Object.freeze({
  CREATED_AT_DESC: "createdAt,desc",
  PRICE_ASC: "price,asc",
  PRICE_DESC: "price,desc",
});

export function ItemListContextProvider({ children }) {
  const { searchTrigger } = useSearchContext();

  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);
  const [urlWithoutPaginationAndSorting, setUrlWithoutPaginationAndSorting] =
    useState(getPath.items("", "", "", "", "", ""));
  const [sortMode, setSortMode] = useState(sortOption.CREATED_AT_DESC);
  const [totalElements, setTotalElements] = useState(0);

  const pageSize = useRef(10);

  // Bei einer neuen Suche oder Sortierung wieder auf Seite 1.
  useEffect(() => {
    setPage(0);
  }, [urlWithoutPaginationAndSorting, sortMode, searchTrigger]);

  const { data: response } = useFetch(
    `${urlWithoutPaginationAndSorting}page=${page}&size=${pageSize.current}&sort=${sortMode}`,
    [urlWithoutPaginationAndSorting, sortMode, page, searchTrigger],
  );

  useEffect(() => {
    setItems(response?.content ?? []);
    // Aktualisiert totalElements anhand von response nur bei Änderung an response
    setTotalElements(response?.totalElements ?? 0);
  }, [response]);

  const totalPages = Math.ceil(totalElements / pageSize);

  // Aktuelle Seite innerhalb der gültigen Grenzen halten.
  const currentPage = Math.min(totalPages, Math.max(0, page));

  // Seitenzahlen für die Pagination erzeugen.
  const visiblePages = Array.from(
    { length: totalPages },
    (_, index) => index,
  ).filter(
    (pageNumber) =>
      pageNumber === 0 ||
      pageNumber === totalPages - 1 ||
      Math.abs(pageNumber - currentPage) <= 2,
  );
  return (
    <ItemListContext.Provider
      value={{
        items,
        totalElements,
        sortMode,
        setSortMode,
        sortOption,
        totalPages,
        currentPage,
        visiblePages,
        setPage,
      }}
    >
      {children}
    </ItemListContext.Provider>
  );
}

export function useItemListContext() {
  const context = useContext(ItemListContext);
  if (!context) {
    throw new Error(
      "useItemListContext muss innerhalb von ItemListContextProvider verwendet werden",
    );
  }
  return context;
}
