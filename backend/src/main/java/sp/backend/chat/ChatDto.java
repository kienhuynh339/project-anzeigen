package sp.backend.chat;

import sp.backend.chat.message.MessageDto;
import sp.backend.item.ItemStatus;
import sp.backend.user.UserPublicDto;

public class ChatDto {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private ItemStatus itemStatus;
    private UserPublicDto poster;
    private UserPublicDto interestedUser;
    private MessageDto lastMessage;


    public ChatDto(Long id, Long itemId, String itemTitle, ItemStatus itemStatus, UserPublicDto poster, UserPublicDto interestedUser, MessageDto lastMessage) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemStatus = itemStatus;
        this.poster = poster;
        this.interestedUser = interestedUser;
        this.lastMessage = lastMessage;
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

    public UserPublicDto getPoster() {
        return poster;
    }

    public UserPublicDto getInterestedUser() {
        return interestedUser;
    }

    public MessageDto getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDto lastMessage) {
        this.lastMessage = lastMessage;
    }
}
