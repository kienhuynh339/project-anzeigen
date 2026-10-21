import HamburgerMenu from "../../components/HamburgerMenu";
import logo from "../../assets/logo.svg";

export default function Navbar() {
  const options = [
    { option: "Meine Anzeigen", link: "#" },
    { option: "Nachrichten", link: "#" },
    { option: "Merkliste", link: "#" },
    { option: "Einstellungen", link: "#" },
    { option: "Abmelden", link: "#" },
  ];
  return (
    <div className="container-fluid pb-2 bg-dark">
      <div className="row align-items-center justify-content-between gap-3">
        <a href="#" className="col-auto ps-1 pe-0">
          <img
            src={logo}
            alt="Logo"
            style={{ maxWidth: "150px", maxHeight: "60px" }}
          />
        </a>
        <div className="col-auto pe-0 ps-0">
          <div className="d-flex flex-nowrap align-items-center gap-2">
            <div className="">Hallo ...!</div>
            <div>
              <button type="button" className="btn btn-outline-light">
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
