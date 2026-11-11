/**
 * @author Trung Kien Huynh
 */
import { sendMessage as apiSendMessage } from "../../apis/messageApi";
import { useChatContext } from "../../contexts/ChatContext";
import { useState } from "react";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../../utils/inputValidation";
import { useAuthContext } from "../../contexts/AuthContext";

export default function MessageInput() {
  const { currentChat, otherUser } = useChatContext();
  const { user } = useAuthContext();

  const [message, setMessage] = useState("");
  const [isSending, setIsSending] = useState(false);
  // Fehlermeldung
  const [notification, setNotification] = useState("");

  const handleSend = async () => {
    if (!validateInput(message, FIELD_KEYS.MESSAGE)) {
      setNotification(INPUT_NOTICES[FIELD_KEYS.MESSAGE]);
      return;
    }

    setIsSending(true);

    try {
      await apiSendMessage(
        currentChat.itemId,
        currentChat.interestedUser.id,
        message,
      );
      setMessage("");
    } catch (e) {
      setNotification(e.message);
    } finally {
      setIsSending(false);
    }
  };
  return (
    <div>
      {currentChat.fistDeletedByUserId &&
      currentChat.fistDeletedByUserId !== user.id ? (
        <p className="m-0 text-center">
          {otherUser.profileName} hat den Chat verlassen.
        </p>
      ) : (
        <div className="d-flex flex-column">
          <div className="d-flex gap-2">
            {/* Nachrichteneingabefeld  */}
            <textarea
              type="text"
              className="form-control border-dark-subtle"
              placeholder="Nachricht schreiben..."
              maxLength={4000}
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              disabled={isSending}
              rows={1}
            />
            <button /* sendet Nachricht */
              type="button"
              className="btn btn-primary border-dark-subtle"
              disabled={isSending || !message}
              onClick={handleSend}
            >
              Senden
            </button>
          </div>
          {/* Zeichenanzahllimit */}
          {message && (
            <small className="text-muted">{message.length}/4000 Zeichen</small>
          )}
          {/* Fehlermeldung */}
          {notification && (
            <p className="bg-danger-subtle mt-1 mb-0 border border-danger-subtle rounded p-1 text-danger">
              {notification}
            </p>
          )}
        </div>
      )}
    </div>
  );
}
