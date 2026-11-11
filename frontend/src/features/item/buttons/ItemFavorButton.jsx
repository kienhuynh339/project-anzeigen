/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import {
  deleteFavoriteItem,
  addFavoriteItem,
} from "../../../apis/favoriteItemApi";
import { useEffect } from "react";
import { useAppContext } from "../../../contexts/AppContext";
import ErrorDialog from "../../../components/ErrorDialog";

export default function ItemFavorButton({
  itemId,
  isFavorite,
  className = "btn btn-outline-danger",
  style,
  showLabel = false,
}) {
  const { openDialog } = useAppContext();

  const [isButtonDisabled, setIsButtonDisabled] = useState(false);
  const [favorite, setFavorite] = useState(isFavorite);

  useEffect(() => {
    setFavorite(isFavorite);
  }, [isFavorite]);

  const handleClick = async (event) => {
    event.preventDefault();
    setIsButtonDisabled(true);

    if (favorite) {
      try {
        await deleteFavoriteItem(itemId);
        setFavorite(false);
      } catch (e) {
        openDialog(ErrorDialog, { errorMessage: e.message });
      }
    } else {
      try {
        await addFavoriteItem(itemId);
        setFavorite(true);
      } catch (e) {
        openDialog(ErrorDialog, { errorMessage: e.message });
      }
    }

    setIsButtonDisabled(false);
  };

  return (
    <button
      className={className}
      style={style}
      onClick={handleClick}
      disabled={isButtonDisabled}
    >
      <i
        className={` bi bi-heart${favorite ? "-fill" : ""} ${!showLabel && (favorite ? "text-danger" : "")}`}
      ></i>
      {showLabel &&
        (favorite ? " Von Merkliste entfernen" : " Zur Merkliste hinzufügen")}
    </button>
  );
}
