export function updateItemFormData(
  title,
  categoryId,
  description,
  locationId,
  postcode,
  locationExtraInfo,
  price,
  images,
  status,
) {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("categoryId", categoryId);
  formData.append("description", description);
  formData.append("locationId", locationId);
  formData.append("postcode", postcode);
  formData.append("locationExtraInfo", locationExtraInfo);
  formData.append("price", price);
  formData.append("status", status);
  images.forEach((image) => {
    formData.append("images", image);
  });
  return formData;
}
