/**
 * @author Trung Kien Huynh
 */
import { useState, useEffect } from "react";
import { useFetch } from "../../hooks/useFetch";

export default function LocationInput({
  postcode,
  setLocationId,
  setLocationExtraInfo,
  maxHeight = "400px",
  width = "15rem",
  dropListAbove = false,
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
    postcode: location.postcode,
  }));

  // States bei Änderung der PLZ zurücksetzen
  useEffect(() => {
    setInputValue("");
    setLocationId("");
    setLocationExtraInfo?.("");
  }, [postcode]);

  // Ort-Optionen aktualisieren
  const options = locations.filter((location) =>
    location.name.toLowerCase().includes(inputValue.toLowerCase()),
  );

  const getLocationExtraInfo = (locationId) => {
    return locationsData.find((l) => l.id === locationId).extraInfo;
  };

  const handleInputChange = (event) => {
    const value = event.target.value;
    setInputValue(value);

    // locationId und locationExtraInfo bei gültiger Eingabe setzen
    const location = locations.find(
      (location) => location.name.toLowerCase() === value.toLowerCase(),
    );
    if (location) {
      setLocationId(location.id);
      setLocationExtraInfo?.(getLocationExtraInfo(location.id));
    } else {
      setLocationId("");
      setLocationExtraInfo?.("");
    }
  };

  const handleOptionSelect = (location) => {
    setInputValue(location.name);
    setLocationId(location.id);
    setLocationExtraInfo?.(getLocationExtraInfo(location.id));
    setShowSuggestions(false);
  };

  return (
    <div className="position-relative" style={{ width: width }}>
      <input
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
            bottom: dropListAbove ? "100%" : null,
            top: dropListAbove ? "auto" : null,
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
    </div>
  );
}
