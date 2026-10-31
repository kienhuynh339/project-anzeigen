/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { userId } from "../../utils/userId";
import { createItemFormData } from "../../utils/createItemFormData";
import { createItemRequest } from "../../utils/createItemRequest";
import ItemForm from "./ItemForm";
import { useNavigate } from "react-router";
export default function CreateItem() {
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
    status: "DRAFT",
  });
  const [images, setImages] = useState([]);

  const handleSubmit = (status) => {
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
      status,
    );
    createItemRequest(formData);
  };

  const handleSaveDraft = () => {
    handleSubmit("DRAFT");
    navigate("/meine-anzeigen");
  };
  const handlePublishItem = () => {
    handleSubmit("ACTIVE");
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
      >
        {/* Entwurf speichern */}
        <button
          className="btn btn-outline-success"
          type="button"
          onClick={handleSaveDraft}
        >
          Entwurf speichern
        </button>
        {/* Anzeige veröffentlichen */}
        <button
          className="btn btn-primary"
          type="button"
          onClick={handlePublishItem}
        >
          Anzeige veröffentlichen
        </button>
      </ItemForm>
      <div className="py-4"></div>
    </div>
  );
}
