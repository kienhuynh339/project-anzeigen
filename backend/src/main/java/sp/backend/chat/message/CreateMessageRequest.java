/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;

public class CreateMessageRequest {
    private Long itemId;
    private Long interestedUserId;
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
