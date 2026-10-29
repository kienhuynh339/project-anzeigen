/**
 * @author Trung Kien Huynh
 */
import { patchRequest } from "../utils/patchRequest";
export default function ItemActivateOrReserveButton({ itemId }) {
  const handleItemReserved = async (event) => {
    event.preventDefault();
    await patchRequest(
      `http://localhost:8080/api/items/${itemId}`,
      "status",
      "RESERVED",
    ).catch((error) => console.log(error));
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-primary`}
      onClick={handleItemReserved}
    >
      Reservieren
    </button>
  );
}
