/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import sp.backend.utils.Utils.ExceptionMessage;

public class MessageCreateRequest {
    @NotNull(message = ExceptionMessage.ITEM_ID_NOT_NULL)
    @Positive(message = ExceptionMessage.ITEM_ID_POSITIVE)
    private Long itemId;

    @NotNull(message = ExceptionMessage.USER_ID_NOT_NULL)
    @Positive(message = ExceptionMessage.USER_ID_POSITIVE)
    private Long interestedUserId;

    @NotEmpty(message = ExceptionMessage.MESSAGE_NOT_EMPTY)
    @Size(max = 4000, message = ExceptionMessage.MESSAGE_SIZE)
    private String content;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getInterestedUserId() {
        return interestedUserId;
    }

    public void setInterestedUserId(Long interestedUserId) {
        this.interestedUserId = interestedUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
