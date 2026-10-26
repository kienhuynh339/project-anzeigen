/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "../../components/DialogWindow";
import { useFetch } from "../../hooks/useFetch";
import Messages from "./Messages";
import { truncateText } from "../../utils/truncatetext";
import { Link } from "react-router";
import { useState } from "react";
import { deleteRequest } from "../../utils/deleteRequest";

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

  const handleChatDeleteActivate = () => {
    setIsChatDeleteActive(true);
    setCurrentChat(initialChat);
  };

  const handleChatDeleteDeactivate = () => {
    setIsChatDeleteActive(false);
    setDeleteList([]);
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
              <div className="d-flex justify-content-end gap-2">
                {/* Chat löschen */}
                {isChatDeleteActive ? (
                  <>
                    {/* "Chat löschen" abbrechen */}
                    <button
                      className="btn btn-outline-danger"
                      onClick={handleChatDeleteDeactivate}
                    >
                      <i className="bi bi-x-lg"></i>
                    </button>
                  </>
                ) : (
                  <>
                    {chats.length > 0 ? (
                      <>
                        {/* "Chat löschen" aktivieren */}
                        <button
                          className="btn btn-outline-danger"
                          onClick={handleChatDeleteActivate}
                        >
                          <i className="bi bi-trash"></i>
                        </button>
                      </>
                    ) : (
                      <></>
                    )}
                  </>
                )}

                {/* "Chat löschen" bestätigen */}
                {isChatDeleteActive && (
                  <button
                    className="btn btn-outline-danger"
                    onClick={() => setIsDialogActive(true)}
                    disabled={deleteList.length > 0 ? false : true}
                  >
                    Chat löschen
                  </button>
                )}
              </div>
            </div>
            {/* Chatliste */}
            {filteredChats.map((chat) => (
              <button
                key={chat.id}
                className={`list-group-item list-group-item-action p-0 border-bottom border-dark-subtle position-relative ${
                  isChatDeleteActive
                    ? deleteList.includes(chat.id)
                      ? "bg-danger-subtle"
                      : ""
                    : currentChat.id === chat.id
                      ? "bg-primary-subtle"
                      : ""
                }`}
                // style={{ height: "150px", flexShrink: 0 }}
                type="button"
                onClick={() =>
                  isChatDeleteActive
                    ? handleChatToDeleteSelect(chat)
                    : handleChatSelect(chat)
                }
              >
                {/* Roter Punkt oben rechts */}
                {chat.lastMessage.senderId != userId &&
                !chat.lastMessage.read ? (
                  <span
                    className="position-absolute top-0 end-0  rounded-circle bg-danger m-1"
                    style={{ width: "10px", height: "10px", zIndex: 1 }}
                  />
                ) : (
                  ""
                )}

                <div className="d-flex ">
                  <div className="ps-2 pe-3 pt-2 pb-1 d-flex flex-column justify-content-center align-items-center">
                    <img
                      src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${userId == chat.poster.id ? chat.interestedUser.profileName.charAt(0) : chat.poster.profileName.charAt(0)}`}
                      className="rounded-circle"
                      width="40"
                      height="40"
                      style={{ objectFit: "cover" }}
                    />
                    <span style={{ fontSize: "0.7rem" }}>
                      {truncateText(
                        userId == chat.poster.id
                          ? chat.interestedUser.profileName
                          : chat.poster.profileName,
                        6,
                      )}
                    </span>
                  </div>
                  <div className="d-flex flex-column pt-1">
                    <div className="text-dark">
                      {truncateText(chat.itemTitle, 50)}
                    </div>
                    <div
                      className={
                        chat.lastMessage.senderId != userId &&
                        chat.lastMessage.read != true
                          ? "text-danger"
                          : ""
                      }
                    >
                      {chat.lastMessage.senderId == userId
                        ? `Du: ${truncateText(chat.lastMessage.content, 45)}`
                        : truncateText(chat.lastMessage.content, 45)}
                    </div>
                  </div>
                </div>
              </button>
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
              {currentChat.id != "" && (
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
                <Messages
                  chatId={currentChat.id}
                  userId={userId}
                  otherUser={otherUser}
                />
                {/* Eingabebereich */}
                <div className="border-top p-3 border-dark-subtle">
                  {currentChat.fistDeletedByUserId ? (
                    <p className="m-0 text-center">
                      {otherUser.profileName} hat den Chat verlassen.
                    </p>
                  ) : (
                    <div className=" d-flex gap-2">
                      <input
                        type="text"
                        className="form-control border-dark-subtle"
                        placeholder="Nachricht schreiben..."
                        aria-label="Nachricht schreiben"
                      />

                      <button
                        type="button"
                        className="btn btn-primary border-dark-subtle"
                      >
                        Senden
                      </button>
                    </div>
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
