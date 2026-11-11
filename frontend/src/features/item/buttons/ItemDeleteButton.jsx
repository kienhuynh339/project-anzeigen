/**
 * @author Trung Kien Huynh
 */
import ItemDeleteDialog from "../ItemDeleteDialog";
import { useAppContext } from "../../../contexts/AppContext";

export default function ItemDeleteButton({
  itemId,
  deleteItem = () => {},
  children,
}) {
  const { openDialog } = useAppContext();

  const handleDeleteItem = (event) => {
    event.preventDefault();
    openDialog(ItemDeleteDialog, { itemId, deleteItem });
  };

  return (
    <button
      type="button"
      className="btn btn-outline-danger"
      onClick={handleDeleteItem}
    >
      <i className="bi bi-trash"></i>
      {children}
    </button>
  );
}
