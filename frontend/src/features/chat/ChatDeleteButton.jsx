/**
 * @author Trung Kien Huynh
 */
export default function ChatDeleteButton({
  isChatDeleteActive,
  handleChatDeleteDeactivate,
  handleChatDeleteActivate,
  setIsDialogActive,
  deleteListLength,
  chatListLength,
}) {
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
          onClick={() => setIsDialogActive(true)}
          disabled={deleteListLength > 0 ? false : true}
        >
          Chat löschen
        </button>
      )}
    </div>
  );
}
