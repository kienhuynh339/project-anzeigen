/**
 * @author Trung Kien Huynh
 */
import { getPath, useFetch } from "../../../hooks/useFetch";
import { useRef, useEffect } from "react";
import { useChatContext } from "../../../contexts/ChatContext";
import MessageListItem from "../list-item/MessageListItem";

export default function MessageList() {
  const { currentChat } = useChatContext();

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
      {messages.map((message) => (
        <MessageListItem key={message.id} message={message}></MessageListItem>
      ))}
    </div>
  );
}
