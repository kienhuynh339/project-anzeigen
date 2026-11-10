/**
 * @author Trung Kien Huynh
 */
import MessageList from "../../features/chat/list/MessageList";
import { useChatContext } from "../../contexts/ChatContext";
import MessageInput from "../../features/chat/MessageInput";
import ChatList from "../../features/chat/list/ChatList";
import ChatHeader from "../../features/chat/ChatHeader";

export default function Chat() {
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
              {currentChat.id && <ChatHeader></ChatHeader>}
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
