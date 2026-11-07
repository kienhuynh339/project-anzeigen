/**
 * @author Trung Kien Huynh
 */
export default function NotFound() {
  return (
    <div className="bg-success d-flex flex-column flex-grow-1">
      <h4 className="bg-danger px-2 py-2 my-0">
        <i className="bi-exclamation-triangle pe-1"></i>
        Diese Seite gibt es nicht.
      </h4>
      <div className="d-flex flex-column flex-grow-1 bg-secondary justify-content-center align-items-center fw-bold">
        <span style={{ fontSize: "10rem", color: "#585858" }}>Fehler</span>
      </div>
    </div>
  );
}
