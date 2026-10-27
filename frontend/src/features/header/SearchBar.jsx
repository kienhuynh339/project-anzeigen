/**
 * @author Trung Kien Huynh
 */
import "../../styles/inputWhiteFocus.css";
import { useState, useEffect } from "react";
import PostcodeInput from "../input/PostCodeInput";
import LocationInput from "../input/LocationInput";
import CategorySelect from "../input/CategorySelect";
import { useNavigate } from "react-router";

export default function SearchBar({
  searchFilters,
  setSearchFilters,
  searchTrigger,
  setSearchTrigger,
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
    setSearchTrigger(!searchTrigger);
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
          <LocationInput
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
              <div className="" style={{ width: "7rem" }}>
                <input
                  type="number"
                  className="form-control"
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
                  className="form-control"
                  placeholder="bis"
                  value={priceMax}
                  onChange={(e) => updateFilter("priceMax", e.target.value)}
                  autoComplete="off"
                />
              </div>
            </div>
          </div>
        </div>
        <button
          type="button"
          className="btn btn-outline-light "
          style={{ width: "7rem" }}
          onClick={handleSubmit}
        >
          Finden
        </button>
      </div>
    </div>
  );
}
