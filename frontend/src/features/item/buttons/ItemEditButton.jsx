/**
 * @author Trung Kien Huynh
 */
import { useNavigate } from "react-router";
export default function ItemEditButton({ itemId, itemStatus }) {
  const navigate = useNavigate();

  const handleNavigate = async (event) => {
    event.preventDefault();
    navigate(`/bearbeiten/${itemId}`);
  };

  return (
    <button
      type="button"
      className={`btn btn-outline-dark`}
      onClick={handleNavigate}
    >
      {itemStatus === "DRAFT" ? "Fortsetzen" : "Bearbeiten"}
    </button>
  );
}
