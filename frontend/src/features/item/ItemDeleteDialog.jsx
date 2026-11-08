/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../components/DialogWindow";
import { useState } from "react";
import { deleteItem as apiDeleteItem } from "../../apis/itemApi";

export default function ItemDeleteDialog({ itemId, deleteItem, closeDialog }) {
  const [deleting, setDeleting] = useState(false);

  const handleItemDelete = async (event) => {
    setDeleting(true);
    event.preventDefault();

    await apiDeleteItem(itemId).catch((e) => console.error(e));
    deleteItem();
    closeDialog();

    setDeleting(false);
  };

  return (
    <DialogWindow
      dialogWidth={"50ch"}
      dialogTitle={"Anzeige löschen"}
      dialogButtons={[
        <button
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
        <button
          key="item-delete-confirm"
          type="button"
          className="btn btn-danger"
          onClick={handleItemDelete}
        >
          Löschen
        </button>,
      ]}
    >
      {deleting && <p>Anzeige wird gelöscht ...</p>}
    </DialogWindow>
  );
}
