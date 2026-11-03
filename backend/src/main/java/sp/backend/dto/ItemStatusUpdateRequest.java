package sp.backend.dto;

import jakarta.validation.constraints.NotNull;
import sp.backend.item.ItemStatus;
import sp.backend.utils.Utils.ExceptionMessage;

public class ItemStatusUpdateRequest {
    @NotNull(message = ExceptionMessage.STATUS_NOT_NULL)
    private ItemStatus status;

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
