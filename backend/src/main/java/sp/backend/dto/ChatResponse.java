/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.item.ItemStatus;

public class ChatResponse {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private ItemStatus itemStatus;
    private UserPublicResponse poster;
    private UserPublicResponse interestedUser;
    private MessageResponse lastMessage;
    private Long fistDeletedByUserId;

    public ChatResponse(Long id, Long itemId, String itemTitle, ItemStatus itemStatus, UserPublicResponse poster, UserPublicResponse interestedUser, MessageResponse lastMessage, Long fistDeletedByUserId) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemStatus = itemStatus;
        this.poster = poster;
        this.interestedUser = interestedUser;
        this.lastMessage = lastMessage;
        this.fistDeletedByUserId = fistDeletedByUserId;
    }


    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public UserPublicResponse getPoster() {
        return poster;
    }

    public UserPublicResponse getInterestedUser() {
        return interestedUser;
    }

    public MessageResponse getLastMessage() {
        return lastMessage;
    }

    public Long getFistDeletedByUserId() {
        return fistDeletedByUserId;
    }

    public void setLastMessage(MessageResponse lastMessage) {
        this.lastMessage = lastMessage;
    }
}
