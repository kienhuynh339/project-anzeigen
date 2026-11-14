/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.chat.Chat;
import sp.backend.item.ItemStatus;

public class ChatResponse {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private ItemStatus itemStatus;
    private UserPublicResponse poster;
    private UserPublicResponse interestedUser;
    private MessageResponse lastMessage;
    private Long firstDeletedByUserId;

    public ChatResponse(Long id, Long itemId, String itemTitle, ItemStatus itemStatus, UserPublicResponse poster, UserPublicResponse interestedUser, MessageResponse lastMessage, Long firstDeletedByUserId) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemStatus = itemStatus;
        this.poster = poster;
        this.interestedUser = interestedUser;
        this.lastMessage = lastMessage;
        this.firstDeletedByUserId = firstDeletedByUserId;
    }

    public ChatResponse(Chat chat, MessageResponse lastMessage) {
        this.id = chat.getId();
        this.itemId = chat.getItem().getId();
        this.itemTitle = chat.getItem().getTitle();
        this.itemStatus = chat.getItem().getStatus();
        this.poster = new UserPublicResponse(chat.getPoster());
        this.interestedUser = new UserPublicResponse(chat.getInterestedUser());
        this.lastMessage = lastMessage;
        this.firstDeletedByUserId = chat.getFirstDeletedByUser() != null ? chat.getFirstDeletedByUser().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public UserPublicResponse getPoster() {
        return poster;
    }

    public void setPoster(UserPublicResponse poster) {
        this.poster = poster;
    }

    public UserPublicResponse getInterestedUser() {
        return interestedUser;
    }

    public void setInterestedUser(UserPublicResponse interestedUser) {
        this.interestedUser = interestedUser;
    }

    public MessageResponse getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageResponse lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getFirstDeletedByUserId() {
        return firstDeletedByUserId;
    }

    public void setFirstDeletedByUserId(Long firstDeletedByUserId) {
        this.firstDeletedByUserId = firstDeletedByUserId;
    }
}
