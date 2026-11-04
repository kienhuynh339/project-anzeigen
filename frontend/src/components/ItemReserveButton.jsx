/**
 * @author Trung Kien Huynh
 */
import { updateItemStatus } from "../api/itemApi";
export default function ItemReserveButton({ itemId }) {
  const handleItemReserved = async (event) => {
    event.preventDefault();
    await updateItemStatus(itemId, "RESERVED").catch((error) =>
      console.log(error),
    );
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
