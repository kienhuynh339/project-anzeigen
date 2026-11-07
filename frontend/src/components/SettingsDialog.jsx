import { useState } from "react";
import DialogWindow from "./DialogWindow";
import * as userApi from "../api/userApi";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../utils/inputValidation";

const fields = {
  [FIELD_KEYS.PROFILE_NAME]: {
    label: "Profilname",
    type: "text",
  },
  [FIELD_KEYS.ACCOUNT_NAME]: {
    label: "Benutzername",
    type: "text",
  },
  [FIELD_KEYS.PASSWORD]: {
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
  const [updating, setUpdating] = useState(false);

  const handleCancel = () => {
    closeDialog();
  };

  const handleSave = async () => {
    if (!validateInput(value, fieldKey)) {
      setNotification(INPUT_NOTICES[fieldKey]);
      return;
    }

    setUpdating(true);
    try {
      switch (fieldKey) {
        case FIELD_KEYS.PROFILE_NAME:
          await userApi.updateProfileName(value);
          break;
        case FIELD_KEYS.ACCOUNT_NAME:
          await userApi.updateAccountName(value);
          break;
        case FIELD_KEYS.PASSWORD:
          await userApi.updatePassword(value);
          break;
      }
      setNotification("");
      closeDialog();
      setUpdating(false);
    } catch (e) {
      setNotification(e.message);
      setUpdating(false);
    }
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
          disabled={updating}
        >
          Abbrechen
        </button>,
        <button
          key="settings-save-button"
          className="btn btn-primary"
          type="button"
          onClick={handleSave}
          disabled={updating}
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
        disabled={updating}
      ></input>
      {notification && (
        <p className="bg-danger-subtle mt-1 border border-danger-subtle rounded p-1 text-danger">
          {notification}
        </p>
      )}
    </DialogWindow>
  );
}
