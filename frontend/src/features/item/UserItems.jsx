/**
 * @author Trung Kien Huynh
 */
import ItemList from "./ItemList";
import { Link, useNavigate } from "react-router";
import { useParams } from "react-router";
import { useFetch, getPath } from "../../hooks/useFetch";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import { useEffect } from "react";
import { itemCardTypes } from "./itemCardTypes";
import { useAuth } from "../../contexts/AuthContext";
import ProfileImage from "../../components/ProfileImage";

export default function UserItems() {
  const { user } = useAuth();
  const navigate = useNavigate();
  // ItemList-Props
  const { otherUserId } = useParams();
  const urlWithoutPaginationAndSorting = getPath.itemsByUserId(otherUserId);
  const PAGE_SIZE = 9999;
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2">
      <div>Dieser Nutzer hat derzeit keine Anzeigen online.</div>
    </div>
  );
  // Daten des anderen Users
  const { data: otherUser } = useFetch(
    `http://localhost:8080/api/users/${otherUserId}`,
    [otherUserId],
    { profileName: "", createdAt: "" },
  );

  // "/meine-anzeigen" aufrufen statt "user/:userId", wenn otherUser = user
  useEffect(() => {
    if (String(otherUserId) == String(user?.id)) {
      navigate("/meine-anzeigen", { replace: true });
    }
  }, [otherUserId, user]);
  if (String(otherUserId) === String(user?.id)) {
    return null;
  }

  return (
    <>
      <div className="d-flex justify-content-center align-items-center gap-2 border-bottom border-dark-subtle">
        <ProfileImage
          firstLetterOfName={otherUser?.profileName?.charAt(0)}
          size={"50"}
        ></ProfileImage>
        <span className="fw-bold">{otherUser.profileName}</span>
        <span>|</span>
        <span>
          Aktiv seit: {formatTimestamp(otherUser.createdAt, TimestampType.USER)}
        </span>
      </div>
      <ItemList
        urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
        pageSize={PAGE_SIZE}
        showItemAmount={false}
        additionalContentWhenItemAmountIsZero={
          additionalContentWhenItemAmountIsZero
        }
        itemCardType={itemCardTypes.ITEM_WITH_FAVOR_BUTTON}
      ></ItemList>
    </>
  );
}
