/**
 * @author Trung Kien Huynh
 */
import ItemList from "./ItemList";
import { useNavigate } from "react-router";
import { initialSearchFilters } from "../../utils/initialSearchFilters";
import { itemCardTypes } from "./itemCardTypes";
import { useAuth } from "../../contexts/AuthContext";

export default function MyFavoriteItems({ setSearchFilters }) {
  const { user } = useAuth();
  const navigate = useNavigate();
  const handleSearchFiltersReset = () => {
    setSearchFilters(initialSearchFilters);
  };

  const urlWithoutPaginationAndSorting = `http://localhost:8080/api/items/user/favorites?`;
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
