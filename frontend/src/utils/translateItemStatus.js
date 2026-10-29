export function translateItemStatus(status) {
  let translatedStatus = null;
  switch (status) {
    case "ACTIVE":
      translatedStatus = "";
      break;
    case "RESERVED":
      translatedStatus = "Reserviert";
      break;
    case "DRAFT":
      translatedStatus = "Entwurf";
      break;
    case "DELETED":
      translatedStatus = "Gelöscht";
      break;
  }
  return translatedStatus;
}
