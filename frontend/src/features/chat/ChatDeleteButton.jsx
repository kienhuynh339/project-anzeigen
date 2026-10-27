/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import DialogWindow from "../../components/DialogWindow";
import { deleteRequest } from "../../utils/deleteRequest";

export default function ChatDeleteButton({
  userId,
  isChatDeleteActive,
  handleChatDeleteDeactivate,
  handleChatDeleteActivate,
  deleteList,
  chatListLength,
  setDeleteList,
  setIsChatDeleteActive,
  setChatsUpdateTrigger,
}) {
  const [isDialogActive, setIsDialogActive] = useState(false);
  const [isChatDeleting, setIsChatDeleting] = useState(false);
  const handleChatDelete = async () => {
    setIsChatDeleting(true);
    await Promise.all(
      deleteList.map((chatId) =>
        deleteRequest(
          `http://localhost:8080/api/chats/${chatId}/user/${userId}`,
        ).catch((error) => {
          console.log(error);
        }),
      ),
    );

    setChatsUpdateTrigger((prev) => !prev);
    setDeleteList([]);
    setIsChatDeleteActive(false);
    setIsDialogActive(false);
    setIsChatDeleting(false);
  };
  return (
    <>
      <div className="d-flex justify-content-end gap-2">
        {/* Chat löschen */}
        {isChatDeleteActive ? (
          <button /* "Chat löschen" abbrechen */
            className="btn btn-outline-danger"
            onClick={handleChatDeleteDeactivate}
          >
            <i className="bi bi-x-lg"></i>
          </button>
        ) : chatListLength > 0 ? (
          <button /* "Chat löschen" aktivieren */
            className="btn btn-outline-danger"
            onClick={handleChatDeleteActivate}
          >
            <i className="bi bi-trash"></i>
          </button>
        ) : null}

        {/* "Chat löschen" bestätigen */}
        {isChatDeleteActive && (
          <button
            className="btn btn-outline-danger"
            onClick={() => setIsDialogActive(true)}
            disabled={deleteList.length > 0 ? false : true}
          >
            Chat löschen
          </button>
        )}
      </div>
      {/* Dialogfenster */}
      <DialogWindow
        dialogWidth={"50ch"}
        isOpen={isDialogActive}
        dialogTitle={"Chat löschen"}
        dialogContent={isChatDeleting && <p>Chats werden gelöscht ...</p>}
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
            onClick={handleChatDelete}
          >
            Bestätigen
          </button>,
        ]}
      ></DialogWindow>
    </>
  );
}
