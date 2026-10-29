/**
 * @author Trung Kien Huynh
 */
import { useFetch } from "../../hooks/useFetch";
import MessageList from "./MessageList";
import { truncateText } from "../../utils/truncatetext";
import { Link } from "react-router";
import { useState, useEffect } from "react";
import ChatListItem from "./ChatListItem";
import ChatDeleteButton from "./ChatDeleteButton";
import { createMessage } from "../../utils/createMessage";
import ItemDeleteButton from "../../components/ItemDeleteButton";
import ItemActivateButton from "../../components/ItemActivateButton";
import ItemReserveButton from "../../components/ItemReserveButton";

import { userId } from "../../utils/userId";
import ItemTitle from "../../components/ItemTitle";

export default function ChatView() {
  const initialChat = {
    id: "",
    itemId: "",
    itemTitle: "",
    itemStatus: "",
    poster: {
      createdAt: "",
      id: "",
      profileName: "",
    },
    interestedUser: {
      createdAt: "",
      id: "",
      profileName: "",
    },
    lastMessage: {
      content: "",
      createdAt: "",
      id: "",
      read: "",
      senderId: "",
    },
    fistDeletedByUserId: "",
  };
  const chatFilterOptions = [
    { label: "Alle Anzeigen", value: "all" },
    { label: "Deine Anzeigen", value: "yours" },
    { label: "Anzeigen anderer", value: "others" },
  ];
  const [currentChat, setCurrentChat] = useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = useState(false);
  const [deleteList, setDeleteList] = useState([]); // Ids von zu löschenden Chats
  const [chatFilter, setChatFilter] = useState("all");
  const [chatsUpdateTrigger, setChatsUpdateTrigger] = useState(false);
  const [messageContent, setMessageContent] = useState("");
  const [messagesUpdateTrigger, setMessagesUpdateTrigger] = useState(false);
  const [isMessageInputActive, setIsMessageInputActive] = useState(false);

  const chatPage = useFetch(
    `http://localhost:8080/api/chats?userId=${userId}`,
    [userId, chatsUpdateTrigger],
  );

  const chats = chatPage?.content ?? [];

  useEffect(() => {
    if (!currentChat.id) return;

    const updatedChat = chats.find((chat) => chat.id === currentChat.id);

    if (updatedChat) {
      setCurrentChat(updatedChat);
    }
  }, [chats, currentChat.id]);

  const otherUser =
    userId != currentChat.poster.id
      ? currentChat.poster
      : currentChat.interestedUser;

  const filteredChats = chats.filter((chat) => {
    switch (chatFilter) {
      case "all":
        return true;
      case "yours":
        return chat.poster.id == userId;
      case "others":
        return chat.poster.id != userId;
    }
  });

  const handleChatSelect = async (chat) => {
    if (!chat.lastMessage.read) {
      // Nachricht(en) als gelesen markieren
      await fetch(
        `http://localhost:8080/api/messages/chat/${chat.id}/sender/${chat.lastMessage.senderId}`,
        { method: "PATCH" },
      )
        .then((response) => {
          if (!response.ok) {
            throw new Error(
              "Fehler beim Markieren der Nachrichten als gelesen",
            );
          }
          chat.lastMessage.read = true;
        })
        .catch((error) => {
          console.log(error);
        });
    }

    setCurrentChat(chat);
  };

  const handleChatToDeleteSelect = (chat) => {
    setDeleteList((prev) => {
      return prev.includes(chat.id)
        ? prev.filter((id) => id !== chat.id)
        : [...prev, chat.id];
    });
  };

  const handleMessageSend = async (
    itemId,
    interestedUserId,
    senderId,
    content,
  ) => {
    setIsMessageInputActive(true);
    await createMessage(itemId, interestedUserId, senderId, content);
    setMessageContent("");
    setMessagesUpdateTrigger((prev) => !prev);
    setChatsUpdateTrigger((prev) => !prev);
    setIsMessageInputActive(false);
  };

  return (
    <div className="container-fluid">
      <h2 className="py-2 m-0">Meine Nachrichten</h2>
      <div
        className="row border border-dark-subtle shadow-sm"
        style={{ height: "85vh" }}
      >
        {/* Linke Hälfte: Chatliste */}
        <div className="h-100 col-5 p-0 border-end border-dark-subtle">
          <div className="h-100 list-group list-group-flush overflow-auto">
            {/* Aktionleiste */}
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
              <ChatDeleteButton
                isChatDeleteActive={isChatDeleteActive}
                handleChatDeleteDeactivate={() => {
                  setIsChatDeleteActive(false);
                  setDeleteList([]);
                }}
                handleChatDeleteActivate={() => {
                  setIsChatDeleteActive(true);
                  setCurrentChat(initialChat);
                }}
                deleteList={deleteList}
                chatListLength={chats.length}
                setDeleteList={setDeleteList}
                setIsChatDeleteActive={setIsChatDeleteActive}
                userId={userId}
                setChatsUpdateTrigger={setChatsUpdateTrigger}
              ></ChatDeleteButton>
            </div>
            {/* Chatliste */}
            {filteredChats
              .sort(
                (a, b) =>
                  new Date(b.lastMessage.createdAt).getTime() -
                  new Date(a.lastMessage.createdAt).getTime(),
              )
              .map((chat) => (
                <ChatListItem
                  key={chat.id}
                  chat={chat}
                  userId={userId}
                  backgroundStyle={
                    isChatDeleteActive
                      ? deleteList.includes(chat.id)
                        ? "bg-danger-subtle"
                        : null
                      : currentChat.id === chat.id
                        ? "bg-primary-subtle"
                        : null
                  }
                  handleOnClick={() =>
                    isChatDeleteActive
                      ? handleChatToDeleteSelect(chat)
                      : handleChatSelect(chat)
                  }
                />
              ))}
          </div>
        </div>

        {/* Rechte Hälfte: Chatverlauf */}
        <div className="h-100 col-7 p-0">
          <div className="h-100 d-flex flex-column">
            {/* Chat-Kopfzeile */}
            <div
              className="border-bottom border-dark-subtle bg-secondary-subtle"
              style={{ height: currentChat.id ? "auto" : "3rem" }}
            >
              {currentChat.id && (
                <>
                  {/* Anzeigetitel */}
                  <h5
                    className="text-primary m-0 ps-3  d-flex  align-items-center"
                    style={{ height: "2.95rem" }}
                  >
                    <Link
                      className="text-decoration-none"
                      to={`/anzeigen/${currentChat.itemId}`}
                    >
                      <ItemTitle
                        itemStatus={currentChat.itemStatus}
                        itemTitle={currentChat.itemTitle}
                        textLimit={40}
                      ></ItemTitle>
                    </Link>
                  </h5>
                  {/* Löschen / Reservieren */}
                  {userId == currentChat.poster.id &&
                    currentChat.itemStatus != "DELETED" && (
                      <>
                        <div className="border-top border-dark-subtle mx-2"></div>
                        <div className="d-flex flex-wrap justify-content-between p-2">
                          <ItemDeleteButton itemId={currentChat.itemId}>
                            <span className="ps-1">Anzeige löschen</span>
                          </ItemDeleteButton>
                          {currentChat.itemStatus == "RESERVED" && (
                            <ItemActivateButton
                              itemId={currentChat.itemId}
                            ></ItemActivateButton>
                          )}
                          {currentChat.itemStatus == "ACTIVE" && (
                            <ItemReserveButton
                              itemId={currentChat.itemId}
                            ></ItemReserveButton>
                          )}
                        </div>
                      </>
                    )}
                </>
              )}
            </div>

            {/* Chatverlauf */}
            {currentChat.id && (
              <>
                {/* Nachrichten */}
                <MessageList
                  chatId={currentChat.id}
                  userId={userId}
                  otherUser={otherUser}
                  messagesUpdateTrigger={messagesUpdateTrigger}
                />
                {/* Eingabebereich */}
                <div className="border-top p-3 border-dark-subtle">
                  {currentChat.fistDeletedByUserId ? (
                    <p className="m-0 text-center">
                      {otherUser.profileName} hat den Chat verlassen.
                    </p>
                  ) : (
                    <form
                      className="d-flex gap-2"
                      onSubmit={(e) => {
                        e.preventDefault();
                        handleMessageSend(
                          currentChat.itemId,
                          currentChat.interestedUser.id,
                          userId,
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
                        disabled={isMessageInputActive}
                      />
                      <button
                        type="submit"
                        className="btn btn-primary border-dark-subtle"
                        disabled={isMessageInputActive}
                      >
                        Senden
                      </button>
                    </form>
                  )}
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
