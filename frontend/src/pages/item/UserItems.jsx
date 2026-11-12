/**
 * @author Trung Kien Huynh
 */
import ItemList from "../../features/item/ItemList";
import { Navigate } from "react-router";
import { useParams } from "react-router";
import { useFetch, getPath } from "../../hooks/useFetch";
import { formatTimestamp, TIMESTAMP_TYPES } from "../../utils/formatTimestamp";
import { ITEM_CARD_TYPES } from "../../features/item/ItemCard";
import { useAuthContext } from "../../contexts/AuthContext";
import ProfileImage from "../../components/ProfileImage";

export default function UserItems() {
  const { otherUserId } = useParams();

  const { user } = useAuthContext();

  let isOtherUserIdValid = /^[1-9]\d*$/.test(otherUserId);

  const PAGE_SIZE = 9999;
  const urlWithoutPaginationAndSorting = getPath.itemsByUserId(otherUserId);
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2">
      <div>Dieser Nutzer hat derzeit keine Anzeigen online.</div>
    </div>
  );

  // Daten des anderen Users
  const {
    data: otherUser,
    loading,
    error: userFetchError,
  } = useFetch(
    isOtherUserIdValid ? getPath.userByUserId(otherUserId) : null,
    [otherUserId],
    { id: "", profileName: "", createdAt: "" },
  );

  if (loading) {
    return <p>Lade...</p>;
  }

  if (!isOtherUserIdValid || userFetchError) {
    return <Navigate to="/404" replace></Navigate>;
  }

  if (String(otherUserId) === String(user?.id)) {
    return <Navigate to="/meine-anzeigen" replace></Navigate>;
  }

  return (
    <>
      <div className="d-flex justify-content-center align-items-center gap-2 border-bottom border-dark-subtle py-2">
        <ProfileImage
          firstLetterOfName={otherUser.profileName.charAt(0)}
          size={"50"}
        ></ProfileImage>
        <span className="fw-bold">{otherUser.profileName}</span>
        <span>|</span>
        <span>
          Aktiv seit:{" "}
          {formatTimestamp(otherUser.createdAt, TIMESTAMP_TYPES.USER)}
        </span>
      </div>
      <ItemList
        pageSize={PAGE_SIZE}
        urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
        showItemAmount={false}
        additionalContentWhenItemAmountIsZero={
          additionalContentWhenItemAmountIsZero
        }
        itemCardType={ITEM_CARD_TYPES.ITEM_WITH_FAVOR_BUTTON}
      ></ItemList>
    </>
  );
}
