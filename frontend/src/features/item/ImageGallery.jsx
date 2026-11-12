/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import noImage from "../../assets/noImage.jpg";
import { translateItemStatus } from "../../utils/translateItemStatus";

export default function ImageGallery({ imageIds, itemStatus }) {
  const [currentIndex, setCurrentIndex] = useState(0);

  const hasImages = imageIds.length > 0;

  const previousImage = () => {
    setCurrentIndex((prev) => (prev - 1 + imageIds.length) % imageIds.length);
  };
  const nextImage = () => {
    setCurrentIndex((currentIndex + 1) % imageIds.length);
  };

  return (
    <div
      className="container-fluid position-relative bg-dark text-center p-0"
      style={{ height: "55ch" }}
    >
      {hasImages ? (
        <>
          <img
            src={`http://localhost:8080/api/images/${imageIds[currentIndex]}`}
            style={{
              width: "100%",
              height: "100%",
              objectFit: "contain",
              filter:
                currentIndex === 0 && itemStatus && itemStatus !== "ACTIVE"
                  ? "brightness(0.20)"
                  : "none",
            }}
          />
          {/* Das erste Bild zeigt den Anzeige-Zustand (Reserviert/Entwurf/Gelöscht) */}
          {currentIndex === 0 && itemStatus && itemStatus !== "ACTIVE" && (
            <span className="text-white position-absolute top-50 start-50 translate-middle">
              {translateItemStatus(itemStatus)}
            </span>
          )}
          {imageIds.length > 1 && (
            <>
              <button /* Vorheriges Bild */
                className="btn btn-dark position-absolute top-50 start-0 translate-middle-y ms-2 rounded-circle"
                type="button"
                onClick={previousImage}
              >
                &#10094;
              </button>
              <button /* Nächstes Bild */
                className="btn btn-dark position-absolute top-50 end-0 translate-middle-y me-2 rounded-circle"
                type="button"
                onClick={nextImage}
              >
                &#10095;
              </button>
              {/* Bildnummer */}
              <span className="badge bg-dark position-absolute bottom-0 end-0 m-2">
                {currentIndex + 1} / {imageIds.length}
              </span>
            </>
          )}
        </>
      ) : (
        // Kein Bild
        <img className="h-100" src={noImage}></img>
      )}
    </div>
  );
}
