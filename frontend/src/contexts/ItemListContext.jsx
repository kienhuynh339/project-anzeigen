import { createContext, useContext, useState, useEffect } from "react";
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
  const [pageSize, setPageSize] = useState(10);

  // Bei einer neuen Suche oder Sortierung wieder auf Seite 1.
  useEffect(() => {
    setPage(0);
  }, [urlWithoutPaginationAndSorting, sortMode, searchTrigger]);

  const { data: response } = useFetch(
    `${urlWithoutPaginationAndSorting}page=${page}&size=${pageSize}&sort=${sortMode}`,
    [urlWithoutPaginationAndSorting, sortMode, page, searchTrigger],
  );

  useEffect(() => {
    setItems(response?.content ?? []);
    // Aktualisiert totalElements anhand von response nur bei Änderung an response
    setTotalElements(response?.totalElements ?? 0);
  }, [response]);

  return (
    <ItemListContext.Provider
      value={{
        items,
        totalElements,
        pageSize,
        sortMode,
        setSortMode,
        sortOption,
        page,
        setPage,
        setPageSize,
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
