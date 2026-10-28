/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { createItemFavorite } from "../utils/createItemFavorite";
import { deleteRequest } from "../utils/deleteRequest";
export default function ItemFavorButton({ userId, itemId }) {
  const [isFavorite, setIsFavorite] = useState(false);
  const handleFavoriteButtonClick = async (event) => {
    event.preventDefault();
    if (isFavorite) {
      await deleteRequest(
        `http://localhost:8080/api/item-favorites/user/${userId}/item/${itemId}`,
      ).catch((error) => {
        console.log(error);
      });
      setIsFavorite(false);
    } else {
      await createItemFavorite(itemId, userId);
      setIsFavorite(true);
    }
  };
  return (
    <button
      className="top-0 end-0 m-1 rounded position-absolute btn"
      style={{ backgroundColor: "#9d9d9db9" }}
      onClick={handleFavoriteButtonClick}
    >
      <i
        className={` bi bi-heart${isFavorite ? "-fill" : ""} ${isFavorite ? "text-danger" : ""}`}
      ></i>
    </button>
  );
}
