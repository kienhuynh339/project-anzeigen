/**
 * @author Trung Kien Huynh
 */
import { useState } from "react";
import { updateItemStatus as apiUpdateItemStatus } from "../../../apis/itemApi";
import ErrorDialog from "../../../components/ErrorDialog";
import { useAppContext } from "../../../contexts/AppContext";

export default function ItemReserveButton({
  itemId,
  updateItemStatus = () => {},
}) {
  const { openDialog } = useAppContext();

  const [isReserving, setIsReserving] = useState(false);

  const handleReserveItem = async (event) => {
    setIsReserving(true);
    event.preventDefault();

    try {
      await apiUpdateItemStatus(itemId, "RESERVED");
      updateItemStatus();
    } catch (e) {
      openDialog(ErrorDialog, { errorMessage: e.message });
    }

    setIsReserving(false);
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-primary`}
      onClick={handleReserveItem}
      disabled={isReserving}
    >
      Reservieren
    </button>
  );
}
