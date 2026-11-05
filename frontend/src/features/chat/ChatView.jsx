/**
 * @author Trung Kien Huynh
 */
import MessageList from "./MessageList";
import { Link } from "react-router";
import ItemDeleteButton from "../../components/ItemDeleteButton";
import ItemActivateButton from "../../components/ItemActivateButton";
import ItemReserveButton from "../../components/ItemReserveButton";
import ItemTitle from "../../components/ItemTitle";
import { useAuthContext } from "../../contexts/AuthContext";
import { useChatContext } from "../../contexts/ChatContext";
import MessageInput from "./MessageInput";
import ChatList from "./ChatList";

export default function ChatView() {
  const { user } = useAuthContext();
  const { currentChat } = useChatContext();

  return (
    <div className="container-fluid">
      <h2 className="py-2 m-0">Meine Nachrichten</h2>
      <div
        className="row border border-dark-subtle shadow-sm"
        style={{ height: "85vh" }}
      >
        {/* Linke Hälfte: Chatliste */}
        <div className="h-100 col-5 p-0 border-end border-dark-subtle">
          <ChatList></ChatList>
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
                  {user.id == currentChat.poster.id &&
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
                <MessageList></MessageList>
                {/* Eingabebereich */}
                <div className="border-top p-3 border-dark-subtle">
                  <MessageInput></MessageInput>
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
