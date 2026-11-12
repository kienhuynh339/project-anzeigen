import { useAppContext } from "../contexts/AppContext";

/**
 * @author Trung Kien Huynh
 */
export default function HamburgerMenu({ options }) {
  // true -> Roter Punkt anzeigen (signalisiert, dass es neue Nachricht(en) gibt)
  const { isRedPointActive } = useAppContext();
  return (
    <div className="btn-group postition-relative">
      {/* Roter Punkt */}
      {isRedPointActive ? (
        <span
          className="position-absolute top-0 start-0  rounded-circle bg-danger m-1"
          style={{ width: "10px", height: "10px", zIndex: 1 }}
        />
      ) : null}
      {/* Dropdown-Knopf */}
      <button
        type="button"
        className="btn btn-outline-light rounded-2"
        data-bs-toggle="dropdown"
      >
        <i className="bi bi-list-ul"></i>
      </button>
      {/* Dropdown-Menü */}
      <ul className="dropdown-menu dropdown-menu-end shadow">
        {options.map((option) => (
          <li key={option.label} className="position-relative">
            {/* Roter Punkt */}
            {option.redPoint && isRedPointActive ? (
              <span
                className="position-absolute top-0 start-0  rounded-circle bg-danger m-1"
                style={{ width: "10px", height: "10px", zIndex: 1 }}
              />
            ) : null}
            {/* Optionen */}
            <button className="dropdown-item" onClick={option.action}>
              {option.label}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
