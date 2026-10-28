/**
 * @author Trung Kien Huynh
 */

import { patchRequest } from "../utils/patchRequest";
export default function ItemActivateButton({ itemId }) {
  const handleItemActivate = async (event) => {
    event.preventDefault();
    await patchRequest(
      `http://localhost:8080/api/items/${itemId}`,
      "status",
      "ACTIVE",
    ).catch((error) => console.log(error));
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
