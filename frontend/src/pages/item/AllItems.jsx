/**
 * @author Trung Kien Huynh
 */
import { useSearchContext } from "../../contexts/SearchContext";
import { ITEM_CARD_TYPES } from "../../features/item/ItemCard";
import ItemList from "../../features/item/ItemList";
import { getPath } from "../../hooks/useFetch";

// Alle (gefilterten) Anzeigen
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
      itemCardType={ITEM_CARD_TYPES.ITEM_WITH_FAVOR_BUTTON}
    ></ItemList>
  );
}
