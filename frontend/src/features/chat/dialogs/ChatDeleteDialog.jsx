/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../../components/DialogWindow";
import { useState } from "react";
import { deleteChat } from "../../../apis/chatApi";
import ErrorDialog from "../../../components/ErrorDialog";
import { useAppContext } from "../../../contexts/AppContext";

export default function ChatDeleteDialog({
  deleteList,
  setDeleteList,
  setIsChatDeleteActive,
  closeDialog,
}) {
  const { openDialog } = useAppContext();

  const [isDeletingChat, setIsDeletingChat] = useState(false);

  const handleDeleteChat = async () => {
    setIsDeletingChat(true);

    // Fehlermeldung(en)
    let errorMessage = "";

    await Promise.all(
      deleteList.map((chatId) =>
        deleteChat(chatId).catch((e) => {
          errorMessage += `${errorMessage ? "\n" : ""}Chat ${chatId} konnte nicht gelöscht werden. ${e.message}`;
        }),
      ),
    );
    setDeleteList([]);

    setIsChatDeleteActive(false);
    if (errorMessage) {
      // öffnet ErrorDialog-Dialogfenster
      openDialog(ErrorDialog, { errorMessage });
    } else {
      closeDialog();
    }

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
