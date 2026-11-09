package sp.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sp.backend.exception.ExceptionMessage;

public class ItemFavoriteCreateRequest {
    @NotNull(message = ExceptionMessage.ITEM_ID_NOT_NULL)
    @Positive(message = ExceptionMessage.ITEM_ID_POSITIVE)
    private Long itemId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

}
