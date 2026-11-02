/**
 * @author Trung Kien Huynh
 */
import HamburgerMenu from "../../components/HamburgerMenu";
import logo from "../../assets/logo.svg";
import { Link } from "react-router";
import { useNavigate } from "react-router";
import { initialSearchFilters } from "../../utils/initialSearchFilters";
import { useAuth } from "../../contexts/AuthContext";

export default function Navbar({
  setSearchFilters,
  searchTrigger,
  setSearchTrigger,
}) {
  const navigate = useNavigate();
  const { user, logout, isAuthenticated } = useAuth();
  const options = [
    { label: "Meine Anzeigen", action: () => navigate("/meine-anzeigen") },
    { label: "Nachrichten", action: () => navigate("/nachrichten") },
    { label: "Merkliste", action: () => navigate("/meine-merkliste") },
    { label: "Einstellungen", action: () => navigate("/einstellungen") },
    { label: "Abmelden", action: handleLogout },
  ];

  async function handleLogout() {
    await logout();
    navigate("/login");
  }

  const handleLogoClick = () => {
    setSearchFilters(initialSearchFilters);
    setSearchTrigger(!searchTrigger);
  };

  return (
    <div className="container-fluid pb-2 bg-dark">
      <div className="row align-items-center justify-content-between gap-3">
        <Link to="/" className="col-auto ps-1 pe-0" onClick={handleLogoClick}>
          <img
            src={logo}
            alt="Logo"
            style={{ maxWidth: "150px", maxHeight: "60px" }}
          />
        </Link>
        <div className="col-auto pe-0 ps-0">
          <div className="d-flex flex-nowrap align-items-center gap-2">
            {/* Login/Logout/Register */}
            {isAuthenticated ? (
              <>
                <div className="text-white">Hallo {user.profileName}!</div>
                <button
                  type="button"
                  className="btn btn-outline-light"
                  onClick={() => navigate("/anzeige-erstellen")}
                >
                  <i className="bi bi-plus-circle"></i> Anzeige erstellen
                </button>
                <div className="ps-0 pe-1">
                  <HamburgerMenu options={options} />
                </div>
              </>
            ) : (
              <>
                <button
                  className="btn btn-outline-light"
                  onClick={() => navigate("/login")}
                >
                  Anmelden
                </button>
                <button
                  className="btn btn-outline-light"
                  onClick={() => navigate("/register")}
                >
                  Registrieren
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
