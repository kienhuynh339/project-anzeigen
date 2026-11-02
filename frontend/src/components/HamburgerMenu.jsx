/**
 * @author Trung Kien Huynh
 */
export default function HamburgerMenu({ options }) {
  return (
    <div className="btn-group">
      <button
        type="button"
        className="btn btn-outline-light rounded-2"
        data-bs-toggle="dropdown"
      >
        <i className="bi bi-list-ul"></i>
      </button>
      <ul className="dropdown-menu dropdown-menu-end shadow">
        {options.map((option) => (
          <li key={option.label}>
            <button className="dropdown-item" onClick={option.action}>
              {option.label}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
