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
      <ul className="dropdown-menu dropdown-menu-end">
        {options.map((option) => (
          <li key={option.option}>
            <a className="dropdown-item" href={option.link}>
              {option.option}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}
