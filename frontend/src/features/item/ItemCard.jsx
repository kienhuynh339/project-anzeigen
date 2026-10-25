import { Link } from "react-router";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import { useState } from "react";

export default function ItemCard({ item }) {
  const [isFavorite, setIsFavorite] = useState(false);
  const [isBooked, setIsBooked] = useState(false);

  const handleFavoriteButtonClick = (event) => {
    event.stopPropagation();
    event.preventDefault();
    setIsFavorite(!isFavorite);
    // UPDATE-Anfrage an Backend
  };

  const handleDeleteButtonClick = (event) => {
    event.stopPropagation();
    event.preventDefault();
    // DELETE-Anfrage an Backend (für Favorite/Item)
  };

  const handleMessageButtonClick = (event) => {
    event.stopPropagation();
    event.preventDefault();
    // Message...
  };

  const handleBookButtonClick = (event) => {
    event.stopPropagation();
    event.preventDefault();
    setIsBooked(!isBooked);
    // UPDATE-Anfrage an Backend
  };

  return (
    <Link
      className="text-decoration-none hover-effect p-0"
      to={`/anzeigen/${item.id}`}
      style={{ width: "22rem" }}
    >
      <div className="card shadow-sm h-100 position-relative">
        {/* Favorite-Knopf */}
        <button
          className="top-0 end-0 m-1 rounded position-absolute btn"
          style={{ backgroundColor: "#9d9d9db9" }}
          onClick={handleFavoriteButtonClick}
        >
          <i
            className={` bi bi-heart${isFavorite ? "-fill" : ""} ${isFavorite ? "text-danger" : ""}`}
          ></i>
        </button>
        {/* Bild */}
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

          <div className="d-flex flex-column gap-2">
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
            <div className="d-flex justify-content-between">
              {/* <button
                type="button"
                className="btn btn-outline-danger"
                onClick={handleDeleteButtonClick}
              >
                <i className="bi bi-trash"></i>
              </button>
              <button
                type="button"
                className="btn btn-outline-primary"
                onClick={handleMessageButtonClick}
              >
                <i className="bi bi-chat-text"></i>
              </button> */}
              <button
                type="button"
                className="btn btn-outline-danger"
                onClick={handleDeleteButtonClick}
              >
                <i className="bi bi-trash"></i>
              </button>
              <button
                type="button"
                className={`btn btn${isBooked ? "-outline-success" : "-outline-primary"}`}
                onClick={handleBookButtonClick}
              >
                {isBooked ? "Aktivieren" : "Reservieren"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </Link>
  );
}
