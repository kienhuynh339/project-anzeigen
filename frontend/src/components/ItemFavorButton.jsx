/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { createItemFavorite } from "../utils/createItemFavorite";
import { deleteRequest } from "../utils/deleteRequest";
export default function ItemFavorButton({ userId, itemId, isFavorite }) {
  const [isFavoriteChanging, setIsFavoriteChanging] = useState(false);
  const [favorite, setFavorite] = useState(isFavorite);
  const handleFavoriteButtonClick = async (event) => {
    event.preventDefault();
    setIsFavoriteChanging(true);
    if (isFavorite) {
      await deleteRequest(
        `http://localhost:8080/api/item-favorites/user/${userId}/item/${itemId}`,
      )
        .then(() => {
          setFavorite(false);
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      await createItemFavorite(itemId, userId)
        .then(() => {
          setFavorite(true);
        })
        .catch((error) => {
          console.log(error);
        });
    }
    setIsFavoriteChanging(false);
  };
  return (
    <button
      className="top-0 end-0 m-1 rounded position-absolute btn"
      style={{ backgroundColor: "#9d9d9db9" }}
      onClick={handleFavoriteButtonClick}
      disabled={isFavoriteChanging}
    >
      <i
        className={` bi bi-heart${favorite ? "-fill" : ""} ${favorite ? "text-danger" : ""}`}
      ></i>
    </button>
  );
}
