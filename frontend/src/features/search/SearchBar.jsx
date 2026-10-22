import "../../styles/inputWhiteFocus.css";
import { useState, useEffect } from "react";
import PostcodeInput from "../input/PostCodeInput";
import LocationSelect from "../input/LocationInput";
import CategorySelect from "../input/CategorySelect";
import { useFetch } from "../../hooks/useFetch";
import { useNavigate } from "react-router";

export default function SearchBar({
  searchFilters,
  setSearchFilters,
  submitState,
  setSubmitState,
}) {
  const navigate = useNavigate();
  const {
    searchTerm,
    categoryId,
    postcode,
    locationId,
    radiusKm,
    priceMin,
    priceMax,
  } = searchFilters;

  const updateFilter = (key, value) => {
    setSearchFilters((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setSubmitState(!submitState);
    navigate("/");
  };

  return (
    <div className="container-fluid bg-dark pb-2">
      <form onSubmit={handleSubmit}>
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
                updateFilter("searchTerm", e.target.value);
              }}
              autoComplete="off"
            />
          </div>
          {/* Kategorie */}
          <div style={{ width: "17rem" }}>
            <CategorySelect
              categoryId={categoryId}
              setCategoryId={(value) => {
                updateFilter("categoryId", value);
              }}
            />
          </div>
          {/* PLZ */}
          <div className="position-relative" style={{ width: "6rem" }}>
            <PostcodeInput
              postcode={postcode}
              setPostcode={(value) => {
                updateFilter("postcode", value);
              }}
            />
          </div>
          {/* Ort */}
          <div className="position-relative" style={{ width: "15rem" }}>
            <LocationSelect
              postcode={postcode}
              setLocationId={(value) => {
                updateFilter("locationId", value);
              }}
            />
          </div>

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

          <button
            type="submit"
            className="btn btn-outline-light "
            style={{ width: "7rem" }}
          >
            Finden
          </button>
        </div>
        <div className="collapse mt-2  text-light" id="advancedFilters">
          <div className="d-flex flex-wrap gap-2 align-items-center justify-content-center">
            {/* Preis min/max */}
            <span className="">Preis: </span>
            <div className="" style={{ width: "7rem" }}>
              <input
                type="number"
                className="form-control white-focus"
                placeholder="von"
                value={priceMin}
                onChange={(e) => updateFilter("priceMin", e.target.value)}
                autoComplete="off"
              />
            </div>
            <span className="">-</span>
            <div className="" style={{ width: "7rem" }}>
              <input
                type="number"
                className="form-control white-focus"
                placeholder="bis"
                value={priceMax}
                onChange={(e) => updateFilter("priceMax", e.target.value)}
                autoComplete="off"
              />
            </div>
          </div>
        </div>
      </form>
    </div>
  );
}
