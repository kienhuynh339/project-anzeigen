/**
 * @author Trung Kien Huynh
 */
import { Link } from "react-router";
import { formatTimestamp, TIMESTAMP_TYPES } from "../../utils/formatTimestamp";
import ItemFavorButton from "./buttons/ItemFavorButton";
import ItemDeleteButton from "./buttons/ItemDeleteButton";
import ItemReserveButton from "./buttons/ItemReserveButton";
import ItemActivateButton from "./buttons/ItemActivateButton";
import FavoriteItemDeleteButton from "./buttons/FavoriteItemDeleteButton";
import MessageButton from "../chat/buttons/MessageButton";
import ItemTitle from "./ItemTitle";
import { translateItemStatus } from "../../utils/translateItemStatus";
import { truncateText } from "../../utils/truncateText";
import noImage from "../../assets/noImage.jpg";
import ItemEditButton from "./buttons/ItemEditButton";
import { useAuthContext } from "../../contexts/AuthContext";

export const ITEM_CARD_TYPES = Object.freeze({
  MY_FAVORITE_ITEM: "MY_FAVORITE_ITEM",
  MY_ITEM: "MY_ITEM",
  ITEM_WITH_FAVOR_BUTTON: "ITEM_WITH_FAVOR_BUTTON",
});

export default function ItemCard({
  item,
  itemCardType,
  deleteItem,
  updateItemStatus,
}) {
  const { user } = useAuthContext();

  return (
    <>
      <Link
        className="text-decoration-none hover-effect p-0"
        to={`/anzeigen/${item.id}`}
        style={{ width: "22rem" }}
      >
        <div className="card shadow-sm h-100">
          <div className="position-relative">
            {/* Anzeigebild */}
            <img
              src={`http://localhost:8080/api/images/item/${item.id}/first`}
              className="card-img-top"
              alt={`item-${item.id}-image`}
              loading="lazy"
              onError={(e) => {
                e.currentTarget.onerror = null;
                e.currentTarget.src = noImage;
              }}
              style={{
                objectFit: "cover",
                height: "22rem",
                filter:
                  item.status && item.status !== "ACTIVE"
                    ? "brightness(0.20)"
                    : "none",
              }}
            />
            {/* Favorite-Knopf */}
            {user &&
              user.id !== item.posterId &&
              itemCardType === ITEM_CARD_TYPES.ITEM_WITH_FAVOR_BUTTON && (
                <ItemFavorButton
                  itemId={item.id}
                  isFavorite={item.favorite}
                  className="top-0 end-0 m-1 rounded position-absolute btn"
                  style={{ backgroundColor: "#9d9d9db9" }}
                ></ItemFavorButton>
              )}
            {/* Anzeigezustand (Gelöscht/Entwurf/Reserviert) */}
            {item.status && item.status !== "ACTIVE" && (
              <span className="text-white position-absolute top-50 start-50 translate-middle">
                {translateItemStatus(item.status)}
              </span>
            )}
          </div>

          <div className="card-body d-flex flex-column justify-content-between pt-0">
            <div className="container-fluid p-0 m-0">
              {/* Ort (ggf. Abstand in km) */}
              <div
                className="d-flex align-items-center gap-1"
                style={{ fontSize: "0.85rem" }}
              >
                <i className="bi bi-geo-alt-fill text-body-tertiary"></i>
                <p className="card-text text-secondary m-0">
                  {`${item.location.postcode} ${item.location.name} ${truncateText(item.location.extraInfo, 13)} ${item.distance ? `(${item.distance.toFixed(1)} km)` : ""}`}
                </p>
              </div>
              <h5 className="card-title">
                {/* Anzeigetitel */}
                <ItemTitle
                  itemStatus={item.status}
                  itemTitle={item.title}
                  textLimit={80}
                ></ItemTitle>
              </h5>
            </div>

            <div className="d-flex flex-column gap-2">
              <div className="d-flex justify-content-between align-items-end">
                {/* Preis */}
                <p className="fs-5 fw-bold m-0">
                  {item.price === 0
                    ? ""
                    : item.price.toLocaleString("de-DE", {
                        style: "currency",
                        currency: "EUR",
                      })}
                </p>
                {/* Zeitstempel */}
                <p className="text-secondary m-0">
                  {formatTimestamp(item.createdAt, TIMESTAMP_TYPES.ITEM)}
                </p>
              </div>
              <div className="d-flex justify-content-between">
                {itemCardType === ITEM_CARD_TYPES.MY_ITEM && (
                  <>
                    {/* Anzeige löschen */}
                    <ItemDeleteButton
                      itemId={item.id}
                      deleteItem={() => deleteItem(item.id)}
                    ></ItemDeleteButton>
                    {item.status === "DRAFT" ? (
                      // Anzeige bearbeiten/fortsetzen
                      <ItemEditButton
                        itemId={item.id}
                        itemStatus={item.status}
                      ></ItemEditButton>
                    ) : item.status === "ACTIVE" ? (
                      // Anzeige reservieren
                      <ItemReserveButton
                        itemId={item.id}
                        updateItemStatus={() =>
                          updateItemStatus(item.id, "RESERVED")
                        }
                      ></ItemReserveButton>
                    ) : item.status === "RESERVED" ? (
                      // Anzeige aktivieren
                      <ItemActivateButton
                        itemId={item.id}
                        updateItemStatus={() =>
                          updateItemStatus(item.id, "ACTIVE")
                        }
                      ></ItemActivateButton>
                    ) : null}
                  </>
                )}
                {itemCardType === ITEM_CARD_TYPES.MY_FAVORITE_ITEM && (
                  <>
                    {/* Anzeige aus der Merkliste entfernen */}
                    <FavoriteItemDeleteButton
                      itemId={item.id}
                      deleteFavoriteItem={() => deleteItem(item.id)}
                    ></FavoriteItemDeleteButton>
                    {item.status !== "DELETED" && (
                      // Nachricht schreiben
                      <MessageButton item={item}></MessageButton>
                    )}
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </Link>
    </>
  );
}
