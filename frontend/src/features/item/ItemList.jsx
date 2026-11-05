/**
 * @author Trung Kien Huynh
 */
import "../../styles/hoverEffect.css";
import ItemCard from "./ItemCard";
import { useItemListContext } from "../../contexts/ItemListContext";

export default function ItemList({
  title = "",
  showItemAmount = false,
  additionalContentWhenItemAmountIsZero = "",
  itemCardType,
}) {
  const {
    items,
    totalElements,
    sortMode,
    setSortMode,
    sortOption,
    totalPages,
    currentPage,
    visiblePages,
    setPage,
  } = useItemListContext();
  return (
    <div className="p-2">
      <div
        className="d-flex justify-content-between
                      align-items-center mb-1"
      >
        <h2 className="ps-1 m-0 d-flex align-items-center">
          {showItemAmount ? (
            <span style={{ fontSize: "1rem", fontWeight: "normal" }}>
              {totalElements} Ergebnisse gefunden
            </span>
          ) : (
            title
          )}
        </h2>
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
        {totalElements == 0 && additionalContentWhenItemAmountIsZero}
        {items.map((item) => (
          <ItemCard key={item.id} item={item} itemCardType={itemCardType} />
        ))}
      </div>
      {/* AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA */}
      {totalPages > 1 && (
        <nav className="mt-4">
          <ul className="pagination gap-2 justify-content-center align-items-center flex-wrap">
            {/* Vorherige Seite */}
            <li
              className={`page-item ${currentPage === 0 ? "disabled" : null}`}
            >
              <button
                type="button"
                className="page-link"
                disabled={currentPage === 0}
                onClick={() => setPage(currentPage - 1)}
              >
                <i className="bi bi-arrow-left"></i>
              </button>
            </li>

            {/* Seitenzahlen */}
            {visiblePages.map((pageNumber, index) => {
              const previousPage = visiblePages[index - 1];

              return (
                <div key={pageNumber} className="d-flex gap-2">
                  {previousPage !== undefined &&
                    pageNumber - previousPage > 1 && <span>…</span>}

                  <li
                    className={` ${currentPage === pageNumber ? "active" : null}`}
                  >
                    <button
                      type="button"
                      className="page-link"
                      onClick={() => setPage(pageNumber)}
                    >
                      {pageNumber + 1}
                    </button>
                  </li>
                </div>
              );
            })}

            {/* Nächste Seite */}
            <li
              className={`page-item ${
                currentPage >= totalPages - 1 ? "disabled" : null
              }`}
            >
              <button
                type="button"
                className="page-link"
                disabled={currentPage >= totalPages - 1}
                onClick={() => setPage(currentPage + 1)}
              >
                <i className="bi bi-arrow-right"></i>
              </button>
            </li>
          </ul>
        </nav>
      )}
    </div>
  );
}
