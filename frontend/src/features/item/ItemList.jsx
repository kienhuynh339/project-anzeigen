/**
 * @author Trung Kien Huynh
 */
import "../../styles/hoverEffect.css";
import ItemCard from "./ItemCard";
import { useSearchContext } from "../../contexts/SearchContext";
import ItemPagination from "./ItemPagination";
import { useEffect, useState } from "react";
import { useFetch } from "../../hooks/useFetch";

const SORT_OPTIONS = Object.freeze({
  CREATED_AT_DESC: "createdAt,desc",
  PRICE_ASC: "price,asc",
  PRICE_DESC: "price,desc",
});

export default function ItemList({
  title = "",
  pageSize = 10,
  urlWithoutPaginationAndSorting,
  showItemAmount = false,
  additionalContentWhenItemAmountIsZero = "",
  itemCardType,
}) {
  const { searchTrigger } = useSearchContext();

  // aktuelle Seite
  const [page, setPage] = useState(0);
  const [sortMode, setSortMode] = useState(SORT_OPTIONS.CREATED_AT_DESC);
  const [items, setItems] = useState([]);
  const [itemAmount, setItemAmount] = useState(0);

  // Bei einer neuen Suche oder Sortierung wieder auf Seite 1 zurückkehren
  useEffect(() => {
    setPage(0);
  }, [sortMode, searchTrigger]);

  // Anzeigen abrufen (Page<...>)
  const { data: response, loading } = useFetch(
    `${urlWithoutPaginationAndSorting}page=${page}&size=${pageSize}&sort=${sortMode}`,
    [sortMode, page, searchTrigger],
  );

  useEffect(() => {
    setItems(response?.content ?? []);
    setItemAmount(response?.totalElements ?? 0);
  }, [response]);

  if (loading) {
    return <p>Lade...</p>;
  }

  const deleteItem = (itemId) => {
    setItems((prev) => prev.filter((item) => item.id !== itemId));
    setItemAmount((prev) => prev - 1);
  };

  const updateItemStatus = (itemId, itemStatus) => {
    setItems((prev) =>
      prev.map((item) =>
        item.id === itemId ? { ...item, status: itemStatus } : item,
      ),
    );
  };

  return (
    <div className="p-2">
      <div
        className="d-flex justify-content-between
                      align-items-center mb-1"
      >
        {/* Titel */}
        <h2 className="ps-1 m-0 d-flex align-items-center">
          {showItemAmount ? (
            <span style={{ fontSize: "1rem", fontWeight: "normal" }}>
              {itemAmount} Ergebnisse gefunden
            </span>
          ) : (
            title
          )}
        </h2>
        {/* Sortierung */}
        {itemAmount > 0 && (
          <div>
            Sortieren:{" "}
            <select
              className="border-dark-subtle rounded"
              value={sortMode}
              onChange={(e) => setSortMode(e.target.value)}
            >
              <option value={SORT_OPTIONS.CREATED_AT_DESC}>Neueste</option>
              <option value={SORT_OPTIONS.PRICE_ASC}>Niedrigster Preis</option>
              <option value={SORT_OPTIONS.PRICE_DESC}>Höchster Preis</option>
            </select>
          </div>
        )}
      </div>
      <div className="d-flex gap-2 flex-wrap justify-content-center">
        {/* Anderer Inhalt, wenn es keine Anzeigen gibt */}
        {itemAmount == 0 && additionalContentWhenItemAmountIsZero}
        {/* Anzeigen */}
        {items.map((item) => (
          <ItemCard
            key={item.id}
            item={item}
            itemCardType={itemCardType}
            deleteItem={deleteItem}
            updateItemStatus={updateItemStatus}
          />
        ))}
      </div>
      {/* Pagination */}
      <ItemPagination
        page={page}
        setPage={setPage}
        itemAmount={itemAmount}
        pageSize={pageSize}
      ></ItemPagination>
    </div>
  );
}
