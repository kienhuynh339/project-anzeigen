/**
 * @author Trung Kien Huynh
 */
import DialogWindow from "./DialogWindow";
import { useState } from "react";
export default function MessageInputDialog({ item, closeDialog }) {
  const [message, setMessage] = useState("");
  const handleSend = () => {
    closeDialog();
  };
  return (
    <DialogWindow
      dialogTitle={"Nachricht schreiben"}
      dialogWidth={"70ch"}
      dialogButtons={[
        <button
          key="message-button-abbrechen"
          className="btn btn-secondary"
          onClick={closeDialog}
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
      ]}
    >
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
    </DialogWindow>
  );
}
