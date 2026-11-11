/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { deleteFavoriteItem as apiDeleteFavoriteItem } from "../../../apis/favoriteItemApi";
import ErrorDialog from "../../../components/ErrorDialog";
import { useAppContext } from "../../../contexts/AppContext";

export default function FavoriteItemDeleteButton({
  itemId,
  deleteFavoriteItem,
}) {
  const { openDialog } = useAppContext();

  const [isDeleting, setIsDeleting] = useState(false);

  const handleDeleteFavoriteItem = async (event) => {
    setIsDeleting(true);
    event.preventDefault();

    try {
      await apiDeleteFavoriteItem(itemId);
      deleteFavoriteItem();
    } catch (e) {
      openDialog(ErrorDialog, { errorMessage: e.message });
    }

    setIsDeleting(false);
  };
  return (
    <button
      type="button"
      className="btn btn-outline-danger"
      onClick={handleDeleteFavoriteItem}
      disabled={isDeleting}
    >
      <i className="bi bi-trash"></i>
    </button>
  );
}
