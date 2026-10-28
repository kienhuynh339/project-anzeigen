/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "./DialogWindow";
import { useState } from "react";
import { deleteRequest } from "../utils/deleteRequest";
export default function ItemDeleteDialog({ itemId, closeDialog }) {
  const [isItemDeleting, setIsItemDeleting] = useState(false);
  const handleItemDelete = async (event) => {
    event.preventDefault();
    setIsItemDeleting(true);
    await deleteRequest(`http://localhost:8080/api/items/${itemId}`).catch(
      (error) => {
        console.log(error);
      },
    );
    setIsItemDeleting(false);
    closeDialog();
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
      {isItemDeleting && <p>Anzeige wird gelöscht ...</p>}
    </DialogWindow>
  );
}
