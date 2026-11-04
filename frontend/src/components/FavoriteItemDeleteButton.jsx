/**
 * @author Trung Kien Huynh
 */
import { deleteFavoriteItem } from "../api/favoriteItemApi";
import { useAuth } from "../contexts/AuthContext";
export default function FavoriteItemDeleteButton({ itemId }) {
  const { user } = useAuth();
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
