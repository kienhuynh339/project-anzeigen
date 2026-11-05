import { useChatContext } from "../../contexts/ChatContext";
import { useAuthContext } from "../../contexts/AuthContext";
import ProfileImage from "../../components/ProfileImage";
import { Link } from "react-router";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";

export default function MessageListItem({ message }) {
  const { user } = useAuthContext();
  const { otherUser } = useChatContext();

  const userId = user?.id;
  const senderId = message.senderId;

  return (
    <div
      className={`d-flex align-items-end ${senderId == userId ? "align-self-end" : null}`}
      style={{ maxWidth: "80%" }}
    >
      {senderId != userId ? (
        <Link className="mb-1 me-1 rounded-circle" to={`/user/${senderId}`}>
          <ProfileImage
            firstLetterOfName={otherUser.profileName.charAt(0)}
          ></ProfileImage>
        </Link>
      ) : null}
      <div
        className={`d-flex flex-column ${senderId == userId ? "align-self-end" : null}`}
      >
        <div
          className={`rounded p-2 ${senderId == userId ? "align-self-end bg-primary-subtle" : "align-self-start bg-secondary-subtle"}`}
        >
          {message.content}
        </div>
        <span
          className={`text-secondary small ${senderId == userId ? "align-self-end" : null}`}
        >
          {formatTimestamp(message.createdAt, TimestampType.MESSAGE)}
        </span>
      </div>
    </div>
  );
}
