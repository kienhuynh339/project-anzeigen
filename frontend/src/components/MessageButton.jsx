/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
export default function MessageButton({ item, setDialogWindowProps }) {
  const [message, setMessage] = useState("");

  const closeDialog = () => {
    setDialogWindowProps((prev) => ({ ...prev, isOpen: false }));
  };
  const handleDialogOpen = (event) => {
    event.preventDefault();
    event.stopPropagation();
    setDialogWindowProps(initialDialogWindowProps);
  };
  const handleDialogClose = (event) => {
    event.preventDefault();
    event.stopPropagation();
    closeDialog();
  };
  const handleSend = (event) => {
    event.preventDefault();
    event.stopPropagation();
    closeDialog();
  };

  const initialDialogWindowProps = {
    isOpen: true,
    dialogWidth: "70ch",
    dialogTitle: "Nachricht schreiben",
    dialogContent: (
      <div>
        <span>
          {item.title.length > 50
            ? item.title.slice(0, 50) + "..."
            : item.title}{" "}
          •{" "}
          {item.price.toLocaleString("de-DE", {
            style: "currency",
            currency: "EUR",
          })}
        </span>
        <textarea
          className="form-control mt-3"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          rows={4}
        />
      </div>
    ),
    dialogButtons: [
      <button
        key="message-button-abbrechen"
        className="btn btn-secondary"
        onClick={handleDialogClose}
      >
        Abbrechen
      </button>,
      <button
        key="message-button-senden"
        className="btn btn-primary"
        onClick={handleSend}
      >
        Senden
      </button>,
    ],
  };
  return (
    <>
      <button
        type="button"
        className="btn btn-outline-primary"
        onClick={handleDialogOpen}
      >
        <i className="bi bi-chat-text"></i> Nachricht schreiben
      </button>
    </>
  );
}
