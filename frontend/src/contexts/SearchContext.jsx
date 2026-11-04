/**
 * @author Trung Kien Huynh
 */
import { createContext, useContext, useState } from "react";

const SearchContext = createContext(null);

export function SearchContextProvider({ children }) {
  const initialSearchFilters = Object.freeze({
    searchTerm: "",
    categoryId: 1,
    postcode: "",
    locationId: "",
    radiusKm: "",
    priceMin: "",
    priceMax: "",
  });
  const [searchFilters, setSearchFilters] = useState(initialSearchFilters);
  const [searchTrigger, setSearchTrigger] = useState(false);
  const resetSearchFilters = () => {
    setSearchFilters(initialSearchFilters);
  };

  return (
    <SearchContext.Provider
      value={{
        resetSearchFilters,
        searchFilters,
        setSearchFilters,
        searchTrigger,
        setSearchTrigger,
      }}
    >
      {children}
    </SearchContext.Provider>
  );
}

export function useSearchContext() {
  const context = useContext(SearchContext);
  if (!context) {
    throw new Error(
      "useSearchContext muss innerhalb von SearchContextProvider verwendet werden",
    );
  }
  return context;
}
