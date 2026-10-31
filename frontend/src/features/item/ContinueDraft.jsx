// /**
//  * @author Trung Kien Huynh
//  */
// import { useState, useEffect } from "react";
// import { userId } from "../../utils/userId";
// import { useParams } from "react-router";
// import ItemForm from "./ItemForm";
// import ErrorNotification from "../../components/ErrorNotification";
// import { updateItemFormData } from "../../utils/updateItemFormData";
// import { updateItemRequest } from "../../utils/UpdateItemRequest";
// import { useFetch } from "../../hooks/useFetch";
// import itemToUpdateItemRequestConverter from "../../utils/itemToUpdateItemRequestConverter";
// export default function ContinueDraft() {
//   const { itemId } = useParams();
//   const [item, setItem] = useState(null);
//   const [images, setImages] = useState([]);

//   let isItemIdValid = /^[1-9]\d*$/.test(itemId);

//   const { data: fetchedItem, error: itemDataFetchError } = useFetch(
//     isItemIdValid
//       ? `http://localhost:8080/api/items/${itemId}?loggedInUserId=${userId}`
//       : null,
//     [itemId],
//   );

//   const { data: imageIds } = useFetch(
//     isItemIdValid ? `http://localhost:8080/api/images/item/${itemId}` : null,
//     [itemId],
//     [],
//   );

//   useEffect(() => {
//     if (imageIds.length === 0) {
//       setImages([]);
//       return;
//     }
//     Promise.all(
//       imageIds.map((id) =>
//         fetch(`http://localhost:8080/api/images/${id}`)
//           .then((response) => {
//             if (!response.ok) {
//               throw new Error(`Bild ${id} konnte nicht geladen werden.`);
//             }
//             return response.blob();
//           })
//           .then(
//             (blob) =>
//               new File([blob], `image-${id}`, {
//                 type: blob.type,
//               }),
//           ),
//       ),
//     )
//       .then((fetchedImages) => {
//         setImages(fetchedImages);
//       })
//       .catch((error) => {
//         console.error("Fehler beim Laden der Bilder:", error);
//       });
//   }, [imageIds]);

//   useEffect(() => {
//     if (fetchedItem !== null) {
//       setItem(itemToUpdateItemRequestConverter(fetchedItem));
//     }
//   }, [fetchedItem]);

//   let initialLocationInputValue = "";
//   if (fetchedItem !== null && fetchedItem.location !== null) {
//     initialLocationInputValue = fetchedItem.location.extraInfo
//       ? `${fetchedItem.location.name} ${fetchedItem.location.extraInfo}`
//       : `${fetchedItem.location.name}`;
//   }

//   if (!isItemIdValid || itemDataFetchError) {
//     return (
//       <ErrorNotification
//         description={"Diese Seite gibt es nicht."}
//       ></ErrorNotification>
//     );
//   }

//   const handleSubmit = (status) => {
//     const formData = updateItemFormData(
//       item.title,
//       item.categoryId,
//       item.description,
//       item.locationId,
//       item.postcode,
//       item.locationExtraInfo,
//       item.price,
//       images,
//       status,
//     );
//     updateItemRequest(fetchedItem.id, userId, formData);
//   };

//   const handleSaveDraft = () => {
//     handleSubmit("DRAFT");
//   };
//   const handlePublishItem = () => {
//     handleSubmit("ACTIVE");
//   };

//   return (
//     <div className="container-fluid ">
//       <h2 className="py-2 m-0">Anzeigendetails</h2>
//       <ItemForm
//         item={item}
//         setItem={setItem}
//         images={images}
//         setImages={setImages}
//         initialLocationInputValue={initialLocationInputValue}
//       >
//         {/* Entwurf speichern */}
//         <button
//           className="btn btn-outline-success"
//           type="button"
//           onClick={handleSaveDraft}
//         >
//           Entwurf speichern
//         </button>
//         {/* Anzeige veröffentlichen */}
//         <button
//           className="btn btn-primary"
//           type="button"
//           onClick={handlePublishItem}
//         >
//           Anzeige veröffentlichen
//         </button>
//       </ItemForm>
//       <div className="py-4"></div>
//     </div>
//   );
// }
