/**
 * @author Trung Kien Huynh
 */
export const FIELD_KEYS = Object.freeze({
  // Userdaten
  PROFILE_NAME: "profileName",
  ACCOUNT_NAME: "accountName",
  PASSWORD: "password",

  // Anzeigedaten
  TITLE: "title",
  DESCRIPTION: "description",
  PRICE: "price",
  POSTCODE: "postcode",
  LOCATION_ID: "locationId",

  // Chat
  MESSAGE: "messageContent",
});

export const INPUT_NOTICES = Object.freeze({
  // Userdaten
  [FIELD_KEYS.PROFILE_NAME]:
    "Der Profilname muss zwischen 1 und 30 Zeichen lang sein.",
  [FIELD_KEYS.ACCOUNT_NAME]:
    "Der Benutzername muss zwischen 5 und 30 Zeichen lang sein.",
  [FIELD_KEYS.PASSWORD]:
    "Das Passwort muss zwischen 5 und 30 Zeichen lang sein.",

  // Anzeigedaten
  [FIELD_KEYS.TITLE]: "Der Titel muss zwischen 5 und 100 Zeichen lang sein.",
  [FIELD_KEYS.DESCRIPTION]:
    "Die Beschreibung darf maximal 4000 Zeichen lang sein.",
  [FIELD_KEYS.PRICE]: "Der Preis muss mindestens 0 betragen.",
  [FIELD_KEYS.POSTCODE]: "Die Postleitzahl muss genau 5 Ziffern enthalten.",
  [FIELD_KEYS.LOCATION_ID]: "Der Standort ist ungültig.",

  // Chat
  [FIELD_KEYS.MESSAGE]:
    "Die Nachricht muss zwischen 1 und 4000 Zeichen lang sein.",
});

export const validateInput = (inputValue, key) => {
  const trimmedValue = String(inputValue ?? "").trim();
  switch (key) {
    // Userdaten
    case FIELD_KEYS.PROFILE_NAME:
      return trimmedValue.length >= 1 && trimmedValue.length <= 30;

    case FIELD_KEYS.ACCOUNT_NAME:
    case FIELD_KEYS.PASSWORD:
      return trimmedValue.length >= 5 && trimmedValue.length <= 30;

    // Anzeigedaten
    case FIELD_KEYS.TITLE:
      return trimmedValue.length >= 5 && trimmedValue.length <= 100;

    case FIELD_KEYS.DESCRIPTION:
      return inputValue.length <= 4000;

    case FIELD_KEYS.PRICE:
      return (
        inputValue !== "" &&
        Number.isFinite(Number(inputValue)) && // gültige, endliche Zahl
        Number(inputValue) >= 0
      );

    case FIELD_KEYS.POSTCODE:
      return /^[0-9]{5}$/.test(inputValue);

    case FIELD_KEYS.LOCATION_ID:
      return Boolean(inputValue);

    // Chat
    case FIELD_KEYS.MESSAGE:
      return inputValue.length >= 1 && inputValue.length <= 4000;

    default:
      return false;
  }
};
