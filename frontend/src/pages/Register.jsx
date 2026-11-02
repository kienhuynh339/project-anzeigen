import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { useNavigate } from "react-router";

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [profileName, setProfileName] = useState("");
  const [accountName, setAccountName] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    try {
      await register(profileName, accountName, password);

      navigate("/login");
    } catch (error) {
      setError(error.message);
    }
  }

  return (
    <div className="d-flex justify-content-center p-5">
      <div className="border border-dark-subtle rounded shadow-sm">
        <div className="m-4">
          <h2 className="text-center mb-3">Registrieren</h2>
          <form
            className="d-flex flex-column gap-2 align-items-center"
            onSubmit={handleSubmit}
          >
            <div className="form-floating">
              <input
                id="profileName"
                type="text"
                className="form-control"
                placeholder="Profilname"
                value={profileName}
                onChange={(e) => {
                  setProfileName(e.target.value);
                  e.target.setCustomValidity(
                    e.target.value.trim().length < 1
                      ? "Der Profilname muss mindestens 1 Zeichen enthalten."
                      : "",
                  );
                }}
                minLength={1}
                style={{ width: "20rem" }}
                required
              />
              <label htmlFor="profileName">Profilname</label>
            </div>
            <div className="form-floating">
              <input
                id="accountName"
                type="text"
                className="form-control"
                placeholder="Benutzername"
                value={accountName}
                onChange={(e) => {
                  setAccountName(e.target.value);
                  e.target.setCustomValidity(
                    e.target.value.trim().length < 6
                      ? "Der Benutzername muss mindestens 6 Zeichen enthalten."
                      : "",
                  );
                }}
                minLength={6}
                style={{ width: "20rem" }}
                required
              />
              <label htmlFor="accountName">Benutzername</label>
            </div>
            <div className="form-floating">
              <input
                id="password"
                type="password"
                className="form-control"
                placeholder="Passwort"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  e.target.setCustomValidity(
                    e.target.value.trim().length < 6
                      ? "Das Passwort muss mindestens 6 Zeichen enthalten."
                      : "",
                  );
                }}
                minLength={6}
                style={{ width: "20rem" }}
                required
              />
              <label htmlFor="password">Passwort</label>
            </div>

            {error && (
              <p className="text-danger m-0" style={{ width: "20rem" }}>
                {error}
              </p>
            )}

            <button
              className="btn btn-primary mt-3"
              type="submit"
              style={{ width: "15rem" }}
            >
              Konto erstellen
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
