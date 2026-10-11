import { conversations } from "./test_data/conversations";
import React from "react";

const user_id = "002";

export default function Message() {
  const [currentConversation, setCurrentConversation] = React.useState({
    id: null,
    item: { name: "", link: "#", poster_id: "" },
    messages: [],
  });

  const handleClickConversation = (conversation) => {
    setCurrentConversation(conversation);
  };
  return (
    <div className="container-fluid">
      <h2 className="py-2 m-0">Meine Nachrichten</h2>
      <div
        className="row border border-dark-subtle shadow-sm"
        style={{ height: "80vh" }}
      >
        {/* Linke Hälfte: Konversationsliste */}
        <div className="h-100 col-5 p-0 border-end border-dark-subtle">
          <div className="h-100 list-group list-group-flush overflow-auto">
            {conversations.map((conversation) => (
              <button
                className="list-group-item list-group-item-action border-bottom border-dark-subtle"
                // style={{ height: "150px", flexShrink: 0 }}
                type="button"
                onClick={() => handleClickConversation(conversation)}
              >
                <div className="text-dark">
                  {conversation.item.poster_id === user_id
                    ? `${conversation.item.name}`
                    : `${conversation.item.poster_id} - ${conversation.item.name}`}
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Rechte Hälfte: Chatverlauf */}
        <div className="h-100 col-7 p-0 ">
          <div className="h-100 d-flex flex-column">
            {/* Chat-Kopfzeile */}
            <div className="p-3 border-bottom border-dark-subtle">
              <h5 className="mb-0 text-success-emphasis">
                {currentConversation.id === null
                  ? ""
                  : `${currentConversation.item.poster_id} - ${currentConversation.item.name}`}
              </h5>
            </div>

            {/* Nachrichten */}
            <div className="d-flex flex-column flex-grow-1 p-3 overflow-auto">
              {currentConversation.messages.map((message) => (
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
            {currentConversation.id === null ? (
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
