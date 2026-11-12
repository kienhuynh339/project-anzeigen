/**
 * @author Trung Kien Huynh
 */
import ItemList from "../../features/item/ItemList";
import { useNavigate } from "react-router";
import { ITEM_CARD_TYPES } from "../../features/item/ItemCard";
import { useAuthContext } from "../../contexts/AuthContext";
import { getPath } from "../../hooks/useFetch";

export default function MyItems() {
  const navigate = useNavigate();

  const { user } = useAuthContext();

  const PAGE_SIZE = 9999;
  const urlWithoutPaginationAndSorting = getPath.itemsByUserId(user.id);

  const title = "Meine Anzeigen";
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2">
      <div>Du hast keine Anzeigen.</div>
      <button // Zu "/anzeige-erstellen" navigieren
        className="btn btn-outline-primary"
        onClick={() => navigate("/anzeige-erstellen")}
      >
        <i className="bi bi-plus-circle"></i> Anzeige erstellen
      </button>
    </div>
  );

  return (
    <ItemList
      pageSize={PAGE_SIZE}
      urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
      title={title}
      showItemAmount={false}
      additionalContentWhenItemAmountIsZero={
        additionalContentWhenItemAmountIsZero
      }
      itemCardType={ITEM_CARD_TYPES.MY_ITEM}
    ></ItemList>
  );
}
