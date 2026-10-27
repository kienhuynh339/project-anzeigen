/**
 * @author Trung Kien Huynh
 */
import { truncateText } from "../../utils/truncatetext";
export default function ChatListItem({
  chat,
  userId,
  backgroundStyle,
  handleOnClick,
}) {
  return (
    <button
      key={chat.id}
      className={`list-group-item list-group-item-action p-0 border-bottom border-dark-subtle position-relative ${backgroundStyle}`}
      // style={{ height: "150px", flexShrink: 0 }}
      type="button"
      onClick={handleOnClick}
    >
      {/* Roter Punkt oben rechts */}
      {chat.lastMessage.senderId != userId && !chat.lastMessage.read ? (
        <span
          className="position-absolute top-0 end-0  rounded-circle bg-danger m-1"
          style={{ width: "10px", height: "10px", zIndex: 1 }}
        />
      ) : null}

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
          <div className="text-dark">{truncateText(chat.itemTitle, 50)}</div>
          <div
            className={
              chat.lastMessage.senderId != userId &&
              chat.lastMessage.read != true
                ? "text-danger"
                : null
            }
          >
            {chat.lastMessage.senderId == userId
              ? `Du: ${truncateText(chat.lastMessage.content, 45)}`
              : truncateText(chat.lastMessage.content, 45)}
          </div>
        </div>
      </div>
    </button>
  );
}
