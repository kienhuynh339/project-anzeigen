import ItemList from "./ItemList";

export default function AllItems({ searchFilters, submitState }) {
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
      additionalDependencies={[submitState]}
      pageSize={10}
      showItemAmount={true}
      activateAdditionalContentWhenItemAmountIsZero={false}
    ></ItemList>
  );
}
