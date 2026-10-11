const products = [
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
];

export default function ItemList() {
  return (
    <>
      <div
        className="d-flex justify-content-between
                      align-items-center mb-1"
      >
        <span className="mb-0">
          {products.length} Ergebnisse für „...“ in der Kategorie „...“ in
          "..ort.." gefunden.
        </span>
      </div>

      <div className="row g-2">
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </>
  );
}

function ProductCard({ product }) {
  return (
    <a className="col-3 text-decoration-none" href="#">
      <div className="card h-100 shadow-sm">
        <img src={product.image} className="card-img-top" alt={product.name} />

        <div className="card-body d-flex flex-column justify-content-between">
          <h5 className="card-title">
            {product.name.length > 80
              ? product.name.slice(0, 80) + "..."
              : product.name}
          </h5>

          <div className="container-fluid p-0 m-0">
            <p className="card-text text-secondary m-0">{product.location}</p>

            <div className="d-flex justify-content-between">
              <p className="fs-5 fw-bold m-0">
                {product.price.toLocaleString("de-DE", {
                  style: "currency",
                  currency: "EUR",
                })}
              </p>
              <p className="text-secondary m-0">{product.timestamp}</p>
            </div>
          </div>
        </div>
      </div>
    </a>
  );
}
