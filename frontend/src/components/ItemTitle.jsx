/**
 * @author Trung Kien Huynh
 */
import { translateItemStatus } from "../utils/translateItemStatus";
import { truncateText } from "../utils/truncatetext";

export default function ItemTitle({
  itemStatus,
  itemTitle,
  textLimit = undefined,
}) {
  return (
    <span>
      {itemStatus && itemStatus !== "ACTIVE" && (
        <span className="text-secondary pe-1">
          {translateItemStatus(itemStatus)} •
        </span>
      )}
      {truncateText(itemTitle, textLimit)}
    </span>
  );
}
