/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { useAuthContext } from "../../contexts/AuthContext";
import { useNavigate, Navigate } from "react-router";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../../utils/inputValidation";

export default function Register() {
  const navigate = useNavigate();

  const { register } = useAuthContext();
  const { isAuthenticated, isLoadingUser } = useAuthContext();

  const [profileName, setProfileName] = useState("");
  const [accountName, setAccountName] = useState("");
  const [password, setPassword] = useState("");
  const [notification, setNotification] = useState("");

  if (isLoadingUser) {
    return <p>Lade...</p>;
  }

  if (isAuthenticated) {
    return <Navigate to="/" replace></Navigate>;
  }

  async function handleSubmit(e) {
    if (!validateInput(profileName, FIELD_KEYS.PROFILE_NAME)) {
      setNotification(INPUT_NOTICES[FIELD_KEYS.PROFILE_NAME]);
      return;
    }
    if (!validateInput(accountName, FIELD_KEYS.ACCOUNT_NAME)) {
      setNotification(INPUT_NOTICES[FIELD_KEYS.ACCOUNT_NAME]);
      return;
    }
    if (!validateInput(password, FIELD_KEYS.PASSWORD)) {
      setNotification(INPUT_NOTICES[FIELD_KEYS.PASSWORD]);
      return;
    }

    try {
      await register(profileName, accountName, password);
      navigate("/login");
    } catch (error) {
      setNotification(error.message);
    }
  }

  return (
    <div className="d-flex justify-content-center p-5">
      <div className="border border-dark-subtle rounded shadow-sm">
        <div className="m-4">
          <h2 className="text-center mb-3">Registrieren</h2>
          <div className="d-flex flex-column gap-2 align-items-center">
            {/* Profilname */}
            <div className="form-floating">
              <input
                id="profileName"
                type="text"
                className="form-control"
                placeholder="Profilname"
                value={profileName}
                onChange={(e) => {
                  setProfileName(e.target.value);
                }}
                style={{ width: "20rem" }}
              />
              <label htmlFor="profileName">Profilname</label>
            </div>
            {/* Benutzername */}
            <div className="form-floating">
              <input
                id="accountName"
                type="text"
                className="form-control"
                placeholder="Benutzername"
                value={accountName}
                onChange={(e) => {
                  setAccountName(e.target.value);
                }}
                style={{ width: "20rem" }}
              />
              <label htmlFor="accountName">Benutzername</label>
            </div>
            {/* Passwort */}
            <div className="form-floating">
              <input
                id="password"
                type="password"
                className="form-control"
                placeholder="Passwort"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                }}
                style={{ width: "20rem" }}
              />
              <label htmlFor="password">Passwort</label>
            </div>
            {/* Fehlermeldung */}
            {notification && (
              <p
                className="bg-danger-subtle mt-1 border border-danger-subtle rounded p-1 text-danger"
                style={{ width: "20rem" }}
              >
                {notification}
              </p>
            )}

            <button // Konto erstellen
              className="btn btn-primary mt-3"
              type="button"
              style={{ width: "15rem" }}
              onClick={handleSubmit}
            >
              Konto erstellen
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
