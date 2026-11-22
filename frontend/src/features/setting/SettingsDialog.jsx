/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import DialogWindow from "../../components/DialogWindow";
import * as userApi from "../../apis/userApi";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../../utils/inputValidation";
import { useAuthContext } from "../../contexts/AuthContext";

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
  const { triggerLoadCurrentUser } = useAuthContext();

  const [inputValue, setInputValue] = useState(initialValue);
  // Für "Passwort wiederholen"
  const [confirmPassword, setConfirmPassword] = useState("");
  // Fehlermeldung
  const [notification, setNotification] = useState("");
  const [isUpdating, setIsUpdating] = useState(false);

  const isPassword = fieldKey === FIELD_KEYS.PASSWORD;
  const passwordsMatch = confirmPassword === inputValue;
  const initialValueMatch = inputValue === initialValue;

  const handleCancel = () => {
    closeDialog();
  };

  const handleSave = async () => {
    if (!validateInput(inputValue, fieldKey)) {
      setNotification(INPUT_NOTICES[fieldKey]);
      return;
    }

    setIsUpdating(true);

    try {
      switch (fieldKey) {
        case FIELD_KEYS.PROFILE_NAME:
          await userApi.updateProfileName(inputValue);
          break;
        case FIELD_KEYS.ACCOUNT_NAME:
          await userApi.updateAccountName(inputValue);
          break;
        case FIELD_KEYS.PASSWORD:
          await userApi.updatePassword(inputValue);
          break;
      }
      triggerLoadCurrentUser();
      setNotification("");
      closeDialog();
    } catch (e) {
      setNotification(e.message);
    }

    setIsUpdating(false);
  };

  return (
    <DialogWindow
      dialogTitle={`${fields[fieldKey].label} ändern`}
      dialogWidth={"30rem"}
      dialogButtons={[
        <button // Abbrechen
          key="settings-cancel-button"
          className="btn btn-secondary"
          type="button"
          onClick={handleCancel}
          disabled={isUpdating}
        >
          Abbrechen
        </button>,
        <button // Speichern
          key="settings-save-button"
          className="btn btn-primary"
          type="button"
          onClick={handleSave}
          disabled={
            isUpdating || initialValueMatch || (isPassword && !passwordsMatch)
          }
        >
          Speichern
        </button>,
      ]}
    >
      <div className="form-floating">
        <input
          id={fieldKey}
          type={fields[fieldKey].type}
          className="form-control"
          placeholder={fields[fieldKey].label}
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          disabled={isUpdating}
        ></input>
        <label htmlFor={fieldKey}>{fields[fieldKey].label}</label>
      </div>
      {fieldKey === FIELD_KEYS.PASSWORD && (
        // "Passwort wiederholen" - Eingabefeld
        <div className="form-floating mt-2">
          <input
            id="confirmPassword"
            type="password"
            className="form-control"
            placeholder="Passwort wiederholen"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            disabled={isUpdating}
          ></input>
          <label htmlFor="confirmPassword">Passwort wiederholen</label>
        </div>
      )}
      {/* Fehlermeldung */}
      {notification && (
        <p className="bg-danger-subtle mt-1 border border-danger-subtle rounded p-1 text-danger">
          {notification}
        </p>
      )}
    </DialogWindow>
  );
}
