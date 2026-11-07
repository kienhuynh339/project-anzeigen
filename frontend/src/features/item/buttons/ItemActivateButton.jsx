/**
 * @author Trung Kien Huynh
 */

import { useState } from "react";
import { updateItemStatus as apiUpdateItemStatus } from "../../../apis/itemApi";

export default function ItemActivateButton({
  itemId,
  updateItemStatus = () => {},
}) {
  const [activating, setActivating] = useState(false);

  const handleItemActivate = async (event) => {
    setActivating(true);

    event.preventDefault();
    await apiUpdateItemStatus(itemId, "ACTIVE").catch((error) =>
      console.error(error),
    );
    updateItemStatus();

    setActivating(false);
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-success`}
      onClick={handleItemActivate}
      disabled={activating}
    >
      Aktivieren
    </button>
  );
}
