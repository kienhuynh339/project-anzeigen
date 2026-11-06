/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { deleteFavoriteItem as apiDeleteFavoriteItem } from "../api/favoriteItemApi";

export default function FavoriteItemDeleteButton({
  itemId,
  deleteItem: deleteFavoriteItem,
}) {
  const [deletingFavorite, setDeletingFavorite] = useState(false);

  const handleOnclick = async (event) => {
    setDeletingFavorite(true);

    event.preventDefault();
    await apiDeleteFavoriteItem(itemId).catch((error) => {
      console.error(error);
    });
    deleteFavoriteItem();

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
