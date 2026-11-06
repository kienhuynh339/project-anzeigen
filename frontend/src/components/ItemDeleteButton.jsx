/**
 * @author Trung Kien Huynh
 */
import ItemDeleteDialog from "./ItemDeleteDialog";
import { useAppContext } from "../contexts/AppContext";

export default function ItemDeleteButton({ itemId, deleteItem, children }) {
  const { openDialog } = useAppContext();

  const handleOnClick = (event) => {
    event.preventDefault();
    openDialog(ItemDeleteDialog, { itemId, deleteItem });
  };
  return (
    <button
      type="button"
      className="btn btn-outline-danger"
      onClick={handleOnClick}
    >
      <i className="bi bi-trash"></i>
      {children}
    </button>
  );
}
