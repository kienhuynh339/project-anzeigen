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

export default function MessageInput() {
  const [messageContent, setMessageContent] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const { currentChat, otherUser } = useChatContext();
  const [notification, setNotification] = useState("");

  const handleSubmit = async () => {
    if (!validateInput(messageContent, FIELD_KEYS.MESSAGE)) {
      setNotification(INPUT_NOTICES[FIELD_KEYS.MESSAGE]);
      return;
    }

    setSubmitting(true);

    try {
      await apiSendMessage(
        currentChat.itemId,
        currentChat.interestedUser.id,
        messageContent,
      );
      setMessageContent("");
    } catch (e) {
      setNotification(e.message);
    } finally {
      setSubmitting(false);
    }
  };
  return (
    <div>
      {currentChat.fistDeletedByUserId ? (
        <p className="m-0 text-center">
          {otherUser.profileName} hat den Chat verlassen.
        </p>
      ) : (
        <div className="d-flex flex-column">
          <div className="d-flex gap-2">
            <textarea
              type="text"
              className="form-control border-dark-subtle"
              placeholder="Nachricht schreiben..."
              maxLength={4000}
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
              disabled={submitting}
              rows={1}
            />
            <button
              type="button"
              className="btn btn-primary border-dark-subtle"
              disabled={submitting || !messageContent}
              onClick={handleSubmit}
            >
              Senden
            </button>
          </div>
          {messageContent && (
            <small className="text-muted">
              {messageContent.length}/4000 Zeichen
            </small>
          )}
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
