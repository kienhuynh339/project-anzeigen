/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../components/DialogWindow";
import { useFetch } from "../../hooks/useFetch";
import MessageList from "./MessageList";
import { truncateText } from "../../utils/truncatetext";
import { Link } from "react-router";
import { useState } from "react";
import { deleteRequest } from "../../utils/deleteRequest";
import ChatListItem from "./ChatListItem";
import ChatDeleteButton from "./ChatDeleteButton";
import { createMessage } from "../../utils/createMessage";

const userId = "1";

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
  // Ids von zu löschenden Chats
  const [deleteList, setDeleteList] = useState([]);
  const [isDialogActive, setIsDialogActive] = useState(false);
  const [chatFilter, setChatFilter] = useState("all");
  const [chatsUpdateTrigger, setChatsUpdateTrigger] = useState(false);
  const [isChatDeleting, setIsChatDeleting] = useState(false);
  const [messageContent, setMessageContent] = useState("");
  const [messagesUpdateTrigger, setMessagesUpdateTrigger] = useState(false);
  const [isMessageInputActive, setIsMessageInputActive] = useState(false);

  const chatPage = useFetch(
    `http://localhost:8080/api/chats?userId=${userId}`,
    [userId, chatsUpdateTrigger],
  );

  const chats = chatPage?.content ?? [];

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
      await fetch(
        `http://localhost:8080/api/messages/chat/${chat.id}/sender/${chat.lastMessage.senderId}`,
        { method: "PUT" },
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

  const handleChatDelete = async () => {
    setIsChatDeleting(true);
    await Promise.all(
      deleteList.map((chatId) =>
        deleteRequest(
          `http://localhost:8080/api/chats/${chatId}/user/${userId}`,
        ).catch((error) => {
          console.log(error);
        }),
      ),
    );

    setChatsUpdateTrigger((prev) => !prev);
    setDeleteList([]);
    setIsChatDeleteActive(false);
    setIsDialogActive(false);
    setIsChatDeleting(false);
  };

  const handleChatToDeleteSelect = (chat) => {
    setDeleteList((prev) => {
      return prev.includes(chat.id)
        ? prev.filter((id) => id !== chat.id)
        : [...prev, chat.id];
    });
  };

  const [isBooked, setIsBooked] = useState(false);
  const handleDeleteButtonClick = (event) => {
    event.stopPropagation();
    event.preventDefault();
    // DELETE-Anfrage an Backend (für Favorite/Item)
  };

  const handleBookButtonClick = (event) => {
    event.stopPropagation();
    event.preventDefault();
    setIsBooked(!isBooked);
    // UPDATE-Anfrage an Backend
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
        style={{ height: "80vh" }}
      >
        {/* Linke Hälfte: Chatliste */}
        <div className="h-100 col-5 p-0 border-end border-dark-subtle">
          <div className="h-100 list-group list-group-flush overflow-auto">
            {/* Aktionleiste */}
            <div
              className="border-bottom border-dark-subtle  p-2 bg-secondary-subtle d-flex gap-2 align-items-center justify-content-between"
              style={{ height: "6ch" }}
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
                setIsDialogActive={setIsDialogActive}
                deleteListLength={deleteList.length}
                chatListLength={chats.length}
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
        <div className="h-100 col-7 p-0 ">
          <div className="h-100 d-flex flex-column">
            {/* Chat-Kopfzeile */}
            <div
              className="p-3 border-bottom border-dark-subtle bg-secondary-subtle"
              style={{ height: currentChat.id == "" ? "6ch" : "auto" }}
            >
              {currentChat.id && (
                <>
                  {/* Anzeigetitel */}
                  <h5 className="text-primary pb-1">
                    <Link
                      className="text-decoration-none"
                      to={`/anzeigen/${currentChat.itemId}`}
                    >
                      {truncateText(currentChat.itemTitle, 40)}
                    </Link>
                  </h5>
                  {/* Löschen / Reservieren */}
                  <div className="d-flex flex-wrap justify-content-between pt-2 m-0 border-top border-dark-subtle">
                    <button
                      type="button"
                      className="btn btn-outline-danger"
                      onClick={handleDeleteButtonClick}
                    >
                      <i className="bi bi-trash"></i> Anzeige löschen
                    </button>
                    <button
                      type="button"
                      className={`btn btn${isBooked ? "-outline-success" : "-outline-primary"}`}
                      onClick={handleBookButtonClick}
                    >
                      {isBooked ? "Aktivieren" : "Reservieren"}
                    </button>
                  </div>
                </>
              )}
            </div>

            {/* Chatverlauf */}
            {currentChat.id ? (
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
            ) : null}
          </div>
        </div>
      </div>
      {/* Dialogfenster */}
      <DialogWindow
        dialogWidth={"50ch"}
        isOpen={isDialogActive}
        dialogTitle={"Chat löschen"}
        dialogContent={
          <>{isChatDeleting && <p>Chats werden gelöscht ...</p>}</>
        }
        dialogButtons={[
          <button
            key="dialog_button_1"
            type="button"
            className="btn btn-secondary"
            onClick={() => setIsDialogActive(false)}
          >
            Abbrechen
          </button>,
          <button
            key="dialog_button_2"
            type="button"
            className="btn btn-danger"
            onClick={handleChatDelete}
          >
            Bestätigen
          </button>,
        ]}
      />
    </div>
  );
}
