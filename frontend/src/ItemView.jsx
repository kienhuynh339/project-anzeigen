import { useState } from "react";

const userData = {
  profilname: "Sarah",
};

const item = {
  id: 10,
  name: "Reihenhaus",
  description: `
Objektbeschreibung:
Dieses attraktive Reihenhaus bietet ein angenehmes Wohnambiente und vielfältige Möglichkeiten zur individuellen Gestaltung. Es eignet sich ideal für Paare, Familien oder Menschen, die sich den Traum vom eigenen Zuhause erfüllen möchten. Die durchdachte Raumaufteilung ermöglicht eine komfortable Nutzung der Wohnbereiche und schafft eine angenehme Atmosphäre für den Alltag.

Lage und Umgebung:
Das Reihenhaus befindet sich in Aachen-Brand, einem beliebten Stadtteil im Südosten von Aachen. Die Umgebung zeichnet sich durch eine angenehme Wohnatmosphäre und eine gute Infrastruktur aus. Einkaufsmöglichkeiten, Bäckereien, Restaurants, Schulen und Kindergärten sind je nach genauer Lage gut erreichbar. Öffentliche Verkehrsmittel bieten eine Anbindung an die Aachener Innenstadt und das Umland.

Raumaufteilung:
Die Wohnfläche bietet Platz für unterschiedliche Nutzungsmöglichkeiten. Ein großzügiger Wohn- und Essbereich kann als zentraler Treffpunkt dienen. Ergänzend kommen eine Küche, mehrere Schlaf- oder Arbeitszimmer sowie ein Badezimmer infrage. Weitere Räume können abhängig von der tatsächlichen Immobilie als Gästezimmer, Büro oder Hobbyraum genutzt werden.

Ausstattung:
Die konkrete Ausstattung hängt vom Zustand und den individuellen Merkmalen des Hauses ab. Für eine vollständige Objektbeschreibung sollten Angaben zu Heizung, Fenstern, Bodenbelägen, Badezimmer, Küche, Dämmung und möglichen Modernisierungen ergänzt werden. Auch Informationen zu Baujahr und Energieeffizienz sind für Interessenten hilfreich.

Garten und Außenbereich:
Ein möglicher Garten oder eine Terrasse bietet zusätzlichen Raum zur Erholung und Freizeitgestaltung. Hier lassen sich gemütliche Sitzbereiche, eine kleine Grünfläche oder ein Platz für gemeinsame Aktivitäten im Freien gestalten. Die tatsächliche Größe und Ausstattung des Außenbereichs sind noch zu ergänzen.

Besondere Merkmale:
Das Reihenhaus verbindet die Vorteile einer eigenständigen Wohnimmobilie mit einer häufig platzsparenden Grundstücksnutzung. Es bietet Potenzial für individuelle Einrichtungsideen und unterschiedliche Lebenskonzepte. Je nach Objekt können zusätzliche Nutzflächen wie ein Keller, ein Dachboden oder ein Stellplatz vorhanden sein.
  `,
  price: 29.99,
  images: [
    "https://placehold.co/600x400?text=Bild+1",
    "https://placehold.co/600x400?text=Bild+2",
    "https://placehold.co/600x400?text=Bild+3",
  ],
  location: "52076 Aachen-Brand (...km)",
  timestamp: "08.10.2026",
};

export default function ItemView() {
  const [isFavorite, setIsFavorite] = useState(false);
  return (
    <>
      <div className="container-fluid">
        <div className="row mt-2">
          {/* Linke Seite */}
          <div className="col-7">
            {/* Galerie */}
            <ImageGallery item={item} />
            {/* Beschreibung */}
            <h4 className="mt-3 fw-bold">Beschreibung</h4>
            <p style={{ whiteSpace: "pre-line" }}>{item.description}</p>
          </div>
          {/* Rechte Seite */}
          <div className="col-5">
            {/* Angebotstitel */}
            <h4 className="fw-bold">{item.name}</h4>
            <div className="border-top"></div>
            {/* Anbieter */}
            <div className="align-items-center">
              <a className="text-decoration-none text-dark" href="#">
                <img
                  src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${userData.profilname.charAt(0)}`}
                  className="rounded-circle my-2"
                  width="50"
                  height="50"
                  style={{ objectFit: "cover" }}
                />
                <span className="fw-bold ms-2">{userData.profilname}</span>
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
              <p className="card-text text-secondary m-0">{item.location}</p>
            </div>
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-calendar3 text-body-tertiary"></i>
              <p className="text-secondary m-0">{item.timestamp}</p>
            </div>
            <div className="d-flex flex-column gap-3 mt-5 ">
              {/* Nachricht schreiben */}
              <button type="button" className="btn btn-outline-success">
                <i className="bi bi-chat-dots"></i> Nachricht schreiben
              </button>
              {/* Zur Merkliste hinzufügen / Von Merkliste entfernen */}
              <button
                type="button"
                className="btn btn-outline-success"
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

function ImageGallery({ item }) {
  const [currentIndex, setCurrentIndex] = useState(0);

  const images = item.images ?? [];
  const hasImages = images.length > 0;

  const previousImage = () => {
    setCurrentIndex((index) => (index - 1 + images.length) % images.length);
  };
  const nextImage = () => {
    setCurrentIndex((currentIndex + 1) % images.length);
  };

  return (
    <div
      className="container-fluid position-relative bg-dark text-center"
      style={{ height: "55ch" }}
    >
      {hasImages ? (
        <>
          <img className="h-100" src={images[currentIndex]} />
          {images.length > 1 && (
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
                {currentIndex + 1} / {images.length}
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
