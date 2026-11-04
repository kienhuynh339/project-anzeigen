/**
 * @author Trung Kien Huynh
 */
import { sendMessage as apiSendMessage } from "../../api/messageApi";
import { useChatContext } from "../../contexts/ChatContext";
import { useState } from "react";

export default function MessageInput() {
  const [messageContent, setMessageContent] = useState("");
  const [messageInputDisabled, setMessageInputDisabled] = useState(false);
  const { currentChat, otherUser } = useChatContext();

  const sendMessage = async (itemId, interestedUserId, content) => {
    setMessageInputDisabled(true);
    await apiSendMessage(itemId, interestedUserId, content);
    setMessageContent("");
    setMessageInputDisabled(false);
  };
  return (
    <div>
      {currentChat.fistDeletedByUserId ? (
        <p className="m-0 text-center">
          {otherUser.profileName} hat den Chat verlassen.
        </p>
      ) : (
        <form
          className="d-flex gap-2"
          onSubmit={(e) => {
            e.preventDefault();
            sendMessage(
              currentChat.itemId,
              currentChat.interestedUser.id,
              messageContent,
            );
          }}
        >
          <input
            type="text"
            className="form-control border-dark-subtle"
            placeholder="Nachricht schreiben..."
            value={messageContent}
            onChange={(e) => setMessageContent(e.target.value)}
            disabled={messageInputDisabled}
          />
          <button
            type="submit"
            className="btn btn-primary border-dark-subtle"
            disabled={messageInputDisabled}
          >
            Senden
          </button>
        </form>
      )}
    </div>
  );
}
