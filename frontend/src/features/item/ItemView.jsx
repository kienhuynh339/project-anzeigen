import { useState, useEffect } from "react";
import { useParams } from "react-router";
import DialogWindow from "../../components/DialogWindow";
import { useFetch } from "../../hooks/useFetch";

export default function ItemView() {
  const [isFavorite, setIsFavorite] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [message, setMessage] = useState("");
  const { itemId } = useParams();

  const item = useFetch(`http://localhost:8080/api/items/${itemId}`, []) ?? {
    id: "",
    poster: { profileName: "" },
    title: "",
    description: "",
    location: { name: "", plz: "", extraInfo: "" },
    price: 0,
    createdAt: "",
    status: "",
  };

  const imageIds =
    useFetch(`http://localhost:8080/api/images/item/${itemId}`, []) ?? [];

  const handleDialogOpen = () => {
    setIsDialogOpen(true);
  };
  const handleDialogClose = () => {
    setIsDialogOpen(false);
  };
  const handleSend = (event) => {
    event.preventDefault();

    setIsDialogOpen(false);
  };

  return (
    <>
      <div className="container-fluid">
        <div className="row mt-2">
          {/* Linke Seite */}
          <div className="col-7">
            {/* Galerie */}
            <ImageGallery imageIds={imageIds} />
            {/* Beschreibung */}
            <h4 className="mt-3 fw-bold">Beschreibung</h4>
            <p style={{ whiteSpace: "pre-line" }}>
              {item.description.replace(
                /\\n/g /*"\\n" funktioniert nur für das erste Vorkommen*/,
                "\n",
              )}
            </p>
          </div>
          {/* Rechte Seite */}
          <div className="col-5">
            {/* Angebotstitel */}
            <h4 className="fw-bold">{item.title}</h4>
            <div className="border-top"></div>
            {/* Anbieter */}
            <div className="align-items-center">
              <a className="text-decoration-none text-dark" href="#">
                <img
                  src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${item.poster.profileName.charAt(0)}`}
                  className="rounded-circle my-2"
                  width="50"
                  height="50"
                  style={{ objectFit: "cover" }}
                />
                <span className="fw-bold ms-2">{item.poster.profileName}</span>
              </a>
            </div>
            <div className="border-top"></div>
            {/* Preis */}
            <h4 className="fw-bold mt-3">
              {item.price.toLocaleString("de-DE", {
                style: "currency",
                currency: "EUR",
              })}
            </h4>
            {/* Ort, Datum */}
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-geo-alt-fill text-body-tertiary"></i>
              <p className="card-text text-secondary m-0">
                {item.location.name}
              </p>
            </div>
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-calendar3 text-body-tertiary"></i>
              <p className="text-secondary m-0">{item.createdAt}</p>
            </div>
            <div className="d-flex flex-column gap-3 mt-5 ">
              {/* Nachricht schreiben */}
              <button
                type="button"
                className="btn btn-outline-primary"
                onClick={handleDialogOpen}
              >
                <i className="bi bi-chat-dots"></i> Nachricht schreiben
              </button>
              <DialogWindow
                isOpen={isDialogOpen}
                dialogWidth={"70ch"}
                dialogTitle={"Nachricht schreiben"}
                dialogContent={
                  <div>
                    <span className=" mb-3">
                      {item.title.length > 50
                        ? item.title.slice(0, 50) + "..."
                        : item.title}{" "}
                      •{" "}
                      {item.price.toLocaleString("de-DE", {
                        style: "currency",
                        currency: "EUR",
                      })}
                    </span>
                    <textarea
                      className="form-control"
                      value={message}
                      onChange={(e) => setMessage(e.target.value)}
                      rows={4}
                    />
                  </div>
                }
                dialogButtons={[
                  <button
                    className="btn btn-secondary"
                    onClick={handleDialogClose}
                  >
                    Abbrechen
                  </button>,
                  <button className="btn btn-primary" onClick={handleSend}>
                    Senden
                  </button>,
                ]}
              />
              {/* Zur Merkliste hinzufügen / Von Merkliste entfernen */}
              <button
                type="button"
                className="btn btn-outline-primary"
                onClick={() => setIsFavorite(!isFavorite)}
              >
                {isFavorite ? (
                  <>
                    <i className="bi bi-heart-fill"></i> Von Merkliste entfernen
                  </>
                ) : (
                  <>
                    <i className="bi bi-heart"></i> Zur Merkliste hinzufügen
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

function ImageGallery({ imageIds }) {
  const [currentIndex, setCurrentIndex] = useState(0);

  const hasImages = imageIds.length > 0;

  const previousImage = () => {
    setCurrentIndex((index) => (index - 1 + imageIds.length) % imageIds.length);
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
            style={{ width: "100%", height: "100%", objectFit: "contain" }}
          />
          {imageIds.length > 1 && (
            <>
              <button
                className="btn btn-dark position-absolute top-50 start-0 translate-middle-y ms-2 rounded-circle"
                type="button"
                onClick={previousImage}
              >
                &#10094;
              </button>
              <button
                className="btn btn-dark position-absolute top-50 end-0 translate-middle-y me-2 rounded-circle"
                type="button"
                onClick={nextImage}
              >
                &#10095;
              </button>
              <span className="badge bg-dark position-absolute bottom-0 end-0 m-2">
                {currentIndex + 1} / {imageIds.length}
              </span>
            </>
          )}
        </>
      ) : (
        <img
          className="h-100"
          src="https://placehold.co/600x400?text=Kein+Bild"
        ></img>
      )}
    </div>
  );
}
