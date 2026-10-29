export function translateItemStatus(status) {
  let translatedStatus = null;
  switch (status) {
    case "ACTIVE":
      translatedStatus = "";
      break;
    case "RESERVED":
      translatedStatus = "Reserviert";
      break;
    case "DELETED":
      translatedStatus = "Gelöscht";
      break;
  }
  return translatedStatus;
}
