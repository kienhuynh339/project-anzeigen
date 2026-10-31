/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { userId } from "../../utils/userId";
import { createItemFormData } from "../../utils/createItemFormData";
import { createItemRequest } from "../../utils/createItemRequest";
import { useParams } from "react-router";
import ItemForm from "./ItemForm";
import ErrorNotification from "../../components/ErrorNotification";
export default function ContinueDraft() {
  const { itemId } = useParams();
  const [error, setError] = useState(false);
  const [item, setItem] = useState(null);
  const [images, setImages] = useState([]);

  if (error) {
    return (
      <ErrorNotification
        description={"Diese Seite gibt es nicht."}
      ></ErrorNotification>
    );
  }

  useEffect(() => {
    // Prüfe, ob itemId gültig ist
    setError(!/^[1-9]\d*$/.test(itemId));
  }, [itemId]);

  const itemData = useFetch(
    `http://localhost:8080/api/items/${itemId}?loggedInUserId=${userId}`,
    [itemId],
  );

  useEffect(() => {
    if (itemData !== null) {
      setItem(itemData);
    } else {
      setError(true);
    }
  }, [itemData]);

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
  };
  const handlePublishItem = () => {
    handleSubmit("ACTIVE");
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
