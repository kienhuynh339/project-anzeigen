import { useAppContext } from "../contexts/AppContext";

/**
 * @author Trung Kien Huynh
 */
export default function HamburgerMenu({ options }) {
  const { redPointActive } = useAppContext();
  return (
    <div className="btn-group postition-relative">
      {redPointActive ? (
        <span
          className="position-absolute top-0 start-0  rounded-circle bg-danger m-1"
          style={{ width: "10px", height: "10px", zIndex: 1 }}
        />
      ) : null}
      <button
        type="button"
        className="btn btn-outline-light rounded-2"
        data-bs-toggle="dropdown"
      >
        <i className="bi bi-list-ul"></i>
      </button>
      <ul className="dropdown-menu dropdown-menu-end shadow">
        {options.map((option) => (
          <li key={option.label} className="position-relative">
            {option.redPoint && redPointActive ? (
              <span
                className="position-absolute top-0 start-0  rounded-circle bg-danger m-1"
                style={{ width: "10px", height: "10px", zIndex: 1 }}
              />
            ) : null}
            <button className="dropdown-item" onClick={option.action}>
              {option.label}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
