/**
 * @author Trung Kien Huynh
 */
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router";
import MessageButton from "../../components/MessageButton";
import { useFetch } from "../../hooks/useFetch";
import ItemDeleteButton from "../../components/ItemDeleteButton";
import ItemTitle from "../../components/ItemTitle";
import { translateItemStatus } from "../../utils/translateItemStatus";
import ItemFavorButton from "../../components/ItemFavorButton";
import ItemReserveButton from "../../components/ItemReserveButton";
import ItemActivateButton from "../../components/ItemActivateButton";
import noImage from "../../assets/noImage.jpg";
import ItemEditButton from "../../components/ItemEditButton";
import { useAuth } from "../../contexts/AuthContext";

export default function ItemView() {
  const { user } = useAuth();
  const { itemId } = useParams();

  const { data: item } = useFetch(
    `http://localhost:8080/api/items/${itemId}`,
    [],
    {
      id: "",
      poster: { profileName: "", id: "" },
      title: "",
      description: "",
      location: { name: "", plz: "", extraInfo: "" },
      price: 0,
      createdAt: "",
      status: "",
    },
  );

  const { data: imageIds } = useFetch(
    `http://localhost:8080/api/images/item/${itemId}`,
    [],
    [],
  );

  return (
    <>
      <div className="container-fluid">
        <div className="row mt-2">
          {/* Linke Seite */}
          <div className="col-7">
            {/* Galerie */}
            <ImageGallery imageIds={imageIds} itemStatus={item.status} />
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
            <h4 className="fw-bold">
              <ItemTitle
                itemTitle={item.title}
                itemStatus={item.status}
              ></ItemTitle>
            </h4>
            <div className="border-top"></div>
            {/* Anbieter */}
            <div className="align-items-center">
              <Link
                className="text-decoration-none text-dark"
                to={`/user/${item.poster.id}`}
              >
                <img
                  src={`https://placehold.co/600x400/${"ABABAB"}/000000?text=${item.poster.profileName.charAt(0)}`}
                  className="rounded-circle my-2"
                  width="50"
                  height="50"
                  style={{ objectFit: "cover" }}
                />
                <span className="fw-bold ms-2">{item.poster.profileName}</span>
              </Link>
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
                {item.location.postcode} {item.location.name}{" "}
                {item.location.extraInfo}
              </p>
            </div>
            <div className="d-flex align-items-center gap-1">
              <i className="bi bi-calendar3 text-body-tertiary"></i>
              <p className="text-secondary m-0">{item.createdAt}</p>
            </div>
            <div className="d-flex flex-column gap-3 mt-5 ">
              {/* Knöpfe für Interessenten */}
              {user && user?.id != item.poster.id && (
                <>
                  {/* Nachricht schreiben */}
                  {item.status !== "DELETED" && (
                    <MessageButton item={item}></MessageButton>
                  )}
                  {/* Zur Merkliste hinzufügen / Von Merkliste entfernen */}
                  <ItemFavorButton
                    userId={user?.id}
                    itemId={item.id}
                    isFavorite={item.favorite}
                    className={"btn btn-outline-danger"}
                    showLabel={true}
                  ></ItemFavorButton>
                </>
              )}
              {/* Knöpfe für Poster der Anzeige */}
              {user && user?.id == item.poster.id && (
                <>
                  {item.status === "ACTIVE" ? (
                    <ItemReserveButton itemId={item.id}></ItemReserveButton>
                  ) : item.status === "RESERVED" ? (
                    <ItemActivateButton itemId={item.id}></ItemActivateButton>
                  ) : null}
                  {item.status !== "DELETED" && (
                    <>
                      <ItemEditButton
                        itemId={item.id}
                        itemStatus={item.status}
                      ></ItemEditButton>
                      <ItemDeleteButton itemId={item.id}>
                        <span className="ps-1">Anzeige löschen</span>
                      </ItemDeleteButton>
                    </>
                  )}
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

function ImageGallery({ imageIds, itemStatus }) {
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
          {currentIndex === 0 && itemStatus && itemStatus !== "ACTIVE" && (
            <span className="text-white position-absolute top-50 start-50 translate-middle">
              {translateItemStatus(itemStatus)}
            </span>
          )}
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
        <img className="h-100" src={noImage}></img>
      )}
    </div>
  );
}
