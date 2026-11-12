/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../components/DialogWindow";
import { useState } from "react";
import { deleteItem as apiDeleteItem } from "../../apis/itemApi";
import ErrorDialog from "../../components/ErrorDialog";
import { useAppContext } from "../../contexts/AppContext";

export default function ItemDeleteDialog({ itemId, deleteItem, closeDialog }) {
  const { openDialog } = useAppContext();

  const [isDeleting, setIsDeleting] = useState(false);

  const handleDeleteItem = async (event) => {
    setIsDeleting(true);
    event.preventDefault();

    try {
      await apiDeleteItem(itemId);
      deleteItem();
      closeDialog();
    } catch (e) {
      openDialog(ErrorDialog, { errorMessage: e.message });
    }

    // setIsDeleting(false);
  };

  return (
    <DialogWindow
      dialogWidth={"50ch"}
      dialogTitle={"Anzeige löschen"}
      dialogButtons={[
        <button // Abbrechen
          key="item-delete-cancel"
          type="button"
          className="btn btn-secondary"
          onClick={(event) => {
            event.preventDefault();
            closeDialog();
          }}
        >
          Abbrechen
        </button>,
        <button // Anzeige löschen
          key="item-delete-confirm"
          type="button"
          className="btn btn-danger"
          onClick={handleDeleteItem}
        >
          Löschen
        </button>,
      ]}
    >
      {isDeleting && <p>Anzeige wird gelöscht ...</p>}
    </DialogWindow>
  );
}
