import { createContext, useContext, useState } from "react";

const ItemListContext = createContext(null);

export function ItemListContextProvider({ children }) {
  return <ItemListContext.Provider>{children}</ItemListContext.Provider>;
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
