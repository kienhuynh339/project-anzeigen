/**
 * @author Trung Kien Huynh
 */
import ChatDeleteDialog from "../dialogs/ChatDeleteDialog";
import { useAppContext } from "../../../contexts/AppContext";
import { useChatContext } from "../../../contexts/ChatContext";
export default function ChatDeleteButton() {
  const { openDialog } = useAppContext();
  const {
    isChatDeleteActive,
    deleteList,
    setIsChatDeleteActive,
    setDeleteList,
    resetCurrentChat,
    chatListLength,
  } = useChatContext();

  const handleOpenDialog = () => {
    openDialog(ChatDeleteDialog, {
      deleteList,
      setDeleteList,
      setIsChatDeleteActive,
    });
  };

  return (
    <div className="d-flex justify-content-end gap-2">
      {isChatDeleteActive ? (
        <button /* deaktiviert Chat-Löschen-Modus */
          className="btn btn-outline-danger"
          onClick={() => {
            setIsChatDeleteActive(false);
            setDeleteList([]);
          }}
        >
          <i className="bi bi-x-lg"></i>
        </button>
      ) : chatListLength > 0 ? (
        <button /* aktiviert Chat-Löschen-Modus */
          className="btn btn-outline-danger"
          onClick={() => {
            setIsChatDeleteActive(true);
            resetCurrentChat();
          }}
        >
          <i className="bi bi-trash"></i>
        </button>
      ) : null}

      {/* öffnet Chat-Löschen-Dialogfenster  */}
      {isChatDeleteActive && (
        <button
          className="btn btn-outline-danger"
          onClick={handleOpenDialog}
          disabled={deleteList.length > 0 ? false : true}
        >
          Chat löschen
        </button>
      )}
    </div>
  );
}
