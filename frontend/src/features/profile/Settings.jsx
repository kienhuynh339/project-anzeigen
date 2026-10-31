/**
 * @author Trung Kien Huynh
 */
import { useState, useEffect } from "react";
import { useFetch } from "../../hooks/useFetch";
import { userId } from "../../utils/userId";
import SettingsDialog from "../../components/SettingsDialog";
import { useAppContext } from "../../AppContext";

export default function Settings() {
  const fields = [
    {
      key: "profileName",
      label: "Profilname",
      type: "text",
    },
    {
      key: "accountName",
      label: "Benutzername",
      type: "text",
    },
    {
      key: "password",
      label: "Passwort",
      type: "password",
    },
  ];

  const { openDialog } = useAppContext();
  const { data: userData } = useFetch(
    `http://localhost:8080/api/users/${userId}/private`,
    [userId],
    { profileName: "", accountName: "" },
  );

  const handleDialogOpen = (field) => {
    const initialValue = userData[field.key];
    openDialog(SettingsDialog, { initialValue, field });
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
            <span className="text-secondary ps-5 col">
              {field.key !== "password" ? userData[field.key] : "********"}
            </span>
            <div className="col text-end">
              <button
                className="btn btn-outline-primary border-0"
                type="button"
                onClick={() => handleDialogOpen(field)}
              >
                Ändern
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
