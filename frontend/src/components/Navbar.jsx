/**
 * @author Trung Kien Huynh
 */
import HamburgerMenu from "./HamburgerMenu";
import logo from "../assets/logo.svg";
import { Link } from "react-router";
import { useNavigate } from "react-router";
import { useAuthContext } from "../contexts/AuthContext";
import { useSearchContext } from "../contexts/SearchContext";

export default function Navbar() {
  const navigate = useNavigate();

  const { triggerSearch, resetSearchFilters } = useSearchContext();
  const { user, logout, isAuthenticated } = useAuthContext();

  const options = [
    {
      label: "Meine Anzeigen",
      action: () => navigate("/meine-anzeigen"),
      redPoint: false,
    },
    {
      label: "Nachrichten",
      action: () => navigate("/nachrichten"),
      redPoint: true,
    },
    {
      label: "Merkliste",
      action: () => navigate("/meine-merkliste"),
      redPoint: false,
    },
    {
      label: "Einstellungen",
      action: () => navigate("/einstellungen"),
      redPoint: false,
    },
    { label: "Abmelden", action: handleLogout, redPoint: false },
  ];

  async function handleLogout() {
    await logout();
    navigate("/login");
  }

  const handleClickLogo = () => {
    resetSearchFilters();
    // löst eine erneute Suche aus
    triggerSearch();
  };

  return (
    <div className="container-fluid pb-2 bg-dark">
      <div className="row align-items-center justify-content-between gap-3">
        {/* Logo */}
        <Link to="/" className="col-auto ps-1 pe-0" onClick={handleClickLogo}>
          <img src={logo} alt="Logo" width="150" height="60" />
        </Link>
        <div className="col-auto pe-0 ps-0">
          <div className="d-flex flex-nowrap align-items-center gap-2">
            {isAuthenticated ? (
              <>
                {/* Begrüßung */}
                <div className="text-white">Hallo {user.profileName}!</div>
                {/* Anzeige erstellen */}
                <button
                  type="button"
                  className="btn btn-outline-light"
                  onClick={() => navigate("/anzeige-erstellen")}
                >
                  <i className="bi bi-plus-circle"></i> Anzeige erstellen
                </button>

                <div className="ps-0 pe-1">
                  {/* Dropdown-Knopf */}
                  <HamburgerMenu options={options} />
                </div>
              </>
            ) : (
              <>
                {/* Anmelden */}
                <button
                  className="btn btn-outline-light"
                  onClick={() => navigate("/login")}
                >
                  Anmelden
                </button>
                {/* Registrieren */}
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
