/**
 * @author Trung Kien Huynh
 */

import DialogWindow from "./DialogWindow";

export default function ErrorDialog({ errorMessage, closeDialog }) {
  return (
    <DialogWindow
      dialogTitle={"Fehler"}
      dialogButtons={[
        <button
          key="error-dialog-close"
          className="btn btn-secondary"
          type="button"
          onClick={closeDialog}
        >
          Schließen
        </button>,
      ]}
    >
      {/* Fehler-Nachricht */}
      <span className="text-danger">{errorMessage}</span>
    </DialogWindow>
  );
}
