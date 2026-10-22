import "../../styles/inputWhiteFocus.css";
import { useState, useEffect } from "react";
import PostcodeInput from "../input/PostCodeInput";
import LocationSelect from "../input/LocationInput";

export default function SearchBar() {
  const [categories, setCategories] = useState([{ id: 1, name: "" }]);
  const [searchTerm, setSearchTerm] = useState("");
  const [categoryId, setCategoryId] = useState(1);
  const [postcode, setPostcode] = useState("");
  const [locationId, setLocationId] = useState(null);

  // Kategorien laden
  useEffect(() => {
    fetch("http://localhost:8080/api/categories")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Fehler beim Laden der Kategorien");
        }
        return response.json();
      })
      .then((data) => {
        setCategories(data);
      });
  }, []);
  return (
    <div className="container-fluid bg-dark pb-2">
      <form>
        <div className="d-flex gap-2 flex-wrap align-items-center justify-content-center">
          {/*Suchbegriff*/}
          <div
            className="flex-grow-1"
            style={{ minWidth: "25rem", width: "auto" }}
          >
            <input
              type="text"
              className="form-control white-focus"
              placeholder="Suchbegriff"
              value={searchTerm}
              onChange={(e) => {
                setSearchTerm(e.target.value);
              }}
            />
          </div>
          {/* Kategorie */}
          <div style={{ width: "17rem" }}>
            <select
              id="category"
              name="category"
              className="form-select white-focus"
              value={categoryId}
              onChange={(e) => {
                setCategoryId(e.target.value);
              }}
            >
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>
          {/* PLZ */}
          <div className="position-relative" style={{ width: "6rem" }}>
            <PostcodeInput postcode={postcode} setPostcode={setPostcode} />
          </div>
          {/* Ort */}
          <div className="position-relative" style={{ width: "15rem" }}>
            <LocationSelect postcode={postcode} setLocationId={setLocationId} />
          </div>

          {/* Umkreis */}
          <div className="">
            <input
              type="text"
              className="form-control white-focus"
              placeholder="Umkreis (km)"
              style={{ width: "7.5rem" }}
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
            >
              <i className="bi bi-sliders"></i>
            </button>
          </div>

          <button type="submit" className="col-1 btn btn-outline-light ">
            Finden
          </button>
        </div>
        <div className="collapse mt-2  text-light" id="advancedFilters">
          <div className="d-flex flex-wrap gap-2 align-items-center justify-content-center">
            <span className="">Preis: </span>
            <div className="" style={{ width: "7rem" }}>
              <input
                type="number"
                className="form-control white-focus"
                placeholder="von"
              />
            </div>
            <span className="">-</span>
            <div className="" style={{ width: "7rem" }}>
              <input
                type="number"
                className="form-control white-focus"
                placeholder="bis"
              />
            </div>
          </div>
        </div>
      </form>
      <p className="text-white">{locationId}</p>
    </div>
  );
}
