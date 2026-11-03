/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "./DialogWindow";
import { useState } from "react";
import { deleteRequest } from "../utils/deleteRequest";
import { useAuth } from "../contexts/AuthContext";
export default function ChatDeleteDialog({
  setIsChatDeleteActive,
  setDeleteList,
  deleteList,
  closeDialog,
}) {
  const { user } = useAuth();
  const [isChatDeleting, setIsChatDeleting] = useState(false);
  const handleChatDelete = async () => {
    setIsChatDeleting(true);
    await Promise.all(
      deleteList.map((chatId) =>
        deleteRequest(`http://localhost:8080/api/chats/${chatId}`).catch(
          (error) => {
            console.log(error);
          },
        ),
      ),
    );
    setDeleteList([]);
    setIsChatDeleteActive(false);
    setIsChatDeleting(false);
    closeDialog();
  };
  return (
    <DialogWindow
      dialogTitle={"Chat löschen"}
      dialogWidth={"50ch"}
      dialogButtons={[
        <button
          key="dialog_button_1"
          type="button"
          className="btn btn-secondary"
          onClick={closeDialog}
        >
          Abbrechen
        </button>,
        <button
          key="dialog_button_2"
          type="button"
          className="btn btn-danger"
          onClick={handleChatDelete}
        >
          Bestätigen
        </button>,
      ]}
    >
      {isChatDeleting && <p>Chats werden gelöscht ...</p>}
    </DialogWindow>
  );
}
