/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "./DialogWindow";
import { useState } from "react";
import { useChatContext } from "../contexts/ChatContext";
import { deleteChat } from "../api/chatApi";
export default function ChatDeleteDialog({ closeDialog }) {
  const { deleteList, setDeleteList, setIsChatDeleteActive } = useChatContext();
  const [deletingChat, setDeletingChat] = useState(false);
  const handleDeleteChat = async () => {
    setDeletingChat(true);
    await Promise.all(
      deleteList.map((chatId) =>
        deleteChat(chatId).catch((e) => console.error(e)),
      ),
    );
    setDeleteList([]);
    setIsChatDeleteActive(false);
    setDeletingChat(false);
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
          onClick={handleDeleteChat}
        >
          Bestätigen
        </button>,
      ]}
    >
      {deletingChat && <p>Chats werden gelöscht ...</p>}
    </DialogWindow>
  );
}
