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
      <span className="text-danger">{errorMessage}</span>
    </DialogWindow>
  );
}
