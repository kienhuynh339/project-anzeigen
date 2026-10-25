import ItemList from "./ItemList";
import { useNavigate } from "react-router";

const userId = "1";

export default function UserItems() {
  const navigate = useNavigate();

  const urlWithoutPaginationAndSorting = `http://localhost:8080/api/items/user/${userId}?`;
  const PAGE_SIZE = 9999;
  const title = "Meine Anzeigen";
  const additionalContentWhenItemAmountIsZero = (
    <div className="d-flex flex-column gap-2">
      <div>Du hast keine Anzeigen.</div>
      <button
        className="btn btn-outline-primary"
        onClick={() => navigate("/anzeige-erstellen")}
      >
        <i className="bi bi-plus-circle"></i> Anzeige erstellen
      </button>
    </div>
  );

  return (
    <ItemList
      urlWithoutPaginationAndSorting={urlWithoutPaginationAndSorting}
      pageSize={PAGE_SIZE}
      title={title}
      showItemAmount={false}
      additionalContentWhenItemAmountIsZero={
        additionalContentWhenItemAmountIsZero
      }
    ></ItemList>
  );
}
