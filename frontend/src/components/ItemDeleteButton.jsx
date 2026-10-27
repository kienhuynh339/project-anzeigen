import DialogWindow from "./DialogWindow";
import { deleteRequest } from "../utils/deleteRequest";
import { useState } from "react";

export default function ItemDeleteButton({ itemId, setChatsUpdateTrigger }) {
  const [isDialogActive, setIsDialogActive] = useState(false);
  const [isItemDeleting, setIsItemDeleting] = useState(false);
  const handleItemDelete = async () => {
    setIsItemDeleting(true);
    await deleteRequest(`http://localhost:8080/api/items/${itemId}`).catch(
      (error) => {
        console.log(error);
      },
    );
    setIsDialogActive(false);
    setChatsUpdateTrigger((prev) => !prev);
    setIsItemDeleting(false);
  };
  return (
    <>
      <button
        type="button"
        className="btn btn-outline-danger"
        onClick={() => setIsDialogActive(true)}
      >
        <i className="bi bi-trash"></i> Anzeige löschen
      </button>
      <DialogWindow
        dialogWidth={"50ch"}
        isOpen={isDialogActive}
        dialogTitle={"Anzeige löschen"}
        dialogContent={isItemDeleting && <p>Anzeige wird gelöscht ...</p>}
        dialogButtons={[
          <button
            key="dialog_button_1"
            type="button"
            className="btn btn-secondary"
            onClick={() => setIsDialogActive(false)}
          >
            Abbrechen
          </button>,
          <button
            key="dialog_button_2"
            type="button"
            className="btn btn-danger"
            onClick={handleItemDelete}
          >
            Löschen
          </button>,
        ]}
      ></DialogWindow>
    </>
  );
}
