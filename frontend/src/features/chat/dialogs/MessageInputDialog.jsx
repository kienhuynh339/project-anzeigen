/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../../components/DialogWindow";
import { useState } from "react";
import { sendMessage } from "../../../apis/messageApi";
import { useAuthContext } from "../../../contexts/AuthContext";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../../../utils/inputValidation";

export default function MessageInputDialog({ item, closeDialog }) {
  const { user } = useAuthContext();

  const [message, setMessage] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [notification, setNotification] = useState("");

  const handleSend = async (itemId, interestedUserId, content) => {
    if (!validateInput(content, FIELD_KEYS.MESSAGE)) {
      setNotification(INPUT_NOTICES[FIELD_KEYS.MESSAGE]);
      return;
    }

    setSubmitting(true);

    try {
      await sendMessage(itemId, interestedUserId, content);
      setMessage("");
      closeDialog();
    } catch (e) {
      setNotification(e.message);
    } finally {
      setSubmitting(false);
    }
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
          disabled={submitting}
        >
          Abbrechen
        </button>,
        <button
          key="message-button-senden"
          className="btn btn-primary"
          disabled={submitting || !message}
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
          disabled={submitting}
          onChange={(e) => setMessage(e.target.value)}
          rows={4}
          maxLength={4000}
        />
        <p className="text-end mb-0">
          <small className="text-muted">{message.length}/4000 Zeichen</small>
        </p>
        {notification && (
          <p className="bg-danger-subtle my-0 border border-danger-subtle rounded p-1 text-danger">
            {notification}
          </p>
        )}
      </div>
    </DialogWindow>
  );
}
