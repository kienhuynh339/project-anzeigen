/**
 * @author Trung Kien Huynh
 */
import { deleteRequest } from "../utils/deleteRequest";
export default function FavoriteItemDeleteButton({ itemId, userId }) {
  const handleOnclick = async (event) => {
    event.preventDefault();
    await deleteRequest(
      `http://localhost:8080/api/item-favorites/user/${userId}/item/${itemId}`,
    ).catch((error) => {
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
