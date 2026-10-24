import HamburgerMenu from "../../components/HamburgerMenu";
import logo from "../../assets/logo.svg";
import { Link } from "react-router";
import { useNavigate } from "react-router";

export default function Navbar({
  setSearchFilters,
  submitState,
  setSubmitState,
}) {
  const navigate = useNavigate();
  const options = [
    { option: "Meine Anzeigen", link: "/meine-anzeigen" },
    { option: "Nachrichten", link: "/nachrichten" },
    { option: "Merkliste", link: "/" },
    { option: "Einstellungen", link: "/" },
    { option: "Abmelden", link: "#" },
  ];

  const handleLogoClick = () => {
    setSearchFilters({
      searchTerm: "",
      categoryId: 1,
      postcode: "",
      locationId: "",
      radiusKm: "",
      priceMin: "",
      priceMax: "",
    });
    setSubmitState(!submitState);
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
            <div className="text-white">Hallo ...!</div>
            <div>
              <button
                type="button"
                className="btn btn-outline-light"
                onClick={() => navigate("/anzeige-erstellen")}
              >
                <i className="bi bi-plus-circle"></i> Anzeige erstellen
              </button>
            </div>
            <div className="ps-0 pe-1">
              <HamburgerMenu options={options} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
