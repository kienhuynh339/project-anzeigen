/**
 * @author Trung Kien Huynh
 */
import { useNavigate } from "react-router";
export default function ItemEditButton({ itemId, itemStatus }) {
  const navigate = useNavigate();
  const handleOnClick = async (event) => {
    event.preventDefault();
    navigate(`/bearbeiten/${itemId}`);
  };
  return (
    <button
      type="button"
      className={`btn btn-outline-dark`}
      onClick={handleOnClick}
    >
      {itemStatus === "DRAFT" ? "Fortsetzen" : "Bearbeiten"}
    </button>
  );
}
