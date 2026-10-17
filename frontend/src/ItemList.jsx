import { useState, useEffect } from "react";
import "./hoverEffect.css";

export default function ItemList() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/anzeigen") // return: ein Response-Objekt
      .then((response) => {
        if (!response.ok) {
          throw new Error("Fehler beim Laden der Anzeigen");
        }
        return response.json(); // JSON in JS-Wert
      }) // ein Promise???
      .then((data) => {
        setItems(data); // data enthält den JS-Wert
      });
  }, []);

  return (
    <div className="p-2">
      <div
        className="d-flex justify-content-between
                      align-items-center mb-1"
      >
        <span className="mb-0">
          {items.length} Ergebnisse für „...“ in der Kategorie „...“ in
          "..ort.." gefunden.
        </span>
      </div>

      <div className="d-flex gap-3 flex-wrap justify-content-center">
        {items.map((item) => (
          <ItemCard key={item.id} item={item} />
        ))}
      </div>
    </div>
  );
}

function ItemCard({ item }) {
  return (
    <a
      className="text-decoration-none hover-effect p-0"
      href="#"
      style={{ width: "24%" }}
    >
      <div className="card shadow-sm ">
        <img
          src={`http://localhost:8080/api/anzeigen/${item.id}/bilder/erstes`}
          className="card-img-top"
          alt={`item-${item.id}-image`}
          loading="lazy"
          style={{ objectFit: "cover", height: "40ch" }}
        />

        <div className="card-body d-flex flex-column justify-content-between pt-0">
          <div className="container-fluid p-0 m-0">
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-geo-alt-fill text-body-tertiary"></i>
              <p className="card-text text-secondary m-0">
                {item.location.ort}
              </p>
            </div>
            <h5 className="card-title">
              {item.title.length > 80
                ? item.title.slice(0, 80) + "..."
                : item.title}
            </h5>
          </div>

          <div className="d-flex justify-content-between">
            <p className="fs-5 fw-bold m-0">
              {item.price.toLocaleString("de-DE", {
                style: "currency",
                currency: "EUR",
              })}
            </p>
            <p className="text-secondary m-0">{item.createdAt}</p>
          </div>
        </div>
      </div>
    </a>
  );
}
