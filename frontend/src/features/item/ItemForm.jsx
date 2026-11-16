/**
 * @author Trung Kien Huynh
 */
import CategorySelect from "./input/CategorySelect";
import ImageInput from "./input/ImageInput";
import PostcodeInput from "./input/PostcodeInput";
import LocationInput from "./input/LocationInput";

export default function ItemForm({
  item,
  setItem,
  images,
  setImages,
  notification,
  initialLocationInputValue = "",
  children,
}) {
  if (!item) {
    return;
  }

  // value(prev[key]) ist "(prev)=>{...}" in "set((prev)=>{...})"
  const updateItemDetails = (key, value) => {
    setItem((prev) => ({
      ...prev,
      [key]: typeof value === "function" ? value(prev[key]) : value,
    }));
  };

  return (
    <div
      className="container border-dark-subtle border rounded mx-auto mt-3 pb-3 shadow-sm "
      style={{ width: "70rem" }}
    >
      {/* Titel */}
      <div className={"row mx-1 mt-3 align-items-center"}>
        <label htmlFor="title" className="fw-bold col-2">
          Titel *
        </label>
        <div className="col">
          <input
            type="text"
            id="title"
            name="title"
            className="form-control border-dark-subtle"
            maxLength={100}
            value={item.title}
            onChange={(e) => updateItemDetails("title", e.target.value)}
          />
        </div>
      </div>
      <p className="text-muted text-end me-3">
        <small>{item.title.trim().length}/100 Zeichen</small>
      </p>
      {/* Kategorie */}
      <div className={"row mx-1 my-3 align-items-center"}>
        <label className="fw-bold col-2">Kategorie</label>
        <div className="col">
          <CategorySelect
            categoryId={item.categoryId}
            setCategoryId={(value) => updateItemDetails("categoryId", value)}
          ></CategorySelect>
        </div>
      </div>
      {/* Beschreibung */}
      <div className={"row mx-1 mt-3 align-items-center"}>
        <label htmlFor="description" className="fw-bold col-2">
          Beschreibung
        </label>
        <div className="col">
          <textarea
            id="description"
            name="description"
            rows="4"
            className="form-control border-dark-subtle"
            maxLength={4000}
            value={item.description}
            onChange={(e) => updateItemDetails("description", e.target.value)}
          />
        </div>
      </div>
      <p className="text-muted text-end me-3">
        <small>{item.description.length}/4000 Zeichen</small>
      </p>
      {/* Preis */}
      <div className={"row mx-1 my-3 align-items-center"}>
        <label htmlFor="price" className="fw-bold col-2">
          Preis *
        </label>
        <div className="col-5 d-flex align-items-center">
          <input
            type="text"
            inputMode="numeric"
            id="price"
            name="price"
            className="form-control border-dark-subtle"
            value={item.price}
            onChange={(e) => {
              const value = e.target.value;
              if (/^(|0|[1-9]\d*)$/.test(value)) {
                updateItemDetails("price", value);
              }
            }}
          />
          &nbsp;,00&euro;
        </div>
      </div>
      {/* Bilder*/}
      <div className={"row mx-1 mt-3 align-items-center"}>
        <label className="fw-bold col-2">Bilder</label>
        <div className="col">
          <ImageInput images={images} setImages={setImages}></ImageInput>
        </div>
      </div>

      {/* Ort */}
      <div className={"row mx-1 my-3 align-items-center"}>
        <span className="fw-bold col-2">Ort *</span>
        {/* PLZ */}
        <div className="col-2">
          <PostcodeInput
            postcode={item.postcode}
            setPostcode={(value) => updateItemDetails("postcode", value)}
            width="8rem"
            dropListAbove={true}
          ></PostcodeInput>
        </div>
        {/* Ort */}
        <div className="col">
          <LocationInput
            postcode={item.postcode}
            setLocationId={(value) => updateItemDetails("locationId", value)}
            setLocationExtraInfo={(value) =>
              updateItemDetails("locationExtraInfo", value)
            }
            dropListAbove={true}
            width="100%"
            initialInputValue={initialLocationInputValue}
          ></LocationInput>
        </div>
      </div>
      {notification && (
        // Fehlermeldung
        <div className={"row mx-1 my-3 align-items-center"}>
          <span className="bg-danger-subtle border rounded py-2 border-danger-subtle  text-danger">
            {notification}
          </span>
        </div>
      )}
      <p className="text-muted small text-end pe-3">* Pflichtfeld</p>
      {/*Knöpfe */}
      <div
        className={
          "border-dark-subtle d-flex mt-5 mb-3 me-3 justify-content-end gap-4"
        }
      >
        {children}
      </div>
    </div>
  );
}
