/**
 * @author Trung Kien Huynh
 */
import ItemList from "../../features/item/ItemList";
import { useNavigate } from "react-router";
import { ITEM_CARD_TYPES } from "../../features/item/ItemCard";
import { getPath } from "../../hooks/useFetch";
import { useSearchContext } from "../../contexts/SearchContext";

export default function MyFavoriteItems() {
  const navigate = useNavigate();

  const { resetSearchFilters } = useSearchContext();

  const PAGE_SIZE = 9999;
  const urlWithoutPaginationAndSorting = getPath.itemsFavorites();
  const title = "Meine Merkliste";
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2 align-items-center">
      <div>Deine Merkliste ist leer.</div>
      <button // Zu "/" navigieren
        className="btn btn-outline-primary"
        onClick={() => {
          resetSearchFilters();
          navigate("/");
        }}
      >
        Anzeigen durchsuchen
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
      itemCardType={ITEM_CARD_TYPES.MY_FAVORITE_ITEM}
    ></ItemList>
  );
}
