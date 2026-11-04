/**
 * @author Trung Kien Huynh
 */
import ItemList from "./ItemList";
import { useNavigate } from "react-router";
import { itemCardTypes } from "./itemCardTypes";
import { getPath } from "../../hooks/useFetch";
import { useSearchContext } from "../../contexts/SearchContext";

export default function MyFavoriteItems() {
  const navigate = useNavigate();
  const { resetSearchFilters } = useSearchContext();
  const handleSearchFiltersReset = () => {
    resetSearchFilters();
  };

  const urlWithoutPaginationAndSorting = getPath.itemsFavorites();
  const PAGE_SIZE = 9999;
  const title = "Meine Merkliste";
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2 align-items-center">
      <div>Deine Merkliste ist leer.</div>
      <button
        className="btn btn-outline-primary"
        onClick={() => {
          handleSearchFiltersReset();
          navigate("/");
        }}
      >
        Anzeigen durchsuchen
      </button>
    </div>
  );

  return (
    <ItemList
      urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
      pageSize={PAGE_SIZE}
      title={title}
      showItemAmount={false}
      additionalContentWhenItemAmountIsZero={
        additionalContentWhenItemAmountIsZero
      }
      itemCardType={itemCardTypes.MY_FAVORITE_ITEM}
    ></ItemList>
  );
}
