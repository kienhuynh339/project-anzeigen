/**
 * @author Trung Kien Huynh
 */
import { sendMessage as apiSendMessage } from "../../api/messageApi";
import { useChatContext } from "../../contexts/ChatContext";
import { useState } from "react";

export default function MessageInput() {
  const [messageContent, setMessageContent] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const { currentChat, otherUser } = useChatContext();

  const handleSubmit = async () => {
    setSubmitting(true);

    await apiSendMessage(
      currentChat.itemId,
      currentChat.interestedUser.id,
      messageContent,
    );
    setMessageContent("");

    setSubmitting(false);
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
            <input
              type="text"
              className="form-control border-dark-subtle"
              placeholder="Nachricht schreiben..."
              maxLength={4000}
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
              disabled={submitting}
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
        </div>
      )}
    </div>
  );
}
