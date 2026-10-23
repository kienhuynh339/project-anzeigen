import { useFetch } from "../../hooks/useFetch";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import { Link } from "react-router";

export default function Messages({ chatId, userId, otherUser }) {
  const messagePage = useFetch(
    `http://localhost:8080/api/chats/${chatId}/messages`,
    [chatId],
  );

  const messages = messagePage?.content ?? [];

  return (
    <>
      <div className="d-flex flex-column flex-grow-1 p-3 overflow-auto">
        {messages.map((message) => (
          <div
            key={message.id}
            className={`d-flex align-items-end ${message.senderId == userId ? "align-self-end" : ""}`}
            style={{ maxWidth: "80%" }}
          >
            {message.senderId != userId ? (
              <Link className="pb-1 pe-1">
                <img
                  src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${otherUser.profileName.charAt(0)}`}
                  className="rounded-circle"
                  width="30"
                  height="30"
                  style={{ objectFit: "cover" }}
                />
              </Link>
            ) : (
              <></>
            )}
            <div
              className={`d-flex flex-column ${message.senderId == userId ? "align-self-end" : ""}`}
            >
              <div
                className={`rounded p-2 ${message.senderId == userId ? "align-self-end bg-primary-subtle" : "align-self-start bg-secondary-subtle"}`}
              >
                {message.content}
              </div>
              <span
                className={`text-secondary small ${message.senderId == userId ? "align-self-end" : ""}`}
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
