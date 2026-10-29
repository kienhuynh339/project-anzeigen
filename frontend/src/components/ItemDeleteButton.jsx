/**
 * @author Trung Kien Huynh
 */
import ItemDeleteDialog from "./ItemDeleteDialog";
import { useAppContext } from "../AppContext";
export default function ItemDeleteButton({ itemId, children }) {
  const { openDialog } = useAppContext();
  const handleOnClick = (event) => {
    event.preventDefault();
    openDialog(ItemDeleteDialog, { itemId });
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
