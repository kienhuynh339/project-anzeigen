/**
 * @author Trung Kien Huynh
 */
import { useState, useEffect } from "react";
import { useParams } from "react-router";
import ItemForm from "./ItemForm";
import { getPath, useFetch } from "../../hooks/useFetch";
import { useNavigate } from "react-router";
import { updateItem } from "../../api/itemApi";
import { getImage } from "../../api/imageApi";
import { Navigate } from "react-router";

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

export default function EditItem() {
  const navigate = useNavigate();
  const { itemId } = useParams();
  const [item, setItem] = useState(null);
  const [images, setImages] = useState([]);

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

  const handleSubmit = (status) => {
    updateItem(
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
  };

  const handleSaveDraft = () => {
    handleSubmit("DRAFT");
    navigate("/meine-anzeigen");
  };

  const handlePublishItem = () => {
    handleSubmit("ACTIVE");
    navigate("/meine-anzeigen");
  };

  const handleSaveItem = () => {
    handleSubmit(fetchedItem.status);
    navigate("/meine-anzeigen");
  };

  const handleCancel = () => {
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
        initialLocationInputValue={initialLocationInputValue}
      >
        <button
          className="btn btn-secondary"
          type="button"
          onClick={handleCancel}
        >
          Abbrechen
        </button>
        {/* Entwurf speichern */}
        {fetchedItem?.status === "DRAFT" && (
          <button
            className="btn btn-outline-success"
            type="button"
            onClick={handleSaveDraft}
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
