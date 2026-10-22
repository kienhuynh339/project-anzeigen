import { useState, useEffect } from "react";
import { useFetch } from "../../hooks/useFetch";

export default function PostcodeInput({
  postcode,
  setPostcode,
  maxHeight = "400px",
}) {
  const [showSuggestions, setShowSuggestions] = useState(false);

  // PLZs abrufen
  const postcodes = useFetch(
    `http://localhost:8080/api/postcodes?postcodePrefix=${encodeURIComponent(postcode)}`,
    [postcode],
  );

  return (
    <>
      <input
        type="text"
        className="form-control white-focus w-100"
        placeholder="PLZ"
        value={postcode}
        onChange={(e) => {
          setPostcode(e.target.value);
          setShowSuggestions(true);
        }}
        onFocus={() => setShowSuggestions(true)}
        onBlur={() => setShowSuggestions(false)}
        autoComplete="off"
      />

      {showSuggestions && postcodes.length > 0 && (
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
          {postcodes.map((v) => (
            <li
              key={v}
              className="list-group-item list-group-item-action"
              style={{ cursor: "pointer" }}
              onMouseDown={() => {
                setPostcode(v);
                setShowSuggestions(false);
              }}
            >
              {v}
            </li>
          ))}
        </ul>
      )}
    </>
  );
}
