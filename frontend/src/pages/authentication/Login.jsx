/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { useAuthContext } from "../../contexts/AuthContext";
import { Navigate, useNavigate } from "react-router";

export default function Login() {
  const navigate = useNavigate();

  const { login } = useAuthContext();
  const { isAuthenticated, isLoadingUser } = useAuthContext();

  const [accountName, setAccountName] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  if (isLoadingUser) {
    return <p>Lade...</p>;
  }

  if (isAuthenticated) {
    return <Navigate to="/" replace></Navigate>;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      await login(accountName, password);
      navigate("/");
    } catch (error) {
      setError(error.message);
    }
  }

  return (
    <div className="d-flex justify-content-center p-5">
      <div className="border border-dark-subtle rounded shadow-sm">
        <div className="m-4">
          <h2 className="text-center mb-3">Anmelden</h2>
          <form
            className="d-flex flex-column gap-2 align-items-center"
            onSubmit={handleSubmit}
          >
            <div className="form-floating">
              <input
                id="accountName"
                className="form-control"
                type="text"
                placeholder="Benutzername"
                value={accountName}
                onChange={(e) => setAccountName(e.target.value)}
                style={{ width: "20rem" }}
                required
              />
              <label htmlFor="accountName">Benutzername</label>
            </div>
            <div className="form-floating">
              <input
                id="password"
                className="form-control"
                type="password"
                placeholder="Passwort"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
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
              Anmelden
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
