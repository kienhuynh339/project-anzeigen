/**
 * @author Trung Kien Huynh
 */
import { useFetch } from "../../hooks/useFetch";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import { Link } from "react-router";
import { useRef, useEffect } from "react";

export default function MessageList({
  chatId,
  userId,
  otherUser,
  messagesUpdateTrigger,
}) {
  const { data: messagePage } = useFetch(
    `http://localhost:8080/api/messages/chat/${chatId}?page=0&size=200&sort=createdAt,asc`,
    [chatId, messagesUpdateTrigger],
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
    <>
      <div
        className="d-flex flex-column flex-grow-1 p-3 overflow-auto h-100"
        ref={chatContainerRef}
      >
        {messages.map((message) => (
          <div
            key={message.id}
            className={`d-flex align-items-end ${message.senderId == userId ? "align-self-end" : null}`}
            style={{ maxWidth: "80%" }}
          >
            {message.senderId != userId ? (
              <Link className="pb-1 pe-1" to={`/user/${message.senderId}`}>
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
              className={`d-flex flex-column ${message.senderId == userId ? "align-self-end" : null}`}
            >
              <div
                className={`rounded p-2 ${message.senderId == userId ? "align-self-end bg-primary-subtle" : "align-self-start bg-secondary-subtle"}`}
              >
                {message.content}
              </div>
              <span
                className={`text-secondary small ${message.senderId == userId ? "align-self-end" : null}`}
              >
                {formatTimestamp(message.createdAt, TimestampType.MESSAGE)}
              </span>
            </div>
          </div>
        ))}
      </div>
    </>
  );
}
