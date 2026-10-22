import { useState, useEffect } from "react";
import { useFetch } from "../../hooks/useFetch";

export default function LocationInput({
  postcode,
  setLocationId,
  maxHeight = "400px",
}) {
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [inputValue, setInputValue] = useState("");
  const [options, setOptions] = useState([]);

  // Orte abrufen
  const locationsData =
    useFetch(`http://localhost:8080/api/locations?postcode=${postcode}`, [
      postcode,
    ]) ?? [];

  // location.extraInfo an location.name anhängen
  const locations = locationsData.map((location) => ({
    id: location.id,
    name: `${location.name} ${location.extraInfo ?? ""}`.trim(),
    plz: location.plz,
  }));

  // States bei Änderung der PLZ zurücksetzen
  useEffect(() => {
    setInputValue("");
    setOptions([]);
    setLocationId(null);
  }, [postcode]);

  // Optionen aktualisieren
  useEffect(() => {
    setOptions(locations);
  }, [locationsData]);

  const handleInputChange = (event) => {
    setInputValue(event.target.value);
    // Optionen bei Benutzereingabe filtern
    setOptions(
      locations.filter((location) =>
        location.name.toLowerCase().includes(event.target.value.toLowerCase()),
      ),
    );
    // locationId bei gültiger Eingabe setzen
    const location = locations.find(
      (location) =>
        location.name.toLowerCase() === event.target.value.toLowerCase(),
    );
    setLocationId(location?.id ?? null);
  };

  const handleOptionSelect = (location) => {
    setInputValue(location.name);
    setLocationId(location.id);
    setShowSuggestions(false);
  };

  return (
    <>
      <input
        id="locationInput"
        type="text"
        className="form-control white-focus w-100"
        placeholder="Ort"
        value={inputValue}
        onChange={handleInputChange}
        onFocus={() => setShowSuggestions(true)}
        onBlur={() => setShowSuggestions(false)}
      />

      {showSuggestions && locations.length > 0 && (
        <ul
          className="list-group position-absolute w-100"
          style={{
            zIndex: 1000,
            maxHeight,
            overflowY: "auto",
            backgroundColor: "white",
            borderRadius: "0",
            border: "1px solid #676767",
          }}
        >
          {options.map((option) => (
            <li
              key={`${option.id}_${option.name}`}
              className="list-group-item list-group-item-action"
              style={{ cursor: "pointer" }}
              onMouseDown={() => {
                handleOptionSelect(option);
              }}
            >
              {option.name}
            </li>
          ))}
        </ul>
      )}
    </>
  );
}
