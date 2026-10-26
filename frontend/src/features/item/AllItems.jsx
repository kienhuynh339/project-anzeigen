/**
 * @author Trung Kien Huynh
 */
import ItemList from "./ItemList";

export default function AllItems({ searchFilters, searchTrigger }) {
  const {
    searchTerm,
    categoryId,
    postcode,
    locationId,
    radiusKm,
    priceMin,
    priceMax,
  } = searchFilters;

  const urlWithoutPaginationAndSorting = `http://localhost:8080/api/items?searchTerm=${searchTerm}&categoryId=${categoryId}&minPrice=${priceMin}&maxPrice=${priceMax}&locationId=${locationId}&radiusKm=${radiusKm}&`;

  return (
    <ItemList
      urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
      additionalDependencies={[searchTrigger]}
      pageSize={10}
      showItemAmount={true}
    ></ItemList>
  );
}
