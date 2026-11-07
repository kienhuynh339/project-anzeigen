import { useState } from "react";
import DialogWindow from "./DialogWindow";
import * as userApi from "../api/userApi";
import {
  USER_DATA_KEYS,
  userDataInputNotice,
  validateUserDataInput,
} from "../utils/inputValidation";

const fields = {
  [USER_DATA_KEYS.PROFILE_NAME]: {
    label: "Profilname",
    type: "text",
  },
  [USER_DATA_KEYS.ACCOUNT_NAME]: {
    label: "Benutzername",
    type: "text",
  },
  [USER_DATA_KEYS.PASSWORD]: {
    label: "Passwort",
    type: "password",
  },
};

export default function SettingsDialog({
  initialValue,
  fieldKey,
  closeDialog,
}) {
  const [value, setValue] = useState(initialValue);
  const [notification, setNotification] = useState("");

  const handleCancel = () => {
    closeDialog();
  };

  const handleSave = async () => {
    if (!validateUserDataInput(value, fieldKey)) {
      setNotification(userDataInputNotice(fieldKey));
      return;
    }
    try {
      switch (fieldKey) {
        case USER_DATA_KEYS.PROFILE_NAME:
          await userApi.updateProfileName(value);
          break;
        case USER_DATA_KEYS.ACCOUNT_NAME:
          await userApi.updateAccountName(value);
          break;
        case USER_DATA_KEYS.PASSWORD:
          await userApi.updatePassword(value);
          break;
      }
    } catch (e) {
      setNotification("oawjdoawjdo");
    }

    setNotification("");
    closeDialog();
  };

  return (
    <DialogWindow
      dialogTitle={`${fields[fieldKey].label} ändern`}
      dialogWidth={"30rem"}
      dialogButtons={[
        <button
          key="settings-cancel-button"
          className="btn btn-secondary"
          type="button"
          onClick={handleCancel}
        >
          Abbrechen
        </button>,
        <button
          key="settings-save-button"
          className="btn btn-primary"
          type="button"
          onClick={handleSave}
        >
          Speichern
        </button>,
      ]}
    >
      <input
        type={fields[fieldKey].type}
        className="form-control"
        value={value}
        onChange={(e) => setValue(e.target.value)}
      ></input>
      {notification && (
        <p className="bg-danger-subtle mt-1 border border-danger-subtle rounded p-1 text-danger">
          {notification}
        </p>
      )}
    </DialogWindow>
  );
}
