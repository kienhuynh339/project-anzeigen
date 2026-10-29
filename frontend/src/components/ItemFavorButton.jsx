/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { createItemFavorite } from "../utils/createItemFavorite";
import { deleteRequest } from "../utils/deleteRequest";
import { useEffect } from "react";
export default function ItemFavorButton({
  userId,
  itemId,
  isFavorite,
  className = "btn btn-outline-danger",
  style,
  showLabel = false,
}) {
  const [isFavoriteChanging, setIsFavoriteChanging] = useState(false);
  const [favorite, setFavorite] = useState(isFavorite);
  useEffect(() => {
    setFavorite(isFavorite);
  }, [isFavorite]);
  const handleFavoriteButtonClick = async (event) => {
    event.preventDefault();
    setIsFavoriteChanging(true);
    if (favorite) {
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
