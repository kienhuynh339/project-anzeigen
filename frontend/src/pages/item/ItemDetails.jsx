/**
 * @author Trung Kien Huynh
 */
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router";
import { getPath, useFetch } from "../../hooks/useFetch";
import ItemDeleteButton from "../../features/item/buttons/ItemDeleteButton";
import ItemTitle from "../../features/item/ItemTitle";
import ItemFavorButton from "../../features/item/buttons/ItemFavorButton";
import ItemReserveButton from "../../features/item/buttons/ItemReserveButton";
import ItemActivateButton from "../../features/item/buttons/ItemActivateButton";
import ItemEditButton from "../../features/item/buttons/ItemEditButton";
import { useAuthContext } from "../../contexts/AuthContext";
import ProfileImage from "../../components/ProfileImage";
import ImageGallery from "../../features/image/ImageGallery";
import MessageButton from "../../features/chat/buttons/MessageButton";

const initialItem = {
  id: "",
  poster: { profileName: "", id: "" },
  title: "",
  description: "",
  location: { name: "", plz: "", extraInfo: "" },
  price: 0,
  createdAt: "",
  status: "",
};

export default function ItemDetails() {
  const { user } = useAuthContext();
  const { itemId } = useParams();
  const [item, setItem] = useState(initialItem);

  const { data: fetchedItem } = useFetch(
    getPath.itemByItemId(itemId),
    [],
    initialItem,
  );

  useEffect(() => {
    setItem(fetchedItem);
  }, [fetchedItem]);

  const { data: imageIds } = useFetch(getPath.imageIdsByItemId(itemId), [], []);

  const deleteItem = () => {
    setItem((prev) => ({ ...prev, status: "DELETED" }));
  };

  const reserveItem = () => {
    setItem((prev) => ({ ...prev, status: "RESERVED" }));
  };

  const activateItem = () => {
    setItem((prev) => ({ ...prev, status: "ACTIVE" }));
  };

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
            <div className="d-flex align-items-center my-2">
              <Link
                className="text-decoration-none "
                to={`/user/${item.poster.id}`}
              >
                <ProfileImage
                  firstLetterOfName={item.poster.profileName.charAt(0)}
                  size={"50"}
                ></ProfileImage>
              </Link>
              <Link
                className="text-decoration-none text-dark   "
                to={`/user/${item.poster.id}`}
              >
                <span className="fw-bold ms-2 ">{item.poster.profileName}</span>
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
              {user && user?.id !== item.poster.id && (
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
              {user && user?.id === item.poster.id && (
                <>
                  {/* Reservieren/Aktivieren */}
                  {item.status === "ACTIVE" ? (
                    <ItemReserveButton
                      itemId={item.id}
                      updateItemStatus={reserveItem}
                    ></ItemReserveButton>
                  ) : item.status === "RESERVED" ? (
                    <ItemActivateButton
                      itemId={item.id}
                      updateItemStatus={activateItem}
                    ></ItemActivateButton>
                  ) : null}
                  {item.status !== "DELETED" && (
                    <>
                      {/* Anzeige Bearbeiten/Fortsetzen */}
                      <ItemEditButton
                        itemId={item.id}
                        itemStatus={item.status}
                      ></ItemEditButton>
                      {/* Anzeige löschen */}
                      <ItemDeleteButton
                        itemId={item.id}
                        deleteItem={deleteItem}
                      >
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
