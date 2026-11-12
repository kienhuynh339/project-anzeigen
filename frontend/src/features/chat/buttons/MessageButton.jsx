/**
 * @author Trung Kien Huynh
 */
import { useAppContext } from "../../../contexts/AppContext";
import MessageInputDialog from "../dialogs/MessageInputDialog";

export default function MessageButton({ item }) {
  const { openDialog } = useAppContext();

  const handleOpenDialog = (event) => {
    event.preventDefault();
    openDialog(MessageInputDialog, { item });
  };

  return (
    <button /* Nachricht-Schreiben-Dialogfenster öffnen */
      type="button"
      className="btn btn-outline-primary"
      onClick={handleOpenDialog}
    >
      <i className="bi bi-chat-text"></i> Nachricht schreiben
    </button>
  );
}
