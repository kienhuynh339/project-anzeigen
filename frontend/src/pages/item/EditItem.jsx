/**
 * @author Trung Kien Huynh
 */
import { useState, useEffect } from "react";
import { useParams } from "react-router";
import ItemForm from "../../features/item/ItemForm";
import { getPath, useFetch } from "../../hooks/useFetch";
import { useNavigate } from "react-router";
import { updateItem } from "../../apis/itemApi";
import { getImage } from "../../apis/imageApi";
import { Navigate } from "react-router";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../../utils/inputValidation";

const mapItemToFormData = (item) => ({
  title: item.title,
  categoryId: item.categoryId,
  description: item.description,
  locationId: item.location?.id ?? "",
  postcode: item.location?.postcode ?? "",
  locationExtraInfo: item.location?.extraInfo ?? "",
  price: item.price,
  status: item.status,
});

const fieldKeys = [
  FIELD_KEYS.TITLE,
  FIELD_KEYS.DESCRIPTION,
  FIELD_KEYS.PRICE,
  FIELD_KEYS.IMAGES_AMOUNT,
  FIELD_KEYS.IMAGES_TOTAL_SIZE,
  FIELD_KEYS.POSTCODE,
  FIELD_KEYS.LOCATION_ID,
];

export default function EditItem() {
  const navigate = useNavigate();
  const { itemId } = useParams();
  const [item, setItem] = useState(null);
  const [images, setImages] = useState([]);
  const [notification, setNotification] = useState("");
  const [submitting, setSubmitting] = useState(false);

  let isItemIdValid = /^[1-9]\d*$/.test(itemId);

  const { data: fetchedItem, error: itemDataFetchError } = useFetch(
    isItemIdValid ? getPath.itemByItemIdToEdit(itemId) : null,
    [itemId],
  );

  const { data: imageIds } = useFetch(
    isItemIdValid ? getPath.imageIdsByItemId(itemId) : null,
    [itemId],
    [],
  );

  // Bilder abrufen
  useEffect(() => {
    if (imageIds.length === 0) {
      setImages([]);
      return;
    }
    Promise.all(imageIds.map((id) => getImage(id)))
      .then((fetchedImages) => {
        setImages(fetchedImages);
      })
      .catch((error) => {
        console.error("Fehler beim Laden der Bilder:", error);
      });
  }, [imageIds]);

  useEffect(() => {
    if (fetchedItem !== null) {
      // fetchedItem hat eine andere Struktur als item
      setItem(mapItemToFormData(fetchedItem));
    }
  }, [fetchedItem]);

  if (!isItemIdValid || itemDataFetchError) {
    return <Navigate to="/404" replace />;
  }

  // inputValue von <LocationInput> besteht aus location.name (und location.extraInfo, wenn location.extraInfo nicht leer ist)
  let initialLocationInputValue = "";
  if (fetchedItem !== null) {
    initialLocationInputValue = fetchedItem.location.extraInfo
      ? `${fetchedItem.location.name} ${fetchedItem.location.extraInfo}`
      : `${fetchedItem.location.name}`;
  }

  const handleSubmit = async (status) => {
    for (const key of fieldKeys) {
      const inputValue =
        key === FIELD_KEYS.IMAGES_AMOUNT || key === FIELD_KEYS.IMAGES_TOTAL_SIZE
          ? images
          : item[key];
      if (!validateInput(inputValue, key)) {
        setNotification(INPUT_NOTICES[key]);
        return false;
      }
    }

    setSubmitting(true);

    try {
      await updateItem(
        fetchedItem.id,
        item.title,
        item.categoryId,
        item.description,
        item.locationId,
        item.postcode,
        item.locationExtraInfo,
        item.price,
        status,
        images,
      );
      return true;
    } catch (e) {
      setNotification(e.message);
      return false;
    }
  };

  const handleSaveDraft = async () => {
    const success = await handleSubmit("DRAFT");

    setSubmitting(false);
    if (!success) return;

    navigate("/meine-anzeigen");
  };

  const handlePublishItem = async () => {
    const success = await handleSubmit("ACTIVE");

    setSubmitting(false);
    if (!success) return;

    navigate("/meine-anzeigen");
  };

  const handleSaveItem = async () => {
    const success = await handleSubmit(fetchedItem.status);

    setSubmitting(false);
    if (!success) return;

    navigate("/meine-anzeigen");
  };

  const handleCancel = () => {
    setSubmitting(false);
    navigate("/meine-anzeigen");
  };

  return (
    <div className="container-fluid ">
      <h2 className="py-2 m-0">Anzeigendetails</h2>
      <ItemForm
        item={item}
        setItem={setItem}
        images={images}
        setImages={setImages}
        notification={notification}
        initialLocationInputValue={initialLocationInputValue}
      >
        <button
          className="btn btn-secondary"
          type="button"
          onClick={handleCancel}
          disabled={submitting}
        >
          Abbrechen
        </button>
        {/* Entwurf speichern */}
        {fetchedItem?.status === "DRAFT" && (
          <button
            className="btn btn-outline-success"
            type="button"
            onClick={handleSaveDraft}
            disabled={submitting}
          >
            Entwurf speichern
          </button>
        )}
        {/* Anzeige veröffentlichen/speichern */}
        <button
          className="btn btn-primary"
          type="button"
          onClick={
            fetchedItem?.status === "ACTIVE" ||
            fetchedItem?.status === "RESERVED"
              ? handleSaveItem
              : handlePublishItem
          }
          disabled={submitting}
        >
          {fetchedItem?.status === "DRAFT"
            ? "Anzeige veröffentlichen"
            : "Anzeige speichern"}
        </button>
      </ItemForm>
      <div className="py-4"></div>
    </div>
  );
}
