/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "./DialogWindow";
import { useState } from "react";
import { sendMessage } from "../api/messageApi";
import { useAuthContext } from "../contexts/AuthContext";
export default function MessageInputDialog({ item, closeDialog }) {
  const { user } = useAuthContext();

  const [message, setMessage] = useState("");
  const [isSending, setIsSending] = useState(false);
  const handleSend = async (itemId, interestedUserId, content) => {
    setIsSending(true);

    await sendMessage(itemId, interestedUserId, content);
    setMessage("");

    closeDialog();
    setIsSending(false);
  };
  return (
    <DialogWindow
      dialogTitle={"Nachricht schreiben"}
      dialogWidth={"70ch"}
      dialogButtons={[
        <button
          key="message-button-abbrechen"
          className="btn btn-secondary"
          onClick={closeDialog}
          disabled={isSending}
        >
          Abbrechen
        </button>,
        <button
          key="message-button-senden"
          className="btn btn-primary"
          disabled={isSending}
          onClick={() => {
            handleSend(item.id, user.id, message);
          }}
        >
          Senden
        </button>,
      ]}
    >
      <div>
        <span>
          {item.title.length > 50
            ? item.title.slice(0, 50) + "..."
            : item.title}{" "}
          •{" "}
          {item.price.toLocaleString("de-DE", {
            style: "currency",
            currency: "EUR",
          })}
        </span>
        <textarea
          className="form-control mt-3"
          value={message}
          disabled={isSending}
          onChange={(e) => setMessage(e.target.value)}
          rows={4}
        />
      </div>
    </DialogWindow>
  );
}
