/**
 * @author Trung Kien Huynh
 */
import { getPath, useFetch } from "../../hooks/useFetch";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import { Link } from "react-router";
import { useRef, useEffect } from "react";
import { useAuth } from "../../contexts/AuthContext";
import { useChatContext } from "../../contexts/ChatContext";

export default function MessageList() {
  const { user } = useAuth();
  const { otherUser, currentChat } = useChatContext();

  const userId = user?.id;

  const { data: messagePage } = useFetch(
    getPath.messagesByChatId(currentChat.id),
    [currentChat.id],
  );

  const messages = messagePage?.content ?? [];

  const chatContainerRef = useRef(null);

  // Scrolle das Nachrichtenfenster bis zum Ende
  useEffect(() => {
    const container = chatContainerRef.current;
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  }, [messages]);

  return (
    <div
      className="d-flex flex-column flex-grow-1 p-3 overflow-auto"
      ref={chatContainerRef}
    >
      {messages.map((message) => {
        const senderId = message.senderId;
        return (
          <div
            key={message.id}
            className={`d-flex align-items-end ${senderId == userId ? "align-self-end" : null}`}
            style={{ maxWidth: "80%" }}
          >
            {senderId != userId ? (
              <Link className="pb-1 pe-1" to={`/user/${senderId}`}>
                <img
                  src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${otherUser.profileName.charAt(0)}`}
                  className="rounded-circle"
                  width="30"
                  height="30"
                  style={{ objectFit: "cover" }}
                />
              </Link>
            ) : null}
            <div
              className={`d-flex flex-column ${senderId == userId ? "align-self-end" : null}`}
            >
              <div
                className={`rounded p-2 ${senderId == userId ? "align-self-end bg-primary-subtle" : "align-self-start bg-secondary-subtle"}`}
              >
                {message.content}
              </div>
              <span
                className={`text-secondary small ${senderId == userId ? "align-self-end" : null}`}
              >
                {formatTimestamp(message.createdAt, TimestampType.MESSAGE)}
              </span>
            </div>
          </div>
        );
      })}
    </div>
  );
}
