/**
 * @author Trung Kien Huynh
 */
import { useSearchContext } from "../../contexts/SearchContext";
import { itemCardTypes } from "./ItemCard";
import ItemList from "./ItemList";
import { getPath } from "../../hooks/useFetch";

export default function AllItems() {
  const { searchFilters } = useSearchContext();

  const { searchTerm, categoryId, locationId, radiusKm, priceMin, priceMax } =
    searchFilters;
  const PAGE_SIZE = 10;
  const urlWithoutPaginationAndSorting = getPath.items(
    searchTerm,
    categoryId,
    priceMin,
    priceMax,
    locationId,
    radiusKm,
  );

  return (
    <ItemList
      pageSize={PAGE_SIZE}
      urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
      showItemAmount={true}
      itemCardType={itemCardTypes.ITEM_WITH_FAVOR_BUTTON}
    ></ItemList>
  );
}
