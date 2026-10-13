import React from "react";

export default function CreateItem() {
  const [images, setImages] = React.useState([]);
  const handleImageChange = (event) => {
    setImages(Array.from(event.target.files));
  };
  return (
    <>
      <div className="container-fluid mb-5">
        <h2 className="py-2 m-0">Anzeige erstellen</h2>
        {/* Benutzerdaten anzeigen */}
        <div
          className="container border-dark-subtle border rounded mx-auto mt-3 shadow-sm "
          style={{ width: "120ch" }}
        >
          {/* Titel */}
          <div
            className={"border-dark-subtle row mx-1 my-3 align-items-center"}
          >
            <label htmlFor="title" className="fw-bold col-2">
              Titel
            </label>
            <div className="col">
              <input
                type="text"
                id="title"
                name="title"
                className="form-control border-dark-subtle"
              />
            </div>
          </div>
          {/* Kategorie */}
          <div
            className={"border-dark-subtle row mx-1 my-3 align-items-center"}
          >
            <label htmlFor="category" className="fw-bold col-2">
              Kategorie
            </label>
            <div className="col">
              <input
                type="text"
                id="category"
                name="category"
                className="form-control border-dark-subtle"
              />
            </div>
          </div>
          {/* Beschreibung */}
          <div
            className={"border-dark-subtle row mx-1 my-3 align-items-center"}
          >
            <label htmlFor="description" className="fw-bold col-2">
              Beschreibung
            </label>
            <div className="col">
              <textarea
                id="description"
                name="description"
                rows="4"
                className="form-control border-dark-subtle"
              />
            </div>
          </div>
          {/* Preis */}
          <div
            className={"border-dark-subtle row mx-1 my-3 align-items-center"}
          >
            <label htmlFor="price" className="fw-bold col-2">
              Preis
            </label>
            <div className="col">
              <input
                type="number"
                id="price"
                name="price"
                className="form-control border-dark-subtle"
              />
            </div>
          </div>
          {/* Bilder Input*/}
          <div
            className={
              "border-dark-subtle row mx-1 mt-3 mb-1 align-items-center"
            }
          >
            <label htmlFor="titel" className="fw-bold col-2">
              Bilder
            </label>
            <div className="col">
              <input
                id="images"
                name="images"
                type="file"
                multiple
                // Browser erlaubt nur Bilder
                accept="image/*"
                className="form-control border-dark-subtle"
                onChange={handleImageChange}
              />
            </div>
          </div>
          {/* Bilder*/}
          <div
            className={
              "border-dark-subtle row mx-1 mb-3 mt-0 align-items-center"
            }
          >
            <div className=" col-2"></div>
            <div className="col">
              <div
                className="border-dark-subtle rounded"
                style={{
                  border: "1px dashed",
                }}
              >
                {images.map((image, index) => {
                  const imageUrl = URL.createObjectURL(image);
                  return (
                    <>
                      <img
                        key={`${image.name}-${index}`}
                        src={imageUrl}
                        alt={image.name}
                        className="img-thumbnail"
                        style={{
                          width: "20ch",
                          height: "20ch",
                          objectFit: "contain",
                          backgroundColor: "#000000",
                        }}
                      />
                    </>
                  );
                })}
              </div>
            </div>
          </div>
          {/* Ort */}
          <div
            className={"border-dark-subtle row mx-1 my-3 align-items-center"}
          >
            <span className="fw-bold col-2">Ort</span>
            {/* PLZ */}
            <div className="col">
              <input
                type="number"
                id="plz"
                name="plz"
                className="form-control border-dark-subtle"
              />
            </div>
            {/* Ort */}
            <div className="col">
              <input
                type="text"
                id="city"
                name="city"
                className="form-control border-dark-subtle"
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
