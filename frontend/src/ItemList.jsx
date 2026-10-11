const items = [
  {
    id: 1,
    name: "Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer Wireless Kopfhörer",
    price: 79.99,
    image: "https://placehold.co/600x400?text=Kopfhoerer",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "Gestern, 15:46",
  },
  {
    id: 2,
    name: "Smartwatch",
    price: 129.99,
    image: "https://placehold.co/600x400?text=Smartwatch",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "Gestern, 15:46",
  },
  {
    id: 3,
    name: "Bluetooth Lautsprecher",
    price: 49.99,
    image: "https://placehold.co/600x400?text=Lautsprecher",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 4,
    name: "Mechanische Tastatur",
    price: 89.99,
    image: "https://placehold.co/600x400?text=Tastatur",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 5,
    name: "Gaming Maus",
    price: 39.99,
    image: "https://placehold.co/600x400?text=Maus",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 6,
    name: "USB-C Hub",
    price: 29.99,
    image: "https://placehold.co/600x400?text=USB-C+Hub",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 7,
    name: "Bluetooth Lautsprecher",
    price: 49.99,
    image: "https://placehold.co/600x400?text=Lautsprecher",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 8,
    name: "Mechanische Tastatur",
    price: 89.99,
    image: "https://placehold.co/600x400?text=Tastatur",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 9,
    name: "Gaming Maus",
    price: 39.99,
    image: "https://placehold.co/600x400?text=Maus",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
  {
    id: 10,
    name: "USB-C Hub",
    price: 29.99,
    image: "https://placehold.co/600x400?text=USB-C+Hub",
    location: "52076 Aachen-Brand (...km)",
    timestamp: "08.10.2026",
  },
];

export default function ItemList() {
  return (
    <>
      <div
        className="d-flex justify-content-between
                      align-items-center mb-1"
      >
        <span className="mb-0">
          {items.length} Ergebnisse für „...“ in der Kategorie „...“ in
          "..ort.." gefunden.
        </span>
      </div>

      <div className="row g-2">
        {items.map((item) => (
          <ItemCard key={item.id} item={item} />
        ))}
      </div>
    </>
  );
}

function ItemCard({ item }) {
  return (
    <a className="col-3 text-decoration-none" href="#">
      <div className="card h-100 shadow-sm">
        <img src={item.image} className="card-img-top" alt={item.name} />

        <div className="card-body d-flex flex-column justify-content-between pt-0">
          <div className="container-fluid p-0 m-0">
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-geo-alt-fill text-body-tertiary"></i>
              <p className="card-text text-secondary m-0">{item.location}</p>
            </div>
            <h5 className="card-title">
              {item.name.length > 80
                ? item.name.slice(0, 80) + "..."
                : item.name}
            </h5>
          </div>

          <div className="d-flex justify-content-between">
            <p className="fs-5 fw-bold m-0">
              {item.price.toLocaleString("de-DE", {
                style: "currency",
                currency: "EUR",
              })}
            </p>
            <p className="text-secondary m-0">{item.timestamp}</p>
          </div>
        </div>
      </div>
    </a>
  );
}
