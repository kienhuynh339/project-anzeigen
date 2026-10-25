import { useState, useEffect } from "react";
import "../../styles/hoverEffect.css";
import { useFetch } from "../../hooks/useFetch";
import ItemCard from "./ItemCard";
import { useNavigate } from "react-router";

const userId = "1";

export default function MyFavoriteItems() {
  const navigate = useNavigate();
  const PAGE_SIZE = 9999;
  const sortOption = Object.freeze({
    CREATED_AT_DESC: "createdAt,desc",
    PRICE_ASC: "price,asc",
    PRICE_DESC: "price,desc",
  });
  const [sortMode, setSortMode] = useState(sortOption.CREATED_AT_DESC);

  const response = useFetch(
    `http://localhost:8080/api/items/user/${userId}/favorites?page=0&size=${PAGE_SIZE}&sort=${sortMode}`,
    [sortMode],
  );
  const items = response?.content ?? [];

  return (
    <div className="p-2">
      <div
        className="d-flex justify-content-between
                          align-items-center mb-1"
      >
        <h2 className="ps-1 m-0">Meine Merkliste</h2>
        {items.length > 0 ? (
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
        ) : (
          ""
        )}
      </div>

      <div className="d-flex gap-2 flex-wrap justify-content-center">
        {items.length > 0 ? (
          ""
        ) : (
          <div className="d-flex flex-column gap-2 align-items-center">
            <div>Deine Merkliste ist leer.</div>
            <button
              className="btn btn-outline-primary"
              onClick={() => navigate("/")}
            >
              Anzeigen durchsuchen
            </button>
          </div>
        )}
        {items.map((item) => (
          <ItemCard key={item.id} item={item} />
        ))}
      </div>
    </div>
  );
}
