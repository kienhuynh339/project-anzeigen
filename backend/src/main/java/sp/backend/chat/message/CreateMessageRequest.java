/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;

public class CreateMessageRequest {
    private Long itemId;
    private Long interestedUserId;
    private Long senderId;
    private String content;


    public CreateMessageRequest(Long itemId, Long interestedUserId, Long senderId, String content) {
        this.itemId = itemId;
        this.interestedUserId = interestedUserId;
        this.senderId = senderId;
        this.content = content;
    }

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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
