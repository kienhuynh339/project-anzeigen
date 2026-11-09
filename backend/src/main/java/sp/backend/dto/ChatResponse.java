/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import jakarta.persistence.EntityNotFoundException;
import sp.backend.chat.Chat;
import sp.backend.chat.message.Message;
import sp.backend.exception.ExceptionMessage;
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

    public ChatResponse(Chat chat, MessageResponse lastMessage) {
        this.id = chat.getId();
        this.itemId = chat.getItem().getId();
        this.itemTitle = chat.getItem().getTitle();
        this.itemStatus = chat.getItem().getStatus();
        this.poster = new UserPublicResponse(chat.getPoster());
        this.interestedUser = new UserPublicResponse(chat.getInterestedUser());
        this.lastMessage = lastMessage;
        this.fistDeletedByUserId = chat.getFirstDeletedByUser() != null ? chat.getFirstDeletedByUser().getId() : null;
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
