/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "./DialogWindow";
import { deleteRequest } from "../utils/deleteRequest";
import { useState } from "react";

export default function ItemDeleteButton({ itemId }) {
  const [isDialogActive, setIsDialogActive] = useState(false);
  const [isItemDeleting, setIsItemDeleting] = useState(false);
  const handleItemDelete = async (event) => {
    event.preventDefault();
    setIsItemDeleting(true);
    await deleteRequest(`http://localhost:8080/api/items/${itemId}`).catch(
      (error) => {
        console.log(error);
      },
    );
    setIsDialogActive(false);
    setIsItemDeleting(false);
  };
  return (
    <>
      <button
        type="button"
        className="btn btn-outline-danger"
        onClick={(event) => {
          event.preventDefault();
          setIsDialogActive(true);
        }}
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
            onClick={(event) => {
              event.preventDefault();
              setIsDialogActive(false);
            }}
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
