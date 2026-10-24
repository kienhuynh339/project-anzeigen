import { Link } from "react-router";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";

export default function ItemCard({ item }) {
  return (
    <Link
      className="text-decoration-none hover-effect p-0"
      to={`/anzeigen/${item.id}`}
      style={{ width: "22rem" }}
    >
      <div className="card shadow-sm h-100">
        <img
          src={`http://localhost:8080/api/images/item/${item.id}/first`}
          className="card-img-top"
          alt={`item-${item.id}-image`}
          loading="lazy"
          style={{ objectFit: "cover", height: "22rem" }}
        />

        <div className="card-body d-flex flex-column justify-content-between pt-0">
          <div className="container-fluid p-0 m-0">
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-geo-alt-fill text-body-tertiary"></i>
              <p className="card-text text-secondary m-0">
                {`${item.location.plz} ${item.location.name} ${item.distance ? `(${item.distance.toFixed(2)} km)` : ""}`}
              </p>
            </div>
            <h5 className="card-title">
              {item.title.length > 80
                ? item.title.slice(0, 80) + "..."
                : item.title}
            </h5>
          </div>

          <div className="d-flex justify-content-between align-items-end">
            <p className="fs-5 fw-bold m-0">
              {item.price == 0
                ? ""
                : item.price.toLocaleString("de-DE", {
                    style: "currency",
                    currency: "EUR",
                  })}
            </p>
            <p className="text-secondary m-0">
              {formatTimestamp(item.createdAt, TimestampType.ITEM)}
            </p>
          </div>
        </div>
      </div>
    </Link>
  );
}
