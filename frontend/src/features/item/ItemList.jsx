/**
 * @author Trung Kien Huynh
 */
import "../../styles/hoverEffect.css";
import ItemCard from "./ItemCard";
import { useItemListContext } from "../../contexts/ItemListContext";
import { useSearchContext } from "../../contexts/SearchContext";
import ItemPagination from "./ItemPagination";
import { useEffect, useState } from "react";
import { useFetch } from "../../hooks/useFetch";

const sortOption = Object.freeze({
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
  const { items, setItems } = useItemListContext();
  const { searchTrigger } = useSearchContext();

  const [page, setPage] = useState(0);
  const [sortMode, setSortMode] = useState(sortOption.CREATED_AT_DESC);
  const [totalElements, setTotalElements] = useState(0);

  // Bei einer neuen Suche oder Sortierung wieder auf Seite 1.
  useEffect(() => {
    setPage(0);
  }, [sortMode, searchTrigger]);

  const { data: response } = useFetch(
    `${urlWithoutPaginationAndSorting}page=${page}&size=${pageSize}&sort=${sortMode}`,
    [sortMode, page, searchTrigger],
  );

  useEffect(() => {
    setItems(response?.content ?? []);
    // Aktualisiert totalElements anhand von response nur bei Änderung an response
    setTotalElements(response?.totalElements ?? 0);
  }, [response]);

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
              {totalElements} Ergebnisse gefunden
            </span>
          ) : (
            title
          )}
        </h2>
        {/* Sortierung */}
        {totalElements > 0 && (
          <div>
            Sortieren:{" "}
            <select
              className="border-dark-subtle rounded"
              value={sortMode}
              onChange={(e) => setSortMode(e.target.value)}
            >
              <option value={sortOption.CREATED_AT_DESC}>Neueste</option>
              <option value={sortOption.PRICE_ASC}>Niedrigster Preis</option>
              <option value={sortOption.PRICE_DESC}>Höchster Preis</option>
            </select>
          </div>
        )}
      </div>

      <div className="d-flex gap-2 flex-wrap justify-content-center">
        {/* Anderer Inhalt, wenn es keine Anzeigen gibt */}
        {totalElements == 0 && additionalContentWhenItemAmountIsZero}
        {/* Anzeigen */}
        {items.map((item) => (
          <ItemCard key={item.id} item={item} itemCardType={itemCardType} />
        ))}
      </div>
      {/* Pagination */}
      <ItemPagination
        page={page}
        setPage={setPage}
        totalElements={totalElements}
        pageSize={pageSize}
      ></ItemPagination>
    </div>
  );
}
