/**
 * @author Trung Kien Huynh
 */
import { useAppContext } from "../AppContext";
import MessageInputDialog from "./MessageInputDialog";
export default function MessageButton({ item }) {
  const { openDialog } = useAppContext();
  const handleDialogOpen = (event) => {
    event.preventDefault();
    openDialog(MessageInputDialog, { item });
  };
  return (
    <>
      <button
        type="button"
        className="btn btn-outline-primary"
        onClick={handleDialogOpen}
      >
        <i className="bi bi-chat-text"></i> Nachricht schreiben
      </button>
    </>
  );
}
