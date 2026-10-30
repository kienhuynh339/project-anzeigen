/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { userId } from "../../utils/userId";
import { createItemFormData } from "../../utils/createItemFormData";
import { createItemRequest } from "../../utils/createItemRequest";
import { useNavigate } from "react-router";
import ItemForm from "./ItemForm";
export default function EditItem() {
  const navigate = useNavigate();
  const [item, setItem] = useState({
    title: "",
    categoryId: 1,
    description: "",
    posterId: userId,
    locationId: "",
    postcode: "",
    locationExtraInfo: "",
    price: "",
    status: "ACTIVE",
  });
  const [images, setImages] = useState([]);

  const handleCancel = () => {
    navigate("/meine-anzeigen");
  };
  const handleSave = () => {
    const formData = createItemFormData(
      item.title,
      item.categoryId,
      item.description,
      item.posterId,
      item.locationId,
      item.postcode,
      item.locationExtraInfo,
      item.price,
      images,
      "ACTIVE",
    );
    createItemRequest(formData);
    navigate("/meine-anzeigen");
  };

  return (
    <div className="container-fluid ">
      <h2 className="py-2 m-0">Anzeige bearbeiten</h2>
      <ItemForm
        item={item}
        setItem={setItem}
        images={images}
        setImages={setImages}
      >
        {/* Entwurf speichern */}
        <button
          className="btn btn-secondary"
          type="button"
          onClick={handleCancel}
        >
          Abbrechen
        </button>
        {/* Anzeige veröffentlichen */}
        <button className="btn btn-primary" type="button" onClick={handleSave}>
          Speichern
        </button>
      </ItemForm>
      <div className="py-4"></div>
    </div>
  );
}
