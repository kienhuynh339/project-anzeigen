/**
 * @author Trung Kien Huynh
 */

import { updateItemStatus } from "../api/itemApi";

export default function ItemActivateButton({ itemId }) {
  const handleItemActivate = async (event) => {
    event.preventDefault();
    await updateItemStatus(itemId, "ACTIVE").catch((error) =>
      console.log(error),
    );
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-success`}
      onClick={handleItemActivate}
    >
      Aktivieren
    </button>
  );
}
