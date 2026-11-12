/**
 * @author Trung Kien Huynh
 */
import { translateItemStatus } from "../../utils/translateItemStatus";
import { truncateText } from "../../utils/truncatetext";

export default function ItemTitle({
  itemStatus,
  itemTitle,
  textLimit = undefined,
}) {
  return (
    <span>
      {itemStatus && itemStatus !== "ACTIVE" && (
        // Anzeigezustand
        <span className="text-secondary pe-1">
          {translateItemStatus(itemStatus)} •
        </span>
      )}
      {/* Anzeigetitel */}
      {truncateText(itemTitle, textLimit)}
    </span>
  );
}
