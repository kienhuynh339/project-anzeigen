import { useEffect } from "react";

export default function DialogWindow({
  dialogContent,
  dialogTitle,
  dialogButtons,
  isOpen,
  dialogWidth,
}) {
  // Scrollen im Hintergrund deaktivieren beim Öffnen des Dialogfensters und aktivieren beim Schließen
  useEffect(() => {
    // hidden verhindert das Scrollen des <body>
    // "" stellt den ursprünglichen CSS-Zustand wieder her
    document.body.style.overflow = isOpen ? "hidden" : "";

    return () => {
      document.body.style.overflow = "";
    };
  }, [isOpen]);
  return (
    <>
      {isOpen ? (
        <>
          {/* Hintergrund-Overlay */}
          <div
            className="position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50"
            style={{ zIndex: 1040 }}
          ></div>
          {/* Dialogfenster */}
          <div
            className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm position-fixed top-50 start-50 translate-middle bg-white"
            style={{ width: `${dialogWidth}`, zIndex: 1050 }}
          >
            {/* Dialogfenster-Titel */}
            <div className="d-flex justify-content-between my-2">
              <h4>{dialogTitle}</h4>
            </div>
            {/* Dialogfenster-Inhalt */}
            {dialogContent}
            {/* Dialogfenster-Knöpfe */}
            <div className="d-flex justify-content-end align-items-center gap-2 pt-3 pb-2">
              {dialogButtons.map((button) => button)}
            </div>
          </div>
        </>
      ) : (
        <></>
      )}
    </>
  );
}
