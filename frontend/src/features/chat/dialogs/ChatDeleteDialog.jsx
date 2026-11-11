/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../../components/DialogWindow";
import { useState } from "react";
import { deleteChat } from "../../../apis/chatApi";

export default function ChatDeleteDialog({
  deleteList,
  setDeleteList,
  setIsChatDeleteActive,
  closeDialog,
}) {
  const [isDeletingChat, setIsDeletingChat] = useState(false);

  const handleDeleteChat = async () => {
    setIsDeletingChat(true);

    await Promise.all(
      deleteList.map((chatId) =>
        deleteChat(chatId).catch((e) => console.error(e)),
      ),
    );
    setDeleteList([]);
    setIsChatDeleteActive(false);
    closeDialog();

    // setIsDeletingChat(false);
  };

  return (
    <DialogWindow
      dialogTitle={"Chat löschen"}
      dialogWidth={"25rem"}
      dialogButtons={[
        <button /* schließt Dialogfenster */
          key="dialog_button_1"
          type="button"
          className="btn btn-secondary"
          onClick={closeDialog}
          disabled={isDeletingChat}
        >
          Abbrechen
        </button>,
        <button /* löscht Chat(s) */
          key="dialog_button_2"
          type="button"
          className="btn btn-danger"
          onClick={handleDeleteChat}
          disabled={isDeletingChat}
        >
          Bestätigen
        </button>,
      ]}
    >
      {isDeletingChat && <p>Chats werden gelöscht ...</p>}
    </DialogWindow>
  );
}
