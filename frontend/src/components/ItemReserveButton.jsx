import { useState } from "react";
import { patchRequest } from "../utils/patchRequest";
export default function ItemReserveButton({ itemId, setChatsUpdateTrigger }) {
  const handleItemReserved = async (event) => {
    await patchRequest(
      `http://localhost:8080/api/items/${itemId}`,
      "status",
      "RESERVED",
    ).catch((error) => console.log(error));
    setChatsUpdateTrigger((prev) => !prev);
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
