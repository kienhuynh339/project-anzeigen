/**
 * @author Trung Kien Huynh
 */
import { deleteFavoriteItem } from "../api/favoriteItemApi";
export default function FavoriteItemDeleteButton({ itemId }) {
  const handleOnclick = async (event) => {
    event.preventDefault();
    await deleteFavoriteItem(itemId).catch((error) => {
      console.log(error);
    });
  };
  return (
    <button
      type="button"
      className="btn btn-outline-danger"
      onClick={handleOnclick}
    >
      <i className="bi bi-trash"></i>
    </button>
  );
}
