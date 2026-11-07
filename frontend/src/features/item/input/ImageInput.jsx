/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";

const formatSize = (bytes) =>
  (bytes / (1024 * 1024)).toLocaleString("de-DE", {
    maximumFractionDigits: 2,
  });

export default function ImageInput({ images, setImages }) {
  const [inputId, setInputId] = useState(0);
  const [selectedImageIndex, setSelectedImageIndex] = useState(null);

  const totalSize = images.reduce((sum, image) => sum + image.size, 0);

  const handleUpload = (event) => {
    const files = Array.from(event.target.files ?? []);

    if (files.length > 0) {
      setImages((prev) => [...prev, ...files]);
      setInputId((prev) => prev + 1);
    }
  };

  const handleDeleteImage = (indexToDelete) => {
    setImages((prevImages) =>
      prevImages.filter((image, index) => index !== indexToDelete),
    );
    setSelectedImageIndex(null);
  };

  const handleChangeImageOrder = (index) => {
    if (images.length < 2) {
      return;
    }
    if (selectedImageIndex === null) {
      // Erstes Bild auswählen
      setSelectedImageIndex(index);
      return;
    }

    if (selectedImageIndex === index) {
      // Auswahl aufheben
      setSelectedImageIndex(null);
      return;
    }

    // Positionen tauschen
    setImages((prevImages) => {
      const updatedImages = [...prevImages];

      [updatedImages[selectedImageIndex], updatedImages[index]] = [
        updatedImages[index],
        updatedImages[selectedImageIndex],
      ];

      return updatedImages;
    });

    setSelectedImageIndex(null);
  };

  return (
    <div>
      <div
        className={`border-dark-subtle rounded d-flex flex-wrap gap-2 justify-content-start ${images.length > 0 && "p-2"}`}
        style={{
          border: images.length > 0 && "1px dashed",
        }}
      >
        <div className={`p-0 m-0 ${images.length === 0 && "container-fluid"}`}>
          {/* key ändert sich -> aktuelles <input> entfernen und ein neues <input> erstellen */}
          <input
            key={inputId}
            id={`images-${inputId}`}
            name="images"
            type="file"
            multiple
            accept="image/*"
            className="d-none"
            onChange={handleUpload}
          />
          <label
            htmlFor={`images-${inputId}`}
            className="btn btn-outline-light text-dark d-flex justify-content-center align-items-center p-0 m-0"
            style={{
              cursor: "pointer",
              width: images.length === 0 ? "100%" : "12rem",
              height: "12rem",
              border: "1px solid #adb5bd",
            }}
          >
            <i className="bi bi-images me-2"></i>Bilder hinzufügen
          </label>
        </div>
        {/* Hochgeladene Bilder */}
        {images.map((image, index) => {
          const imageUrl = URL.createObjectURL(image);
          return (
            <div className="position-relative" key={`${image.name}-${index}`}>
              <img
                src={imageUrl}
                alt={image.name}
                className={`img-thumbnail p-0 m-0 ${
                  selectedImageIndex === index
                    ? "border border-primary border-4"
                    : ""
                }`}
                onClick={() => handleChangeImageOrder(index)}
                style={{
                  width: "12rem",
                  height: "12rem",
                  objectFit: "contain",
                  backgroundColor: "#000000",
                  cursor: "pointer",
                }}
              />
              {/* Positionsnummer */}
              <span className="position-absolute top-0 start-0 m-2 badge text-bg-light">
                {index + 1}
              </span>
              {/* Bild löschen */}
              <button
                className="top-0 end-0 m-2 position-absolute btn
              btn-outline-light d-flex justify-content-center align-items-center"
                style={{
                  width: "2rem",
                  height: "2rem",
                  backgroundColor: "#9d9d9db9",
                }}
                onClick={() => {
                  handleDeleteImage(index);
                }}
              >
                <i className="bi bi-x"></i>
              </button>
            </div>
          );
        })}
        {images.length > 1 && (
          <p className="text-muted small mt-2 container-fluid">
            <i className="bi bi-arrow-left-right me-1"></i>
            Reihenfolge bestimmen: Zwei Bilder nacheinander anklicken, um ihre
            Positionen zu tauschen.
          </p>
        )}
      </div>
      <p className="text-muted text-end">
        <small>
          {images.length}/10 Bilder - {formatSize(totalSize)}/50 MB
        </small>
      </p>
    </div>
  );
}
