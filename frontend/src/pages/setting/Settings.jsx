/**
 * @author Trung Kien Huynh
 */
import { useAppContext } from "../../contexts/AppContext";
import { useAuthContext } from "../../contexts/AuthContext";
import SettingsDialog from "../../features/setting/SettingsDialog";
import { FIELD_KEYS } from "../../utils/inputValidation";

export default function Settings() {
  const { openDialog } = useAppContext();
  const { user } = useAuthContext();

  const handleDialogOpen = (fieldKey) => {
    const initialValue = user[fieldKey] ?? "";
    openDialog(SettingsDialog, { initialValue, fieldKey });
  };

  return (
    <div className="container-fluid">
      <h2 className="py-2 m-0">Einstellungen</h2>
      <div
        className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm "
        style={{ width: "120ch" }}
      >
        {/* Profilname */}
        <div className="border-dark-subtle row mx-1 py-4 align-items-center border-bottom">
          <span className="fw-bold col-2">Profilname</span>
          <span className="text-secondary ps-5 col">{user.profileName}</span>
          <div className="col text-end">
            <button
              className="btn btn-outline-primary border-0"
              type="button"
              onClick={() => handleDialogOpen(FIELD_KEYS.PROFILE_NAME)}
            >
              Ändern
            </button>
          </div>
        </div>
        {/* Benutzername */}
        <div className="border-dark-subtle row mx-1 py-4 align-items-center border-bottom">
          <span className="fw-bold col-2">Benutzername</span>
          <span className="text-secondary ps-5 col">{user.accountName}</span>
          <div className="col text-end">
            <button
              className="btn btn-outline-primary border-0"
              type="button"
              onClick={() => handleDialogOpen(FIELD_KEYS.ACCOUNT_NAME)}
            >
              Ändern
            </button>
          </div>
        </div>
        {/* Passwort */}
        <div className="border-dark-subtle row mx-1 py-4 align-items-center">
          <span className="fw-bold col-2">Passwort</span>
          <span className="text-secondary ps-5 col">******</span>
          <div className="col text-end">
            <button
              className="btn btn-outline-primary border-0"
              type="button"
              onClick={() => handleDialogOpen(FIELD_KEYS.PASSWORD)}
            >
              Ändern
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
