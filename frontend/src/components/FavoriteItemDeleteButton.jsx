/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { deleteFavoriteItem } from "../api/favoriteItemApi";

export default function FavoriteItemDeleteButton({ itemId, deleteItem }) {
  const [deletingFavorite, setDeletingFavorite] = useState(false);

  const handleOnclick = async (event) => {
    setDeletingFavorite(true);

    event.preventDefault();
    await deleteFavoriteItem(itemId).catch((error) => {
      console.error(error);
    });
    deleteItem(itemId);

    setDeletingFavorite(false);
  };
  return (
    <button
      type="button"
      className="btn btn-outline-danger"
      onClick={handleOnclick}
      disabled={deletingFavorite}
    >
      <i className="bi bi-trash"></i>
    </button>
  );
}
