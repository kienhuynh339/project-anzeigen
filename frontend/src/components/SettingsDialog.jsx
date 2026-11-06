import { useState } from "react";
import DialogWindow from "./DialogWindow";
export default function SettingsDialog({ initialValue, field, closeDialog }) {
  const [value, setValue] = useState(initialValue);

  const handleCancel = () => {
    closeDialog();
  };

  const handleSave = () => {
    closeDialog();
  };

  return (
    <DialogWindow
      dialogTitle={`${field.label} ändern`}
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
        type={field.type}
        className="form-control"
        value={value}
        onChange={(e) => setValue(e.target.value)}
      ></input>
    </DialogWindow>
  );
}
