import { useEffect } from "react";

export default function DialogWindow({ dialogContent, isOpen, dialogWidth }) {
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
          <div
            className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm position-fixed top-50 start-50 translate-middle bg-white"
            style={{ width: `${dialogWidth}`, zIndex: 1050 }}
          >
            {/* Dialogfenster-Inhalt */}
            {dialogContent}
          </div>
        </>
      ) : (
        <></>
      )}
    </>
  );
}
