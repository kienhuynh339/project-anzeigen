/**
 * @author Trung Kien Huynh
 */
import "../../styles/inputWhiteFocus.css";
import PostcodeInput from "../item/input/PostcodeInput";
import LocationInput from "../item/input/LocationInput";
import CategorySelect from "../item/input/CategorySelect";
import { useNavigate } from "react-router";
import { useSearchContext } from "../../contexts/SearchContext";

export default function SearchBar({}) {
  const navigate = useNavigate();

  const { searchFilters, setSearchFilters, triggerSearch } = useSearchContext();

  const { searchTerm, categoryId, postcode, radiusKm, priceMin, priceMax } =
    searchFilters;

  const updateFilter = (key, value) => {
    setSearchFilters((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleSearch = (event) => {
    event.preventDefault();
    triggerSearch();
    navigate("/");
  };

  return (
    <div className="container-fluid bg-dark pb-2">
      <div className="d-flex gap-2 flex-wrap align-items-center justify-content-center">
        {/*Suchbegriff*/}
        <div
          className="flex-grow-1"
          style={{ minWidth: "25rem", width: "auto" }}
        >
          <input
            id="searchTerm"
            type="text"
            className="form-control white-focus"
            placeholder="Suchbegriff"
            value={searchTerm}
            onChange={(e) => {
              updateFilter("searchTerm", e.target.value);
            }}
            autoComplete="off"
          />
        </div>
        {/* Kategorie */}
        <div style={{ width: "17rem" }}>
          <CategorySelect
            formFieldId="search-category-select"
            categoryId={categoryId}
            setCategoryId={(value) => {
              updateFilter("categoryId", value);
            }}
          />
        </div>
        {/* PLZ */}
        <PostcodeInput
          postcode={postcode}
          setPostcode={(value) => {
            updateFilter("postcode", value);
          }}
        ></PostcodeInput>
        {/* Ort */}
        <LocationInput
          postcode={postcode}
          setLocationId={(value) => {
            updateFilter("locationId", value);
          }}
        ></LocationInput>

        {/* Umkreis */}
        <div className="">
          <input
            type="number"
            className="form-control white-focus"
            placeholder="Umkreis (km)"
            style={{ width: "8.3rem" }}
            value={radiusKm}
            onChange={(e) => updateFilter("radiusKm", e.target.value)}
            autoComplete="off"
          />
        </div>
        {/* Preis min/max */}
        <div className="col-auto position-relative">
          <button
            className="btn btn-outline-light"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#advancedFilters"
          >
            <i className="bi bi-sliders"></i> Preis
          </button>
          {/* Dropdown-Fenster */}
          <div
            className="collapse mt-2 text-light position-absolute end-0 border border-dark-subtle bg-white rounded shadow"
            id="advancedFilters"
            style={{
              zIndex: 1,
              top: "85%",
            }}
          >
            <div
              className="d-flex flex-wrap gap-2 align-items-center justify-content-center text-dark"
              style={{
                width: "22rem",
                height: "7rem",
              }}
            >
              <span className="">Preis: </span>
              {/* Preis min */}
              <input
                type="text"
                inputMode="numeric"
                className="form-control"
                placeholder="von"
                value={priceMin}
                onChange={(e) => {
                  const value = e.target.value;
                  if (/^(|0|[1-9]\d*)$/.test(value)) {
                    updateFilter("priceMin", value);
                  }
                }}
                autoComplete="off"
                style={{ width: "7rem" }}
              />
              <span className="">-</span>
              {/* Preis max */}
              <input
                type="text"
                inputMode="numeric"
                className="form-control"
                placeholder="bis"
                value={priceMax}
                onChange={(e) => {
                  const value = e.target.value;
                  if (/^(|0|[1-9]\d*)$/.test(value)) {
                    updateFilter("priceMax", value);
                  }
                }}
                autoComplete="off"
                style={{ width: "7rem" }}
              />
            </div>
          </div>
        </div>
        {/* Suche */}
        <button
          type="button"
          className="btn btn-outline-light "
          style={{ width: "7rem" }}
          onClick={handleSearch}
        >
          Finden
        </button>
      </div>
    </div>
  );
}
