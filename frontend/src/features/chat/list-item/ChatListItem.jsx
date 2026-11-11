/**
 * @author Trung Kien Huynh
 */
import ProfileImage from "../../../components/ProfileImage";
import { useAuthContext } from "../../../contexts/AuthContext";
import { useChatContext } from "../../../contexts/ChatContext";
import { truncateText } from "../../../utils/truncatetext";

export default function ChatListItem({ chat }) {
  const { user } = useAuthContext();
  const {
    deleteList,
    currentChat,
    isChatDeleteActive,
    selectChat,
    selectChatToDelete,
  } = useChatContext();

  return (
    <button
      key={chat.id}
      className={`list-group-item list-group-item-action p-0 border-bottom border-dark-subtle position-relative ${
        isChatDeleteActive
          ? deleteList.includes(chat.id)
            ? "bg-danger-subtle"
            : null
          : currentChat.id === chat.id
            ? "bg-primary-subtle"
            : null
      }`}
      // style={{ height: "110px", flexShrink: 0 }}
      type="button"
      onClick={() =>
        isChatDeleteActive ? selectChatToDelete(chat) : selectChat(chat)
      }
    >
      {/* Roter Punkt oben rechts */}
      {chat.lastMessage.senderId !== user.id && !chat.lastMessage.read ? (
        <span
          className="position-absolute top-0 end-0  rounded-circle bg-danger m-1"
          style={{ width: "10px", height: "10px", zIndex: 1 }}
        />
      ) : null}

      <div className="d-flex ">
        <div className="ps-2 pe-3 pt-2 pb-1 d-flex flex-column justify-content-center align-items-center">
          {/* Profilbild des Chatpartners */}
          <ProfileImage
            firstLetterOfName={
              user.id === chat.poster.id
                ? chat.interestedUser.profileName.charAt(0)
                : chat.poster.profileName.charAt(0)
            }
            size={"40"}
          ></ProfileImage>
          {/* Chatpartnername  */}
          <span style={{ fontSize: "0.7rem" }}>
            {truncateText(
              user.id === chat.poster.id
                ? chat.interestedUser.profileName
                : chat.poster.profileName,
              6,
            )}
          </span>
        </div>
        <div className="d-flex flex-column pt-1">
          {/* Anzeigetitel */}
          <div className="text-dark">{truncateText(chat.itemTitle, 50)}</div>
          {/* Letzte Nachricht */}
          <div
            className={
              chat.lastMessage.senderId !== user.id &&
              chat.lastMessage.read !== true
                ? "text-danger"
                : null
            }
            style={{ overflowWrap: "anywhere" }}
          >
            {chat.lastMessage.senderId === user.id
              ? `Du: ${truncateText(chat.lastMessage.content, 35)}`
              : truncateText(chat.lastMessage.content, 35)}
          </div>
        </div>
      </div>
    </button>
  );
}
