import { chats } from "./test_data/chats";
import React from "react";
import DialogWindow from "../../components/DialogWindow";

const user_id = "002";

export default function Message() {
  const initialChat = {
    id: null,
    item: { name: "", link: "#", poster_id: "" },
    messages: [],
  };
  const [currentChat, setCurrentChat] = React.useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = React.useState(false);
  // Ids von zu löschenden Chats
  const [deleteList, setDeleteList] = React.useState([]);
  const [isDialogActive, setIsDialogActive] = React.useState(false);

  const handleActivateChatDelete = () => {
    setIsChatDeleteActive(true);
    setCurrentChat(initialChat);
  };

  const handleDeactivateChatDelete = () => {
    setIsChatDeleteActive(false);
    setDeleteList([]);
  };

  const handleDeleteChat = () => {
    setIsChatDeleteActive(false);
    setIsDialogActive(false);
    // Datenbankoperation
    // Update Chatliste
    setDeleteList([]);
  };

  const handleClickChat = (chat) => {
    setCurrentChat(chat);
  };

  const handleClickChatToDelete = (chat) => {
    setDeleteList((prev) => {
      return prev.includes(chat.id)
        ? prev.filter((id) => id !== chat.id)
        : [...prev, chat.id];
    });
  };

  const truncateText = (text, limiter) => {
    return text.length > limiter ? text.slice(0, limiter) + "..." : text;
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
              className="border-bottom border-dark-subtle  p-2 bg-secondary-subtle d-flex gap-2 align-items-center justify-content-end"
              style={{ height: "6ch" }}
            >
              {/* Chat löschen */}
              {isChatDeleteActive ? (
                <>
                  {/* "Chat löschen" abbrechen */}
                  <button
                    className="btn btn-outline-danger"
                    onClick={handleDeactivateChatDelete}
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
                        onClick={handleActivateChatDelete}
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
                >
                  Chat löschen
                </button>
              )}
              <DialogWindow
                dialogWidth={"50ch"}
                isOpen={isDialogActive}
                dialogContent={
                  <div className="mt-2">
                    <h4>Chat löschen</h4>
                    <div className="d-flex justify-content-end align-items-center gap-2 pt-3 pb-2">
                      <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={() => setIsDialogActive(false)}
                      >
                        Abbrechen
                      </button>
                      <button
                        type="button"
                        className="btn btn-danger"
                        onClick={handleDeleteChat}
                      >
                        Bestätigen
                      </button>
                    </div>
                  </div>
                }
              />
            </div>
            {/* Chatliste */}
            {chats.map((chat) => (
              <button
                className={`list-group-item list-group-item-action border-bottom border-dark-subtle ${
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
                    ? handleClickChatToDelete(chat)
                    : handleClickChat(chat)
                }
              >
                <div className="text-dark">
                  {chat.item.poster_id === user_id
                    ? truncateText(chat.item.name, 50)
                    : `${chat.item.poster_id} - ${truncateText(chat.item.name, 50)}`}
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
                {currentChat.id === null
                  ? ""
                  : `${currentChat.item.poster_id} - ${truncateText(currentChat.item.name, 50)}`}
              </h5>
            </div>

            {/* Nachrichten */}
            <div className="d-flex flex-column flex-grow-1 p-3 overflow-auto">
              {currentChat.messages.map((message) => (
                <div
                  className={`d-flex flex-column ${message.sender_id === user_id ? "align-self-end" : ""}`}
                  style={{ maxWidth: "80%" }}
                >
                  <div
                    className={`rounded p-2 ${message.sender_id === user_id ? "align-self-end bg-primary-subtle" : "align-self-start bg-secondary-subtle"}`}
                  >
                    {message.content}
                  </div>
                  <span
                    className={`text-secondary small ${message.sender_id === user_id ? "align-self-end" : ""}`}
                  >
                    {message.created_at}
                  </span>
                </div>
              ))}
            </div>

            {/* Eingabebereich */}
            {currentChat.id === null ? (
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
    </div>
  );
}
