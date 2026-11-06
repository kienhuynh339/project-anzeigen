import { createContext, useContext, useState } from "react";

const ItemListContext = createContext(null);

export function ItemListContextProvider({ children }) {
  const [items, setItems] = useState([]);
  const [totalElements, setTotalElements] = useState(0);

  const deleteItem = (itemId) => {
    setItems((prev) => prev.filter((item) => item.id !== itemId));
    setTotalElements((prev) => prev - 1);
  };

  const updateItemStatus = (itemId, itemStatus) => {
    setItems((prev) =>
      prev.map((item) =>
        item.id === itemId ? { ...item, status: itemStatus } : item,
      ),
    );
  };

  return (
    <ItemListContext.Provider
      value={{
        items,
        setItems,
        deleteItem,
        updateItemStatus,
        totalElements,
        setTotalElements,
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
