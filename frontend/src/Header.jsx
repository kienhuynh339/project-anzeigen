import logo from "./assets/logo.svg";

export default function Header() {
  return (
    <div className="container-fluid pb-2 bg-success-subtle border border-dark-subtle">
      <header className="row align-items-center justify-content-between gap-3 ">
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
            <div className="ps-0 pe-1">
              <HamburgerMenu />
            </div>
          </div>
        </div>
      </header>
    </div>
  );
}

function HamburgerMenu() {
  const options = [
    { option: "Meine Anzeigen", link: "#" },
    { option: "Nachrichten", link: "#" },
    { option: "Merkliste", link: "#" },
    { option: "Einstellungen", link: "#" },
    { option: "Abmelden", link: "#" },
  ];
  return (
    <div className="btn-group">
      <button
        type="button"
        className="btn btn-outline-dark rounded-2"
        data-bs-toggle="dropdown"
      >
        <i className="bi bi-list-ul"></i>
      </button>
      <ul className="dropdown-menu dropdown-menu-end">
        {options.map((option) => (
          <li>
            <a className="dropdown-item" href={option.link}>
              {option.option}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}
