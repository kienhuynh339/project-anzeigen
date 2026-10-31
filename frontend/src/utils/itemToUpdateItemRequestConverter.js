export default function itemToUpdateItemRequestConverter(item) {
  return {
    title: item.title,
    categoryId: item.categoryId,
    description: item.description,
    locationId: item.location?.id ?? "",
    postcode: item.location?.postcode ?? "",
    locationExtraInfo: item.location?.extraInfo ?? "",
    price: item.price,
    status: item.status,
  };
}
