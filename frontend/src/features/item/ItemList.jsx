/**
 * @author Trung Kien Huynh
 */
import "../../styles/hoverEffect.css";
import ItemCard from "./ItemCard";
import { useItemListContext } from "../../contexts/ItemListContext";
import Pagination from "../../components/Pagination";

export default function ItemList({
  title = "",
  showItemAmount = false,
  additionalContentWhenItemAmountIsZero = "",
  itemCardType,
}) {
  const { items, totalElements, sortMode, setSortMode, sortOption } =
    useItemListContext();
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
      <Pagination></Pagination>
    </div>
  );
}
