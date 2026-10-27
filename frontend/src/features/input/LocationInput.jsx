/**
 * @author Trung Kien Huynh
 */
import { useState, useEffect } from "react";
import { useFetch } from "../../hooks/useFetch";

export default function LocationInput({
  postcode,
  setLocationId,
  maxHeight = "400px",
}) {
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [inputValue, setInputValue] = useState("");

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
    setLocationId("");
  }, [postcode]);

  // Ort-Optionen aktualisieren
  const options = locations.filter((location) =>
    location.name.toLowerCase().includes(inputValue.toLowerCase()),
  );

  const handleInputChange = (event) => {
    const value = event.target.value;
    setInputValue(value);

    // locationId bei gültiger Eingabe setzen
    const location = locations.find(
      (location) => location.name.toLowerCase() === value.toLowerCase(),
    );
    setLocationId(location?.id ?? "");
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
        autoComplete="off"
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
