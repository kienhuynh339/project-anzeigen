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
import ItemTitle from "../../components/ItemTitle";
import { translateItemStatus } from "../../utils/translateItemStatus";
import { truncateText } from "../../utils/truncatetext";
import noImage from "../../assets/noImage.jpg";
import ItemEditButton from "../../components/ItemEditButton";
import { useAuth } from "../../contexts/AuthContext";

export default function ItemCard({ item, itemCardType }) {
  const { user } = useAuth();
  return (
    <>
      <Link
        className="text-decoration-none hover-effect p-0"
        to={`/anzeigen/${item.id}`}
        style={{ width: "22rem" }}
      >
        <div className="card shadow-sm h-100">
          {/* Bild */}
          <div className="position-relative">
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
            {!!user &&
              user?.id != item.posterId &&
              itemCardType === itemCardTypes.ITEM_WITH_FAVOR_BUTTON && (
                <ItemFavorButton
                  itemId={item.id}
                  isFavorite={item.favorite}
                  className="top-0 end-0 m-1 rounded position-absolute btn"
                  style={{ backgroundColor: "#9d9d9db9" }}
                ></ItemFavorButton>
              )}
            {item.status && item.status !== "ACTIVE" && (
              <span className="text-white position-absolute top-50 start-50 translate-middle">
                {translateItemStatus(item.status)}
              </span>
            )}
          </div>

          <div className="card-body d-flex flex-column justify-content-between pt-0">
            <div className="container-fluid p-0 m-0">
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
                <ItemTitle
                  itemStatus={item.status}
                  itemTitle={item.title}
                  textLimit={80}
                ></ItemTitle>
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
                    {item.status === "DRAFT" ? (
                      <ItemEditButton
                        itemId={item.id}
                        itemStatus={item.status}
                      ></ItemEditButton>
                    ) : item.status === "ACTIVE" ? (
                      <ItemReserveButton itemId={item.id}></ItemReserveButton>
                    ) : item.status === "RESERVED" ? (
                      <ItemActivateButton itemId={item.id}></ItemActivateButton>
                    ) : null}
                  </>
                )}
                {itemCardType === itemCardTypes.MY_FAVORITE_ITEM && (
                  <>
                    <FavoriteItemDeleteButton
                      itemId={item.id}
                      userId={user.id}
                    ></FavoriteItemDeleteButton>
                    {item.status !== "DELETED" && (
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
