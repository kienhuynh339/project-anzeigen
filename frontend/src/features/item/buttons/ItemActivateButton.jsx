/**
 * @author Trung Kien Huynh
 */

import { useState } from "react";
import { updateItemStatus as apiUpdateItemStatus } from "../../../apis/itemApi";
import { useAppContext } from "../../../contexts/AppContext";
import ErrorDialog from "../../../components/ErrorDialog";

export default function ItemActivateButton({
  itemId,
  updateItemStatus = () => {},
}) {
  const { openDialog } = useAppContext();

  const [isActivating, setIsActivating] = useState(false);

  const handleActivateItem = async (event) => {
    setIsActivating(true);
    event.preventDefault();

    try {
      await apiUpdateItemStatus(itemId, "ACTIVE");
      updateItemStatus();
    } catch (e) {
      openDialog(ErrorDialog, { errorMessage: e.message });
    }

    setIsActivating(false);
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-success`}
      onClick={handleActivateItem}
      disabled={isActivating}
    >
      Aktivieren
    </button>
  );
}
