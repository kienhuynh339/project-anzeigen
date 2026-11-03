package sp.backend.item;

import jakarta.validation.constraints.NotNull;
import sp.backend.utils.Utils.ExceptionMessage;

public class UpdateItemStatusRequest {
    @NotNull(message = ExceptionMessage.STATUS_NOT_NULL)
    private ItemStatus status;

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
