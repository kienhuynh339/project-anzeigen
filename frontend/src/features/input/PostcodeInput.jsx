/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { useFetch, getPath } from "../../hooks/useFetch";

export default function PostcodeInput({
  postcode,
  setPostcode,
  width = "6rem",
  maxHeight = "400px",
  dropListAbove = false,
}) {
  const [showSuggestions, setShowSuggestions] = useState(false);

  // Gefilterte PLZs abrufen
  const { data: postcodes } = useFetch(
    getPath.postcodesByPostcodePrefix(postcode),
    [postcode],
  );

  return (
    <div className="position-relative" style={{ width: width }}>
      <input
        type="text"
        className="form-control white-focus w-100"
        placeholder="PLZ"
        maxLength={5}
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
            bottom: dropListAbove ? "100%" : null,
            top: dropListAbove ? "auto" : null,
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
    </div>
  );
}
