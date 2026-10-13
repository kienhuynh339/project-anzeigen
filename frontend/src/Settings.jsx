import { useState, useEffect } from "react";
import DialogWindow from "./DialogWindow";

const userData = {
  profilname: "Max Mustermann",
  benutzername: "max.mustermann",
  passwort: "******",
  adresse: "52062 Aachen",
};

const fields = [
  {
    key: "profilname",
    label: "Profilname",
    type: "text",
  },
  {
    key: "benutzername",
    label: "Benutzername",
    type: "text",
  },
  {
    key: "passwort",
    label: "Passwort",
    type: "password",
  },
  {
    key: "adresse",
    label: "Adresse",
    type: "text",
  },
];

export default function Settings() {
  const [data, setData] = useState(userData);
  const [activeField, setActiveField] = useState(null);
  const [fieldValue, setFieldValue] = useState("");
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const handleDialogOpen = (field) => {
    setActiveField(field);
    setFieldValue(data[field.key]);
    setIsDialogOpen(true);
  };

  const handleChange = (event) => {
    event.preventDefault();

    setData((prev) => ({
      ...prev,
      [activeField.key]: fieldValue,
    }));

    setActiveField(null);
    setIsDialogOpen(false);
  };

  const handleDialogClose = () => {
    setActiveField(null);
    setIsDialogOpen(false);
  };

  return (
    <div className="container-fluid">
      <h2 className="py-2 m-0">Einstellungen</h2>
      {/* Benutzerdaten anzeigen */}
      <div
        className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm "
        style={{ width: "120ch" }}
      >
        {/* Felder */}
        {fields.map((field, index) => (
          <div
            className={`border-dark-subtle row mx-1 py-4 align-items-center ${index === fields.length - 1 ? "" : "border-bottom"}`}
            key={field.key}
          >
            <span className="fw-bold col-2">{field.label}</span>
            <span className="text-secondary ps-5 col">{data[field.key]}</span>
            <div className="col text-end">
              <button
                className="btn btn-outline-success border-0"
                type="button"
                onClick={() => handleDialogOpen(field)}
              >
                Ändern
              </button>
            </div>
          </div>
        ))}
      </div>
      {/* Dialogfenster*/}
      {activeField && (
        <DialogWindow
          isOpen={isDialogOpen}
          dialogWidth={"50ch"}
          dialogContent={
            <form className="d-flex flex-column" onSubmit={handleChange}>
              <span className="fw-bold py-4">{activeField.label} ändern</span>
              <input
                className="form-control border-dark-subtle"
                type={activeField.type}
                value={fieldValue}
                onChange={(e) => setFieldValue(e.target.value)}
              ></input>
              <div className="d-flex justify-content-end gap-3 py-4">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleDialogClose}
                >
                  Abbrechen
                </button>
                <button type="submit" className="btn btn-success">
                  Bestätigen
                </button>
              </div>
            </form>
          }
        />
      )}
    </div>
  );
}

// // Scrollen im Hintergrund deaktivieren beim Öffnen des Dialogfensters und aktivieren beim Schließen
// useEffect(() => {
//   // hidden verhindert das Scrollen des <body>
//   // "" stellt den ursprünglichen CSS-Zustand wieder her
//   document.body.style.overflow = activeField ? "hidden" : "";

//   return () => {
//     document.body.style.overflow = "";
//   };
// }, [activeField]);

// {/* Dialogfenster*/}
//       {activeField && (
//         <>
//           {/* Hintergrund-Overlay */}
//           <div
//             className="position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50"
//             style={{ zIndex: 1040 }}
//             // onClick={closeDialog}
//           ></div>
//           <div
//             className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm position-fixed top-50 start-50 translate-middle bg-white"
//             style={{ width: "50ch", zIndex: 1050 }}
//           >
//             {/* Dialogfenster-Inhalt */}
//             <form className="d-flex flex-column" onSubmit={handleChange}>
//               <span className="fw-bold py-4">{activeField.label} ändern</span>
//               <input
//                 className="form-control border-dark-subtle"
//                 type={activeField.type}
//                 value={fieldValue}
//                 onChange={(e) => setFieldValue(e.target.value)}
//               ></input>
//               <div className="d-flex justify-content-end gap-3 py-4">
//                 <button
//                   type="button"
//                   className="btn btn-secondary"
//                   onClick={handleDialogClose}
//                 >
//                   Abbrechen
//                 </button>
//                 <button type="submit" className="btn btn-success">
//                   Bestätigen
//                 </button>
//               </div>
//             </form>
//           </div>
//         </>
//       )}
