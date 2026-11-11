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

  const [isFavoriteChanging, setIsFavoriteChanging] = useState(false);
  const [favorite, setFavorite] = useState(isFavorite);

  useEffect(() => {
    setFavorite(isFavorite);
  }, [isFavorite]);

  const handleFavoriteButtonClick = async (event) => {
    event.preventDefault();
    setIsFavoriteChanging(true);
    if (favorite) {
      try {
        await deleteFavoriteItem(itemId);
        setFavorite(false);
      } catch (e) {
        console.error(error);
      }
    } else {
      try {
        await addFavoriteItem(itemId);
        setFavorite(true);
      } catch (e) {
        const errorMessage = e.message;
        openDialog(ErrorDialog, { errorMessage });
        console.error(e);
      }
    }
    setIsFavoriteChanging(false);
  };

  return (
    <button
      className={className}
      style={style}
      onClick={handleFavoriteButtonClick}
      disabled={isFavoriteChanging}
    >
      <i
        className={` bi bi-heart${favorite ? "-fill" : ""} ${!showLabel && (favorite ? "text-danger" : "")}`}
      ></i>
      {showLabel &&
        (favorite ? " Von Merkliste entfernen" : " Zur Merkliste hinzufügen")}
    </button>
  );
}
