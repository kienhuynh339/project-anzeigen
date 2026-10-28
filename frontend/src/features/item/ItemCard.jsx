/**
 * @author Trung Kien Huynh
 */
import { Link } from "react-router";
import { formatTimestamp, TimestampType } from "../../utils/formatTimestamp";
import ItemFavorButton from "../../components/ItemFavorButton";
import { itemCardTypes } from "./itemCardTypes";
import ItemDeleteButton from "../../components/ItemDeleteButton";
import ItemReserveButton from "../../components/ItemReserveButton";
import ItemActivateButton from "../../components/ItemActivateButton";
import FavoriteItemDeleteButton from "../../components/FavoriteItemDeleteButton";
import MessageButton from "../../components/MessageButton";

export default function ItemCard({ item, userId, itemCardType }) {
  return (
    <>
      <Link
        className="text-decoration-none hover-effect p-0"
        to={`/anzeigen/${item.id}`}
        style={{ width: "22rem" }}
      >
        <div className="card shadow-sm h-100 position-relative">
          {/* Favorite-Knopf */}
          {itemCardType === itemCardTypes.ITEM_WITH_FAVOR_BUTTON && (
            <ItemFavorButton itemId={item.id} userId={userId}></ItemFavorButton>
          )}
          {/* Bild */}
          <img
            src={`http://localhost:8080/api/images/item/${item.id}/first`}
            className="card-img-top"
            alt={`item-${item.id}-image`}
            loading="lazy"
            style={{ objectFit: "cover", height: "22rem" }}
          />

          <div className="card-body d-flex flex-column justify-content-between pt-0">
            <div className="container-fluid p-0 m-0">
              <div className="d-flex align-items-center gap-1">
                <i className="bi bi-geo-alt-fill text-body-tertiary"></i>
                <p className="card-text text-secondary m-0">
                  {`${item.location.plz} ${item.location.name} ${item.distance ? `(${item.distance.toFixed(2)} km)` : ""}`}
                </p>
              </div>
              <h5 className="card-title">
                {item.title.length > 80
                  ? item.title.slice(0, 80) + "..."
                  : item.title}
              </h5>
            </div>

            <div className="d-flex flex-column gap-2">
              <div className="d-flex justify-content-between align-items-end">
                <p className="fs-5 fw-bold m-0">
                  {item.price == 0
                    ? ""
                    : item.price.toLocaleString("de-DE", {
                        style: "currency",
                        currency: "EUR",
                      })}
                </p>
                <p className="text-secondary m-0">
                  {formatTimestamp(item.createdAt, TimestampType.ITEM)}
                </p>
              </div>
              <div className="d-flex justify-content-between">
                {itemCardType === itemCardTypes.MY_ITEM && (
                  <>
                    <ItemDeleteButton itemId={item.id}></ItemDeleteButton>
                    {item.status == "ACTIVE" ? (
                      <ItemReserveButton itemId={item.id}></ItemReserveButton>
                    ) : (
                      <ItemActivateButton itemId={item.id}></ItemActivateButton>
                    )}
                  </>
                )}
                {itemCardType === itemCardTypes.MY_FAVORITE_ITEM && (
                  <>
                    <FavoriteItemDeleteButton
                      itemId={item.id}
                      userId={userId}
                    ></FavoriteItemDeleteButton>
                    <MessageButton item={item}></MessageButton>
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
