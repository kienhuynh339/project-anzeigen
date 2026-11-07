/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { updateItemStatus as apiUpdateItemStatus } from "../../../apis/itemApi";

export default function ItemReserveButton({
  itemId,
  updateItemStatus = () => {},
}) {
  const [reserving, setReserving] = useState(false);

  const handleItemReserved = async (event) => {
    setReserving(true);

    event.preventDefault();
    await apiUpdateItemStatus(itemId, "RESERVED").catch((error) =>
      console.log(error),
    );
    updateItemStatus();

    setReserving(false);
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-primary`}
      onClick={handleItemReserved}
      disabled={reserving}
    >
      Reservieren
    </button>
  );
}
