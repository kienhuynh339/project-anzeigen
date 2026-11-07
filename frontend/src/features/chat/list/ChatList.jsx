/**
 * @author Trung Kien Huynh
 */
import { useChatContext } from "../../../contexts/ChatContext";
import { useState } from "react";
import ChatListItem from "../list-item/ChatListItem";
import ChatDeleteButton from "../buttons/ChatDeleteButton";
import { useAuthContext } from "../../../contexts/AuthContext";

const chatFilterOptions = [
  { label: "Alle Anzeigen", value: "all" },
  { label: "Deine Anzeigen", value: "yours" },
  { label: "Anzeigen anderer", value: "others" },
];

export default function ChatList() {
  const [chatFilter, setChatFilter] = useState(chatFilterOptions[0].value);
  const { chats } = useChatContext();
  const { user } = useAuthContext();

  const filteredChats = chats.filter((chat) => {
    switch (chatFilter) {
      case "all":
        return true;
      case "yours":
        return chat.poster.id == user.id;
      case "others":
        return chat.poster.id != user.id;
    }
  });

  return (
    <div className="h-100 d-flex flex-column">
      {/* Werkzeugleiste */}
      <div
        className="border-bottom border-dark-subtle  p-2 bg-secondary-subtle d-flex gap-2 align-items-center justify-content-between"
        style={{ height: "3rem" }}
      >
        {/* Chat-Filter */}
        <div>
          <select
            id="chatFilter"
            name="chatFilter"
            className="form-select"
            value={chatFilter}
            onChange={(e) => {
              setChatFilter(e.target.value);
            }}
          >
            {chatFilterOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>
        {/* Chat löschen */}
        <ChatDeleteButton></ChatDeleteButton>
      </div>
      {/* Chatliste */}
      <div className=" list-group list-group-flush flex-grow-1 overflow-auto">
        {filteredChats
          .sort(
            (a, b) =>
              new Date(b.lastMessage.createdAt).getTime() -
              new Date(a.lastMessage.createdAt).getTime(),
          )
          .map((chat) => (
            <ChatListItem key={chat.id} chat={chat} />
          ))}
      </div>
    </div>
  );
}
