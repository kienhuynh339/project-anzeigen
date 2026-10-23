import React from "react";
import DialogWindow from "../../components/DialogWindow";
import { useFetch } from "../../hooks/useFetch";
import Messages from "./Messages";
import { truncateText } from "../../utils/truncatetext";
import { Link } from "react-router";

const userId = "1";

export default function Chat() {
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
  };

  const chatFilterOptions = [
    { label: "Alle Anzeigen", value: "all" },
    { label: "Deine Anzeigen", value: "yours" },
    { label: "Anzeigen anderer", value: "others" },
  ];

  const [currentChat, setCurrentChat] = React.useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = React.useState(false);
  // Ids von zu löschenden Chats
  const [deleteList, setDeleteList] = React.useState([]);
  const [isDialogActive, setIsDialogActive] = React.useState(false);
  const [chatFilter, setChatFilter] = React.useState("all");

  const chatPage = useFetch(
    `http://localhost:8080/api/chats?userId=${userId}`,
    [userId],
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

  const handleChatDeleteActivate = () => {
    setIsChatDeleteActive(true);
    setCurrentChat(initialChat);
  };

  const handleChatDeleteDeactivate = () => {
    setIsChatDeleteActive(false);
    setDeleteList([]);
  };

  const handleChatDelete = () => {
    setIsChatDeleteActive(false);
    setIsDialogActive(false);
    // Datenbankoperation
    // Update Chatliste
    setDeleteList([]);
  };

  const handleChatSelect = (chat) => {
    // message.read auf true setzen (auch die früheren)
    // Roter Punkt weg
    chat.lastMessage.read = true; // nur die Letzte
    setCurrentChat(chat);
  };

  const handleChatToDeleteSelect = (chat) => {
    setDeleteList((prev) => {
      return prev.includes(chat.id)
        ? prev.filter((id) => id !== chat.id)
        : [...prev, chat.id];
    });
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
                chat.lastMessage.read != true ? (
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
              style={{ height: "6ch" }}
            >
              <h5 className="mb-0 text-primary">
                {currentChat.id === "" ? (
                  ""
                ) : (
                  <Link
                    className="text-decoration-none"
                    to={`/anzeigen/${currentChat.itemId}`}
                  >
                    {truncateText(currentChat.itemTitle, 40)}
                  </Link>
                )}
              </h5>
            </div>

            {/* Nachrichten */}

            {currentChat.id != "" ? (
              <Messages
                chatId={currentChat.id}
                userId={userId}
                otherUser={otherUser}
              />
            ) : (
              ""
            )}

            {/* Eingabebereich */}
            {currentChat.id === "" ? (
              ""
            ) : (
              <div className="border-top p-3 border-dark-subtle">
                <form
                  className="d-flex gap-2"
                  onSubmit={(e) => e.preventDefault()}
                >
                  <input
                    type="text"
                    className="form-control border-dark-subtle"
                    placeholder="Nachricht schreiben..."
                    aria-label="Nachricht schreiben"
                  />

                  <button
                    type="submit"
                    className="btn btn-primary border-dark-subtle"
                  >
                    Senden
                  </button>
                </form>
              </div>
            )}
          </div>
        </div>
      </div>
      {/* Dialogfenster */}
      <DialogWindow
        dialogWidth={"50ch"}
        isOpen={isDialogActive}
        dialogTitle={"Chat löschen"}
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
