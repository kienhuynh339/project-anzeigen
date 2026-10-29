/**
 * @author Trung Kien Huynh
 */
import ItemList from "./ItemList";
import { Link, useNavigate } from "react-router";
import { useParams } from "react-router";
import { useFetch } from "../../hooks/useFetch";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import { useEffect } from "react";
import { userId } from "../../utils/userId";
import { itemCardTypes } from "./itemCardTypes";

export default function UserItems() {
  const navigate = useNavigate();
  // ItemList-Props
  const { otherUserId } = useParams();
  const urlWithoutPaginationAndSorting = `http://localhost:8080/api/items/user/${otherUserId}?loggedInUserId=${userId}&`;
  const PAGE_SIZE = 9999;
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2">
      <div>Dieser Nutzer hat derzeit keine Anzeigen online.</div>
    </div>
  );
  // Daten des anderen Users
  const otherUser = useFetch(`http://localhost:8080/api/users/${otherUserId}`, [
    otherUserId,
  ]) ?? { profileName: "", createdAt: "" };

  // "/meine-anzeigen" aufrufen statt "user/:userId", wenn otherUser = user
  useEffect(() => {
    if (String(otherUserId) == String(userId)) {
      navigate("/meine-anzeigen", { replace: true });
    }
  }, [otherUserId, userId]);
  if (String(otherUserId) === String(userId)) {
    return null;
  }

  return (
    <>
      <div className="d-flex justify-content-center align-items-center gap-2 border-bottom border-dark-subtle">
        <img
          src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${otherUser.profileName.charAt(0)}`}
          className="rounded-circle my-2"
          width="50"
          height="50"
          style={{ objectFit: "cover" }}
        />
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
        userId={userId}
        itemCardType={itemCardTypes.ITEM_WITH_FAVOR_BUTTON}
      ></ItemList>
    </>
  );
}
