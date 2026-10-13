import "./inputWhiteFocus.css";
export default function SearchBar() {
  return (
    <div className="container-fluid bg-dark pb-2">
      <form>
        <div className="row g-2 align-items-center justify-content-between">
          <div className="col-4">
            <input
              type="text"
              className="form-control white-focus"
              placeholder="Suchbegriff"
            />
          </div>

          <div className="col-2">
            <select className="form-select white-focus" defaultValue="all">
              <option value="all">Alle Kategorien</option>
              <option value="technik">Technik</option>
              <option value="kleidung">Kleidung</option>
            </select>
          </div>

          {/* Ort */}
          <div className="col-2">
            <input
              type="text"
              className="form-control white-focus"
              placeholder="Ort"
            />
          </div>

          {/* Ort */}
          <div className="col-2">
            <input
              type="text"
              className="form-control white-focus"
              placeholder="Ganzer Ort"
            />
          </div>
          {/* 
            <div
              className="col"
              style={{ flex: "0 0 12.5%", maxWidth: "12.5%" }}
            >
              <select className="form-select" defaultValue="newest">
                <option value="newest">Neueste</option>
                <option value="asc">Aufsteigend</option>
                <option value="desc">Absteigend</option>
              </select>
            </div> */}
          <div className="col-auto">
            <button
              className="btn btn-outline-light"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#advancedFilters"
              aria-expanded="false"
              aria-controls="advancedFilters"
            >
              <i className="bi bi-sliders"></i>
            </button>
          </div>

          <button type="submit" className="col-1 btn btn-outline-light ">
            Finden
          </button>
        </div>
        <div className="collapse mt-2  text-light" id="advancedFilters">
          <div className="row g-2 align-items-center">
            <span className="col-auto">Preis: </span>
            <div className="col-2">
              <input
                type="number"
                className="form-control white-focus"
                placeholder="von"
              />
            </div>
            <span className="col-auto">-</span>
            <div className="col-2">
              <input
                type="number"
                className="form-control white-focus"
                placeholder="bis"
              />
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}
