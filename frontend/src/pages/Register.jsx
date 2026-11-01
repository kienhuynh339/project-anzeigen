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
    <form onSubmit={handleSubmit}>
      <h2>Registrieren</h2>

      <input
        type="text"
        placeholder="Profilname"
        value={profileName}
        onChange={(e) => setProfileName(e.target.value)}
        required
      />

      <input
        type="text"
        placeholder="Accountname"
        value={accountName}
        onChange={(e) => setAccountName(e.target.value)}
        required
      />

      <input
        type="password"
        placeholder="Passwort"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        minLength={8}
        required
      />

      {error && <p>{error}</p>}

      <button type="submit">Registrieren</button>
    </form>
  );
}
