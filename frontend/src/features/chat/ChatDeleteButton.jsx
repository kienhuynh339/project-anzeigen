/**
 * @author Trung Kien Huynh
 */
import ChatDeleteDialog from "../../components/ChatDeleteDialog";
import { useAppContext } from "../../AppContext";
export default function ChatDeleteButton({
  userId,
  isChatDeleteActive,
  handleChatDeleteDeactivate,
  handleChatDeleteActivate,
  deleteList,
  chatListLength,
  setDeleteList,
  setIsChatDeleteActive,
}) {
  const { openDialog } = useAppContext();
  const handleOpenDialog = () => {
    openDialog(ChatDeleteDialog, {
      setIsChatDeleteActive,
      setDeleteList,
      deleteList,
      userId,
    });
  };

  return (
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
          onClick={handleOpenDialog}
          disabled={deleteList.length > 0 ? false : true}
        >
          Chat löschen
        </button>
      )}
    </div>
  );
}
