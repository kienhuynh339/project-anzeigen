/**
 * @author Trung Kien Huynh
 */
export default function ItemPagination({
  page,
  setPage,
  itemAmount,
  pageSize,
}) {
  const totalPages = Math.ceil(itemAmount / pageSize);

  // Aktuelle Seite innerhalb der gültigen Grenzen halten.
  const currentPage = Math.min(totalPages, Math.max(0, page));

  // Seitenzahlen für die Pagination erzeugen.
  const visiblePages = Array.from(
    { length: totalPages }, // Anzahl der Arrayelements
    (value, index) => index, // Arrayelement
  ).filter(
    (pageNumber) =>
      pageNumber === 0 || // erste Seite
      pageNumber === totalPages - 1 || // letzte Seite
      Math.abs(pageNumber - currentPage) <= 2, //  die aktuelle Seite und 2 Seiten vor und nach der aktuellen Seite
  );
  return (
    <>
      {totalPages > 1 && (
        <div className="mt-4">
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
        </div>
      )}
    </>
  );
}
