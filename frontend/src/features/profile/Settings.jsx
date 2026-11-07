/**
 * @author Trung Kien Huynh
 */
import SettingsDialog from "../../components/SettingsDialog";
import { useAppContext } from "../../contexts/AppContext";
import { useAuthContext } from "../../contexts/AuthContext";
import { USER_DATA_KEYS } from "../../utils/inputValidation";

const fields = [
  {
    key: USER_DATA_KEYS.PROFILE_NAME,
    label: "Profilname",
    type: "text",
  },
  {
    key: USER_DATA_KEYS.ACCOUNT_NAME,
    label: "Benutzername",
    type: "text",
  },
  {
    key: USER_DATA_KEYS.PASSWORD,
    label: "Passwort",
    type: "password",
  },
];

export default function Settings() {
  const { openDialog } = useAppContext();
  const { user } = useAuthContext();

  const handleDialogOpen = (field) => {
    const initialValue = user[field.key] ?? "";
    openDialog(SettingsDialog, { initialValue, field });
  };

  return (
    <div className="container-fluid">
      <h2 className="py-2 m-0">Einstellungen</h2>
      <div
        className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm "
        style={{ width: "120ch" }}
      >
        {fields.map((field, index) => (
          <div
            className={`border-dark-subtle row mx-1 py-4 align-items-center ${index === fields.length - 1 ? "" : "border-bottom"}`}
            key={field.key}
          >
            {/* Beschriftung  */}
            <span className="fw-bold col-2">{field.label}</span>
            {/* Userdaten */}
            <span className="text-secondary ps-5 col">
              {field.key !== "password" ? user[field.key] : "********"}
            </span>
            {/* Ändern */}
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
