/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { createItem } from "../../apis/itemApi";
import ItemForm from "../../features/item/ItemForm";
import { useNavigate } from "react-router";
import {
  FIELD_KEYS,
  INPUT_NOTICES,
  validateInput,
} from "../../utils/inputValidation";

const initialItem = {
  title: "",
  categoryId: 1,
  description: "",
  locationId: "",
  postcode: "",
  locationExtraInfo: "",
  price: 0,
  status: "DRAFT",
};

const fieldKeys = [
  FIELD_KEYS.TITLE,
  FIELD_KEYS.DESCRIPTION,
  FIELD_KEYS.PRICE,
  FIELD_KEYS.IMAGES_AMOUNT,
  FIELD_KEYS.IMAGES_TOTAL_SIZE,
  FIELD_KEYS.POSTCODE,
  FIELD_KEYS.LOCATION_ID,
];

export default function CreateItem() {
  const navigate = useNavigate();
  const [item, setItem] = useState(initialItem);
  const [images, setImages] = useState([]);
  const [notification, setNotification] = useState("");
  const [submitting, setSubmitting] = useState(false);

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
      await createItem(
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

  return (
    <div className="container-fluid ">
      <h2 className="py-2 m-0">Anzeigendetails</h2>
      <ItemForm
        item={item}
        setItem={setItem}
        images={images}
        setImages={setImages}
        notification={notification}
      >
        {/* Entwurf speichern */}
        <button
          className="btn btn-outline-success"
          type="button"
          onClick={handleSaveDraft}
          disabled={submitting}
        >
          Entwurf speichern
        </button>
        {/* Anzeige veröffentlichen */}
        <button
          className="btn btn-primary"
          type="button"
          onClick={handlePublishItem}
          disabled={submitting}
        >
          Anzeige veröffentlichen
        </button>
      </ItemForm>
      <div className="py-4"></div>
    </div>
  );
}
