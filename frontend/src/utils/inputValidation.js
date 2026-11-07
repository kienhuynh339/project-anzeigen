export const USER_DATA_KEYS = Object.freeze({
  PROFILE_NAME: "profileName",
  ACCOUNT_NAME: "accountName",
  PASSWORD: "password",
});

export const userDataInputNotice = (key) => {
  switch (key) {
    case USER_DATA_KEYS.PROFILE_NAME:
      return "Der Profilname muss zwischen 1 und 30 Zeichen lang sein.";
    case USER_DATA_KEYS.ACCOUNT_NAME:
      return "Der Benutzername muss zwischen 5 und 30 Zeichen lang sein.";
    case USER_DATA_KEYS.PASSWORD:
      return "Das Passwort muss zwischen 5 und 30 Zeichen lang sein.";
  }
};

export const validateUserDataInput = (inputValue, key) => {
  const trimmedValue = inputValue.trim();
  return (
    trimmedValue.length >= (key === USER_DATA_KEYS.PROFILE_NAME ? 1 : 5) &&
    inputValue.length <= 30
  );
};
